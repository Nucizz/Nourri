//
//  RecipeView.swift
//  Nourri_iOS
//
//  Created by Calvin Anacia Suciawan on 08-01-2024.
//

import SwiftUI

struct RecipeView: View {
    
    @StateObject private var ingredientListViewModel = IngredientListViewModel()
    @StateObject public var recipeListViewModel: RecipeListViewModel
    @State private var path = NavigationPath()
    
    var body: some View {
        NavigationStack(path: $path) {
            
            ZStack(alignment: .leading) {
                
                VStack(alignment: .leading) {
                    
                    Text("New Recipe")
                        .fontWeight(/*@START_MENU_TOKEN@*/.bold/*@END_MENU_TOKEN@*/)
                        .font(/*@START_MENU_TOKEN@*/.title/*@END_MENU_TOKEN@*/)
                        .foregroundColor(Color.init(red: 0, green: 0.75, blue: 0))
                    
                    if ingredientListViewModel.isGenerating || ingredientListViewModel.isLoading {
                        
                        LoadingView()
                        
                    } else {
                        
                        VStack {
                            
                            List {
                                    
                                ForEach(ingredientListViewModel.ingredientList.indices, id: \.self) { index in

                                    IngredientViewCell(ingredient: ingredientListViewModel.ingredientList[index])
                                        .swipeActions {
                                            Button(role: .destructive) {
                                                ingredientListViewModel.removeIngredientByIndex(index: index)
                                            } label: {
                                                Label("Delete", systemImage: "trash")
                                            }
                                        }

                                }
                                                        
                            }
                            .listStyle(.plain)
                            .clipShape(
                                .rect(
                                    topLeadingRadius: 15,
                                    bottomLeadingRadius: 15, 
                                    bottomTrailingRadius: 15,
                                    topTrailingRadius: 15
                                )
                            )
                            
                        }
                        
                    }
                                
                }
                .padding(.horizontal, 20)
                .padding(.vertical, 15)
                
                FloatingActionButton(ingredientListViewModelReference: ingredientListViewModel, recipeListViewModel: recipeListViewModel, navigationPath: $path)
                
            }
            .background(Color.gray.opacity(0.15))
            .navigationDestination(for: String.self) { view in
                if view == "NewRecipe" {
                    RecipeDetailView(recipe: recipeListViewModel.recipeList.first)
                }}
            
        }
            
    }
}

#Preview {
    RecipeView(recipeListViewModel: RecipeListViewModel())
}
