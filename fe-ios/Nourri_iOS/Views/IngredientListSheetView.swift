//
//  IngredientListSheetView.swift
//  Nourri_iOS
//
//  Created by Calvin Anacia Suciawan on 18-01-2024.
//

import SwiftUI

struct IngredientListSheetView: View {
    
    @StateObject public var ingredientListViewModelReference: IngredientListViewModel
    @Environment(\.presentationMode) var presentationMode
    
    var body: some View {
        
        VStack(alignment: .center) {
            
            HStack(alignment: .center) {
                
                Text("Choose an Ingredient")
                    .fontWeight(/*@START_MENU_TOKEN@*/.bold/*@END_MENU_TOKEN@*/)
                    .font(/*@START_MENU_TOKEN@*/.title/*@END_MENU_TOKEN@*/)
                    .foregroundColor(Color.init(red: 0, green: 0.75, blue: 0))
                
            }
            .padding(.bottom, 15)
            
            if ingredientListViewModelReference.isLoading {
                
                LoadingView()
                
            } else {
                
                List {
                        
                    ForEach(ingredientListViewModelReference.selectableIngredientList, id: \.id) { ingredient in
                        
                        IngredientViewCell(ingredient: ingredient)
                            .onTapGesture {
                                ingredientListViewModelReference.addIngredientByData(ingredient: ingredient)
                                presentationMode.wrappedValue.dismiss()
                            }
                        
                    }
                    
                }
                .listStyle(.plain)
                .cornerRadius(15)
                                
            }
                        
        }
        .padding(.horizontal, 20)
        .padding(.vertical, 15)
        .background(Color.gray.opacity(0.15))
        
    }
}
