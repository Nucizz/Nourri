//
//  RecipeDetailView.swift
//  Nourri_iOS
//
//  Created by Calvin Anacia Suciawan on 14-01-2024.
//

import SwiftUI

struct RecipeDetailView: View {
    
    let recipe: Recipe?
    @State private var isTidy: Bool = true
    
    var body: some View {
            
        ScrollView(.vertical, showsIndicators: false) {
            
            VStack(alignment: .leading) {
                
                Text(recipe?.title ?? "Undefined")
                    .font(.title2)
                    .fontWeight(.bold)
                    .foregroundColor(Color.green)
                    .padding(.bottom, 15)
                    .onTapGesture {
                        isTidy = !isTidy
                    }
                
                if(isTidy) {
                    
                    Text(recipe?.summary ?? "null")
                        .font(.body)
                        .foregroundColor(Color.secondary)
                        .padding(.bottom, 15)
                    
                    Text("Ingredients")
                        .font(.title3)
                        .fontWeight(.semibold)
                        .foregroundColor(Color.green)
                    
                    Text(recipe?.ingredients ?? "null")
                        .font(.body)
                        .foregroundColor(Color.secondary)
                        .padding(.bottom, 15)
                    
                    Text("Instructions")
                        .font(.title3)
                        .fontWeight(.semibold)
                        .foregroundColor(Color.green)
                    
                    Text(recipe?.instructions ?? "null")
                        .font(.body)
                        .foregroundColor(Color.secondary)
                    
                } else {
                    
                    Text(recipe?.raw ?? "Nothing to show here...")
                        .font(.body)
                        .foregroundColor(Color.secondary)
                    
                }
                
            }
            
        }
        .padding(.horizontal, 20)
        .padding(.vertical, 15)
        .background(Color.gray.opacity(0.15))
        
    }
}

#Preview {
    RecipeDetailView(recipe: Recipe(id: UUID().uuidString, title: "Cookie", ingredients: "Dough", instructions: "Bake", summary: "Yummy but Unhealthy", raw: "Raw contents here!"))
}
