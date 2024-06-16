//
//  RoboflowYolo.swift
//  Nourri_iOS
//
//  Created by Calvin Anacia Suciawan on 20-01-2024.
//

import Foundation

import UIKit

func uploadImageToInferenceServer(image: UIImage) async -> [Ingredient] {
    if let imageData = image.jpegData(compressionQuality: 0.5) {
        
        guard let model_endpoint = Bundle.main.infoDictionary?["AI_MODEL_ENDPOINT"] as? String,
              let api_key = Bundle.main.infoDictionary?["AI_API_KEY"] as? String else {
            debugPrint("MODEL API ERROR")
            return []
        }
        
        let url = URL(string: "https://detect.roboflow.com/\(model_endpoint)?api_key=\(api_key)&name=YOUR_IMAGE.jpg&confidence=0.3&overlap=0.9")!
        var request = URLRequest(url: url)
        request.httpMethod = "POST"
        request.addValue("application/x-www-form-urlencoded", forHTTPHeaderField: "Content-Type")
        request.addValue(String(imageData.base64EncodedString().count), forHTTPHeaderField: "Content-Length")
        request.httpBody = imageData.base64EncodedString().data(using: .utf8)
        
        do {
            let (data, _) = try await URLSession.shared.data(for: request)
            
            if let jsonResponse = try JSONSerialization.jsonObject(with: data, options: []) as? [String: Any] {
                let predictions = jsonResponse["predictions"] as? [[String: Any]] ?? []
                
                var ingredientList: [Ingredient] = []
                
                for prediction in predictions {
                    guard let className = prediction["class"] as? String else {
                        continue
                    }
                    
                    if let ingredient = await getIngredient(name: className) {
                        if !ingredientList.contains(ingredient) {
                            ingredientList.append(ingredient)
                        }
                    }
                }
                
                debugPrint(ingredientList)
                return ingredientList
            } else {
                debugPrint("MODEL NO RESPONSE")
                return []
            }
        } catch {
            debugPrint("MODEL DATA ERROR")
            return []
        }
    } else {
        return []
    }
}

func getIngredient(name: String) async -> Ingredient? {
    guard let domain = Bundle.main.infoDictionary?["API_URL"] as? String,
      let url = URL(string: "\(domain)/get-ingredient-info/\(name)") else {
        return nil
    }

    do {
        let (data, _) = try await URLSession.shared.data(from: url)
        let recipe = try JSONDecoder().decode(Ingredient.self, from: data)
        
        return recipe
    } catch {
        return nil
    }
}


