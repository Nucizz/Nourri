//
//  RecipeViewCell.swift
//  Nourri_iOS
//
//  Created by Calvin Anacia Suciawan on 13-01-2024.
//

import SwiftUI

struct RecipeViewCell: View {
    
    let recipe: Recipe
    
    var body: some View {
    
        NavigationLink(destination: RecipeDetailView(recipe: recipe)) {
            
            VStack(alignment: .leading) {
                
                Text(recipe.title)
                    .font(.title2)
                    .fontWeight(.semibold)
                    .lineLimit(1)
                    .foregroundColor(Color.green)
                
                Text(recipe.summary)
                    .font(.body)
                    .foregroundColor(Color.secondary)
                    .lineLimit(2)
                    .multilineTextAlignment(.leading)
                    
            }
            
        }
        .padding(.bottom, 5)
    
    }
}

#Preview {
    RecipeViewCell(recipe: Recipe(id: 1, title: "Cookie", ingredients: "Dough", instructions: "Bake", summary: "Yummy but Unhealthy", raw: "Raw contents here!"))
}
