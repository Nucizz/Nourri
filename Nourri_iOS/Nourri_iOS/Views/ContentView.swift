//
//  ContentView.swift
//  Nourri_iOS
//
//  Created by Calvin Anacia Suciawan on 08-01-2024.
//

import SwiftUI

struct ContentView: View {
    @StateObject var recipeListViewModel = RecipeListViewModel()
    
    var body: some View {
        TabView {
            HomeView(recipeListViewModel: recipeListViewModel)
                .tabItem {
                    Label("Home", systemImage: "house.fill")
                }
            RecipeView(recipeListViewModel: recipeListViewModel)
                .tabItem {
                    Label("Recipe", systemImage: "book.pages.fill")
                }
            AboutView()
                .tabItem {
                    Label("About", systemImage: "person.2.fill")
                }
        }
        .accentColor(Color.green)
        .navigationBarBackButtonHidden(true)
        .onAppear {
            let blurView = UIVisualEffectView(effect: UIBlurEffect(style: .systemMaterial))
            blurView.frame = CGRect(x: 0, y: 0, width: UIScreen.main.bounds.width, height: 50)
            blurView.autoresizingMask = [.flexibleWidth, .flexibleHeight]
            
            UITabBar.appearance().insertSubview(blurView, at: 0)
        }
    }
}

#Preview {
    ContentView()
}
