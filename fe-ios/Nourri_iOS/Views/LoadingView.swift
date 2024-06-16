//
//  LoadingView.swift
//  Nourri_iOS
//
//  Created by Calvin Anacia Suciawan on 14-01-2024.
//

import SwiftUI

struct LoadingView: View {
    var body: some View {
        
        VStack {
            
            Spacer()
            
            HStack {
                
                Spacer()
                
                ProgressView("Loading...")
                    .progressViewStyle(CircularProgressViewStyle())
                
                Spacer()
                
            }
            
            Spacer()
            
        }
        
    }
}

#Preview {
    LoadingView()
}
