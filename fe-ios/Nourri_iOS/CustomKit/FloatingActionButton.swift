//
//  FloatingActionButton.swift
//  Nourri_iOS
//
//  Created by Calvin Anacia Suciawan on 13-01-2024.
//

import SwiftUI
import AVFoundation

struct FloatingActionButton: View {
    
    @State private var isActionSheetPresented = false
    @State private var isIngredientListSheetPresented = false
    @StateObject public var ingredientListViewModelReference: IngredientListViewModel
    @StateObject public var recipeListViewModel: RecipeListViewModel
    @Binding public var navigationPath: NavigationPath
    @State private var isImagePickerPresented = false
    @State private var selectedImage: UIImage?
    
    var body: some View {
        
        VStack {
            
            Spacer()
            
            HStack {
                
                if ingredientListViewModelReference.ingredientList.count > 0 {
                
                    Button("Generate Recipe",
                       action: {
                        Task {
                            if let newRecipe = await ingredientListViewModelReference.generateRecipe(ingredientList: ingredientListViewModelReference.ingredientList) {
                                DispatchQueue.main.async {
                                    recipeListViewModel.addData(recipe: newRecipe)
                                    navigationPath.append("NewRecipe")
                                }
                            }
                        }
                    })
                    .frame(minWidth: 0, maxWidth: .infinity)
                    .frame(height: 50)
                    .font(.title2)
                    .fontWeight(.semibold)
                    .foregroundColor(Color.white)
                    .background(ingredientListViewModelReference.isGenerating ? Color.gray : Color.green)
                    .cornerRadius(50)   
                    .disabled(ingredientListViewModelReference.isGenerating)
                    
                }
                
                Spacer(minLength: 15)
                
                Button(action: {
                    isActionSheetPresented.toggle()
                }) {
                    Image(systemName: "plus")
                }
                .frame(width: 50, height: 50)
                .foregroundColor(Color.white)
                .font(.title2)
                .fontWeight(.semibold)
                .disabled(ingredientListViewModelReference.isGenerating)
                .background(ingredientListViewModelReference.isGenerating ? Color.gray : Color.green)
                .cornerRadius(25)
                .actionSheet(isPresented: $isActionSheetPresented) {
                    ActionSheet(title: Text("Choose a method"), buttons: [
                        .default(Text("Open Camera")) {
                            isImagePickerPresented.toggle()
                        },
                        .default(Text("Add Manually")) {
                            isIngredientListSheetPresented.toggle()
                        },
                        .cancel()
                    ])
                }
                .sheet(isPresented: $isIngredientListSheetPresented) {
                    IngredientListSheetView(ingredientListViewModelReference: ingredientListViewModelReference)
                }
                .fullScreenCover(isPresented: $isImagePickerPresented) {
                    ImagePicker(selectedImage: $selectedImage, isImagePickerPresented: $isImagePickerPresented, ingredientListViewModelReference: ingredientListViewModelReference)
                }
                
            }
            .padding(.horizontal, 20)
            .padding(.vertical, 15)
            
        }
        
    }
}

struct ImagePicker: UIViewControllerRepresentable {
    @Binding var selectedImage: UIImage?
    @Binding var isImagePickerPresented: Bool
    var ingredientListViewModelReference: IngredientListViewModel

    func makeUIViewController(context: Context) -> UIImagePickerController {
        let imagePicker = UIImagePickerController()
        imagePicker.sourceType = .camera
        imagePicker.delegate = context.coordinator
        return imagePicker
    }

    func updateUIViewController(_ uiViewController: UIImagePickerController, context: Context) {}

    func makeCoordinator() -> Coordinator {
        return Coordinator(parent: self)
    }
    
    init(selectedImage: Binding<UIImage?>, isImagePickerPresented: Binding<Bool>, ingredientListViewModelReference: IngredientListViewModel) {
            _selectedImage = selectedImage
            _isImagePickerPresented = isImagePickerPresented
            self.ingredientListViewModelReference = ingredientListViewModelReference
        }

    class Coordinator: NSObject, UINavigationControllerDelegate, UIImagePickerControllerDelegate {
        var parent: ImagePicker

        init(parent: ImagePicker) {
            self.parent = parent
        }

        func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [UIImagePickerController.InfoKey : Any]) {
            if let uiImage = info[.originalImage] as? UIImage {
                parent.selectedImage = uiImage
                parent.ingredientListViewModelReference.isLoading = true
                
                Task {
                    if let data = uiImage.jpegData(compressionQuality: 1.0) {
                        let scannedIngredient = await uploadImageToInferenceServer(image: UIImage(data: data)!)
                        DispatchQueue.main.async {
                            self.parent.ingredientListViewModelReference.ingredientList += scannedIngredient
                            self.parent.ingredientListViewModelReference.isLoading = false
                            self.parent.isImagePickerPresented = false
                        }
                    }
                }
            } else {
                parent.ingredientListViewModelReference.isLoading = false
            }
            parent.isImagePickerPresented = false
        }
    }
    
    private func requestCameraPermission() {
        AVCaptureDevice.requestAccess(for: .video) { granted in
            DispatchQueue.main.async {
                if granted {
                    self.isImagePickerPresented = true
                }
            }
        }
    }
    
}
