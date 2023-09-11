//
//  WatchersToolbarView.swift
//  CCC
//
//  Created by Mustafa Ozhan on 26.04.22.
//  Copyright © 2022 orgName. All rights reserved.
//

import Res
import SwiftUI

struct WatchersToolbarView: View {
    @Environment(\.colorScheme) private var colorScheme
    var backEvent: () -> Void

    var body: some View {
        VStack {
            HStack {
                ToolbarButton(clickEvent: backEvent, imgName: "chevron.left")

                Text(String(\.txt_watchers))
                    .font(relative: .title3)

                Spacer()
            }

            Text(String(\.txt_watchers_description))
                .contentShape(Rectangle())
                .font(relative: .caption)
                .multilineTextAlignment(.center)
                .background(Res.colors().background_strong.get())
                .foregroundColor(Res.colors().text_weak.get())
                .padding(10.cp())
        }
        .frame(width: .infinity, height: .nan)
        .padding(top: 15.cp(), leading: 10.cp(), trailing: 20.cp())
        .background(Res.colors().background_strong.get())
    }
}
