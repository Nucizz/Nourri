//
//  AboutView.swift
//  Nourri_iOS
//
//  Created by Calvin Anacia Suciawan on 08-01-2024.
//

import SwiftUI

struct AboutView: View {
    
    let imageSize: CGFloat = (UIScreen.main.bounds.width - 85) / 3
    
    var body: some View {
        
        VStack(alignment: .leading) {
            
            Text("About Us")
                .fontWeight(/*@START_MENU_TOKEN@*/.bold/*@END_MENU_TOKEN@*/)
                .font(/*@START_MENU_TOKEN@*/.title/*@END_MENU_TOKEN@*/)
                .foregroundColor(Color.init(red: 0, green: 0.75, blue: 0))
            
            ScrollView(.vertical, showsIndicators: false) {
                
                VStack(alignment: .leading) {
                    
                    Text("Welcome to Nourri, where technology meets nutrition! We're an AI-based app with a simple mission: to help you make healthier choices in your meals. Nourri, derived from the French word for \"to feed,\" embodies our commitment to nourishing your well-being.\n\nWe've harnessed the power of OpenAI to revolutionize your culinary journey. Nourri is not just an app; it's your kitchen companion, using OpenAI to generate personalized, nutritious recipes tailored to your preferences.\n\nOur three dedicated developers are the heart and soul behind the app.")
                        .multilineTextAlignment(.leading)
                        .font(.body)
                        .foregroundColor(.secondary)
                    
                    HStack(alignment: .top) {
                                        
                        VStack(alignment: .center) {
                            
                            Image("calvin")
                                .resizable()
                                .aspectRatio(1, contentMode: .fill)
                                .cornerRadius(15)
                                .frame(width: imageSize, height: imageSize)
                                .clipped()
                            
                            Text("Calvin A S")
                                .font(.subheadline)
                                .fontWeight(.semibold)
                                .multilineTextAlignment(.center)
                            
                            Text("iOS & Backend Developer")
                                .font(.caption)
                                .fontWeight(.semibold)
                                .foregroundColor(Color.secondary)
                                .multilineTextAlignment(.center)
                            
                        }
                        
                        Spacer()
                        
                        VStack(alignment: .center) {
                            
                            Image("thessa")
                                .resizable()
                                .aspectRatio(1, contentMode: .fill)
                                .cornerRadius(15)
                                .frame(width: imageSize, height: imageSize)
                                .clipped()
                            
                            Text("Thessalonica")
                                .font(.subheadline)
                                .fontWeight(.semibold)
                                .multilineTextAlignment(.center)
                            
                            Text("AI Engineer")
                                .font(.caption)
                                .fontWeight(.semibold)
                                .foregroundColor(Color.secondary)
                                .multilineTextAlignment(.center)
                            
                        }
                        
                        Spacer()
                        
                        VStack(alignment: .center) {
                            
                            Image("william")
                                .resizable()
                                .aspectRatio(1, contentMode: .fill)
                                .cornerRadius(15)
                                .frame(width: imageSize, height: imageSize)
                                .clipped()
                            
                            Text("William Ng")
                                .font(.subheadline)
                                .fontWeight(.semibold)
                                .multilineTextAlignment(.center)
                            
                            Text("Android Developer")
                                .font(.caption)
                                .fontWeight(.semibold)
                                .foregroundColor(Color.secondary)
                                .multilineTextAlignment(.center)
                            
                        }
                        
                    }
                    
                }
                
            }
                        
        }
        .padding(.horizontal, 20)
        .padding(.vertical, 15)
        .background(Color.gray.opacity(0.15))
        
    }
    
}

#Preview {
    AboutView()
}
