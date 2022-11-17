//
//  WatchersToolbarView.swift
//  CCC
//
//  Created by Mustafa Ozhan on 26.04.22.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import Res

struct WatchersToolbarView: View {
    var backEvent: () -> Void

    var body: some View {
        VStack {
            HStack {
                ToolbarButton(clickEvent: backEvent, imgName: "chevron.left")

                Text(MR.strings().txt_watchers.get())
                    .font(relative: .title3)

                Spacer()
            }

            Text(MR.strings().txt_watchers_description.get())
                .contentShape(Rectangle())
                .font(relative: .caption)
                .multilineTextAlignment(.center)
                .background(MR.colors().background_strong.get())
                .foregroundColor(MR.colors().text_weak.get())
                .padding(10.cp())
        }
        .frame(width: .infinity, height: .nan)
        .padding(top: 15.cp(), leading: 10.cp(), bottom: 5.cp(), trailing: 20.cp())
        .background(MR.colors().background_strong.get())
    }
}
