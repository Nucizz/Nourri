//
//  HomeView.swift
//  Nourri_iOS
//
//  Created by Calvin Anacia Suciawan on 08-01-2024.
//

import SwiftUI

struct HomeView: View {
    
    @StateObject private var recipeListViewModel = RecipeListViewModel()
    
    var body: some View {
        
        NavigationView {
            
            VStack(alignment: .leading) {
                
                Text(CUIKit.getGreetings())
                    .fontWeight(/*@START_MENU_TOKEN@*/.bold/*@END_MENU_TOKEN@*/)
                    .font(/*@START_MENU_TOKEN@*/.title/*@END_MENU_TOKEN@*/)
                    .foregroundColor(Color.init(red: 0, green: 0.75, blue: 0))
                
                if recipeListViewModel.isLoading {
                    
                    LoadingView()
                    
                } else {
                    
                    ScrollView(.vertical, showsIndicators: false) {
                            
                        ForEach(recipeListViewModel.recipeList, id: \.id) { recipe in
                            
                            RecipeViewCell(recipe: recipe)
                            
                            Divider()
                            
                        }
                        
                    }
                    
                }
                            
            }
            .padding(.horizontal, 20)
            .padding(.vertical, 15)
            .background(Color.gray.opacity(0.15))
            
        }
        
    }
    
}

struct HomeView_Previews: PreviewProvider {
    static var previews: some View {
        HomeView()
    }
}
