//
//  FloatingActionButton.swift
//  Nourri_iOS
//
//  Created by Calvin Anacia Suciawan on 13-01-2024.
//

import SwiftUI

struct FloatingActionButton: View {
    var body: some View {
        
        VStack { // Floating Action Button
            
            Spacer()
            
            HStack {
                
                Spacer()
                
                Button(action: {
                    // Action for the button
                }) {
                    Image(systemName: "plus")
                }
                .frame(width: 50, height: 50)
                .foregroundColor(Color.white)
                .background(Color.green)
                .cornerRadius(25)
                .padding(.horizontal, 20)
                .padding(.vertical, 15)
            }
            
        }
        
    }
}

#Preview {
    FloatingActionButton()
}
