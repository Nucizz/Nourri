//
//  IngredientViewCell.swift
//  Nourri_iOS
//
//  Created by Calvin Anacia Suciawan on 18-01-2024.
//

import SwiftUI

struct IngredientViewCell: View {
    
    let ingredient: Ingredient
    
    var body: some View {
        
        HStack(alignment: .center) {
            
            Text(ingredient.name)
                .font(.title2)
                .fontWeight(.semibold)
                .lineLimit(1)
            
            Spacer()
            
            Text("\(String(format: "%.2f", ingredient.ccal * 100)) ccal/oz")
                .font(.body)
                .foregroundColor(Color.secondary)
                .lineLimit(1)
            
        }
        .padding(.vertical, 5)
        .contentShape(Rectangle())
        
    }
}

#Preview {
    IngredientViewCell(ingredient: Ingredient(id: 1, name: "Chicken", ccal: 1.2343))
}
