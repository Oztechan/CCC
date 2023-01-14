//
//  ToolbarButton.swift
//  CCC
//
//  Created by Mustafa Ozhan on 18/03/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import SwiftUI
import Res

struct ToolbarButton: View {
    @Environment(\.colorScheme) var colorScheme

    var clickEvent: () -> Void
    var imgName: String

    var body: some View {
        Button(
            action: clickEvent,
            label: {
                Image(systemName: imgName)
                    .resize(widthAndHeight: 24.cp())
                    .accentColor(Res.colors().text.get())
                    .padding(.leading, 10.cp())
            }
        ).padding(.trailing, 10.cp())
    }
}
