//
//  Recipe.swift
//  Nourri_iOS
//
//  Created by Calvin Anacia Suciawan on 13-01-2024.
//

import Foundation

struct Recipe: Identifiable, Codable {
    let id: Int
    let title: String
    let ingredients: String
    let instructions: String
    let summary: String
    let raw: String
}
