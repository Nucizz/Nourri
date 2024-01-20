//
//  FloatingActionButton.swift
//  Nourri_iOS
//
//  Created by Calvin Anacia Suciawan on 13-01-2024.
//

import SwiftUI

struct FloatingActionButton: View {
    
    @State private var isActionSheetPresented = false
    @State private var isIngredientListSheetPresented = false
    @StateObject public var ingredientListViewModelReference: IngredientListViewModel
    @StateObject public var recipeListViewModel: RecipeListViewModel
    @State private var isRecipeDetailViewPresented = false
    @State private var generatedRecipe: Recipe?
    
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
                                    generatedRecipe = newRecipe
                                    isRecipeDetailViewPresented.toggle()
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
                    .fullScreenCover(isPresented: $isRecipeDetailViewPresented) {
                        RecipeDetailView(recipe: generatedRecipe!)
                    }
                    
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
                            // Handle Option 1
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
                
            }
            .padding(.horizontal, 20)
            .padding(.vertical, 15)
            
        }
        
    }
}
