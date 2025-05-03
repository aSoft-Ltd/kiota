//
//  ContentView.swift
//  SystemSample
//
//  Created by Anderson Lameck on 28/04/2025.
//

import SwiftUI
import SystemSampleUI
import UniformTypeIdentifiers

struct ComposeView: UIViewControllerRepresentable {
    
    let bridge = SampleBridge()
    func makeUIViewController(context: Context) -> UIViewController {
        bridge.make()
    }

    func updateUIViewController(_ controller: UIViewController, context: Context) {
        
    }
}


struct ContentView: View {
    var body: some View {
        ComposeView()
    }
}

#Preview {
    ContentView()
}
