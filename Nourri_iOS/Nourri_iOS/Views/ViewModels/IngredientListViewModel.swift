//
//  IngredientListViewModel.swift
//  Nourri_iOS
//
//  Created by Calvin Anacia Suciawan on 18-01-2024.
//

import Foundation

final class IngredientListViewModel: ObservableObject {
    
    @Published var ingredientList: [Ingredient] = []
    @Published var selectableIngredientList: [Ingredient] = []
    @Published var isLoading: Bool = true
    @Published var isGenerating: Bool = false
    
    init() {
        Task {
            await fetchSelectableData()
        }
    }
    
    func fetchSelectableData() async  {
        guard let domain = Bundle.main.infoDictionary?["API_URL"] as? String,
          let url = URL(string: "\(domain)/get-ingredient") else {
            DispatchQueue.main.async {
                self.isLoading = false
            }
            return
        }

        do {
            let (data, _) = try await URLSession.shared.data(from: url)
            let ingredients = try JSONDecoder().decode([Ingredient].self, from: data)
            
            DispatchQueue.main.async { [self] in
                selectableIngredientList = ingredients
                isLoading = false
            }
        } catch {
            print("Error fetching data: \(error)")
            DispatchQueue.main.async {
                self.isLoading = false
            }
        }
    }
    
    func addIngredientByData(ingredient: Ingredient) {
        ingredientList.append(ingredient)
    }
    
    func removeIngredientByIndex(index: Int) {
        ingredientList.remove(at: index)
    }
    
    func addIngredientByName(name: String) async {
        guard let domain = Bundle.main.infoDictionary?["API_URL"] as? String,
          let url = URL(string: "\(domain)/get-ingredient-info/\(name)") else {
            DispatchQueue.main.async {
                self.isLoading = false
            }
            return
        }

        do {
            let (data, _) = try await URLSession.shared.data(from: url)
            let ingredient = try JSONDecoder().decode(Ingredient.self, from: data)
            
            DispatchQueue.main.async { [self] in
                ingredientList.append(ingredient)
                isLoading = false
            }
        } catch {
            print("Error fetching data: \(error)")
            DispatchQueue.main.async {
                self.isLoading = false
            }
        }
    }
    
    func generateRecipe(ingredientList: [Ingredient]) async -> Recipe? {
        DispatchQueue.main.async {
            self.isGenerating = true
        }
        
        guard let domain = Bundle.main.infoDictionary?["API_URL"] as? String,
          let url = URL(string: "\(domain)/get-recipe") else {
            DispatchQueue.main.async {
                self.isGenerating = false
            }
            return nil
        }
        
        var request = URLRequest(url: url)
            request.httpMethod = "POST"
            request.addValue("application/json", forHTTPHeaderField: "Content-Type")

        do {
            request.httpBody = try JSONEncoder().encode(ingredientList)
            let data = try await URLSession.shared.data(for: request).0
            let recipe = try JSONDecoder().decode(Recipe.self, from: data)
            
            DispatchQueue.main.async { [self] in
                self.ingredientList.removeAll()
                isGenerating = false
            }
            
            return recipe
        } catch {
            print("Error generating recipe: \(error)")
            DispatchQueue.main.async {
                self.isGenerating = false
            }
            return nil
        }

    }
    
}
