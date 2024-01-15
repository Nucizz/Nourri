//
//  RecipeView.swift
//  Nourri_iOS
//
//  Created by Calvin Anacia Suciawan on 08-01-2024.
//

import SwiftUI

struct RecipeView: View {
    var body: some View {
        ZStack(alignment: .leading) {
            
            VStack(alignment: .leading) {
                
                Text("New Recipe")
                    .fontWeight(/*@START_MENU_TOKEN@*/.bold/*@END_MENU_TOKEN@*/)
                    .font(/*@START_MENU_TOKEN@*/.title/*@END_MENU_TOKEN@*/)
                    .foregroundColor(Color.init(red: 0, green: 0.75, blue: 0))
                
                ScrollView(.vertical, showsIndicators: false) {
                        
                    
                    
                }
                            
            }
            .padding(.horizontal, 20)
            .padding(.vertical, 15)
            
            FloatingActionButton()
            
        }
        .background(Color.gray.opacity(0.15))
            
    }
}

#Preview {
    RecipeView()
}
