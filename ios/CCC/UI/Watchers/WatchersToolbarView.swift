//
//  WatchersToolbarView.swift
//  CCC
//
//  Created by Mustafa Ozhan on 26.04.22.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import Resources

struct WatchersToolbarView: View {
    var backEvent: () -> Void

    var body: some View {
        VStack {
            HStack {
                ToolbarButton(clickEvent: backEvent, imgName: "chevron.left")

                Text(MR.strings().txt_watchers.get())
                    .font(.title3)

                Spacer()
            }

            Text(MR.strings().txt_txt_watchers_description.get())
                .contentShape(Rectangle())
                .font(.footnote)
                .multilineTextAlignment(.center)
                .background(MR.colors().background_strong.get())
                .foregroundColor(MR.colors().text_weak.get())
                .padding(.top, 10)
        }
        .frame(width: .infinity, height: .nan)
        .padding(EdgeInsets(top: 15, leading: 10, bottom: 5, trailing: 20))
        .background(MR.colors().background_strong.get())
    }
}
