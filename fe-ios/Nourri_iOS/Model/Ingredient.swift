//
//  Ingredient.swift
//  Nourri_iOS
//
//  Created by Calvin Anacia Suciawan on 18-01-2024.
//

import Foundation

struct Ingredient: Identifiable, Codable, Equatable {
    let id: Int
    let name: String
    let ccal: Double
}
