//
//  UIKit.swift
//  Nourri_iOS
//
//  Created by Calvin Anacia Suciawan on 08-01-2024.
//

import Foundation

class CUIKit {
    
    static func getGreetings() -> String {
        let hour = Calendar.current.component(.hour, from: Date())
        
        if (hour <= 10) {
            return "Good Morning"
        } else if (hour <= 13) {
            return "Good Day"
        } else if (hour <= 17) {
            return "Good Afternoon"
        } else {
            return "Good Night"
        }
    }
    
}
