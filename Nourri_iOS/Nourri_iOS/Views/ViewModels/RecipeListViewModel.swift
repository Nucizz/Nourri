//
//  RecipeListViewModel.swift
//  Nourri_iOS
//
//  Created by Calvin Anacia Suciawan on 13-01-2024.
//

import Foundation

final class RecipeListViewModel: ObservableObject {
    
    @Published var recipeList: [Recipe] = []
    @Published var isLoading: Bool = true
    
    init() {
        Task {
            await fetchData()
        }
    }
    
    func addData(recipe: Recipe) {
        recipeList.append(recipe)
    }
    
    func fetchData() async  {
        guard let domain = Bundle.main.infoDictionary?["API_URL"] as? String,
          let url = URL(string: "\(domain)/get-all-recipe") else {
            isLoading = false
            return
        }

        do {
            let (data, _) = try await URLSession.shared.data(from: url)
            let recipes = try JSONDecoder().decode([Recipe].self, from: data)
            
            DispatchQueue.main.async { [self] in
                recipeList = recipes
                isLoading = false
            }
        } catch {
            print("Error fetching data: \(error)")
            DispatchQueue.main.async {
                self.isLoading = false
            }
        }
    }
    
}
