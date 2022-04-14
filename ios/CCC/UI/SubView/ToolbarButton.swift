//
//  ToolbarButton.swift
//  CCC
//
//  Created by Mustafa Ozhan on 18/03/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import SwiftUI
import Resources

struct ToolbarButton: View {
    @Environment(\.colorScheme) var colorScheme

    var clickEvent: () -> Void
    var imgName: String

    var body: some View {
        Button(
            action: clickEvent,
            label: {
                Image(systemName: imgName)
                    .imageScale(.large)
                    .accentColor(MR.colors().text.get())
                    .padding(.leading, 10)
            }
        ).padding(.trailing, 10)
    }
}
