//
//  SettingsItemView.swift
//  CCC
//
//  Created by Mustafa Ozhan on 23.04.22.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import Res

struct SettingsItemView: View {

    let imgName: String
    let title: String
    let subTitle: String
    let value: String
    let onClick: () -> Void

    var body: some View {
        HStack {
            Image(systemName: imgName)
                .frame(width: 48.cp(), height: 48.cp(), alignment: .center)
                .font(size: 24.cp())
                .imageScale(.large)
                .accentColor(MR.colors().text.get())
                .padding(top: 8.cp(), leading: 0, bottom: 8.cp(), trailing: 8.cp())

            VStack {
                HStack {
                    Text(title).font(relative: .title3)
                    Spacer()
                }

                Spacer()

                HStack {
                    Text(subTitle).font(relative: .footnote)
                    Spacer()
                }
            }.frame(height: 30.cp())

            Spacer()

            Text(value)
                .lineLimit(2)
                .multilineTextAlignment(.trailing)
                .font(relative: .caption)

            Image(systemName: "chevron.right")
                .frame(width: 48.cp(), height: 48.cp(), alignment: .center)
                .imageScale(.large)
                .accentColor(MR.colors().text.get())
        }
        .listRowInsets(.init())
        .listRowBackground(MR.colors().background.get())
        .contentShape(Rectangle())
        .onTapGesture { onClick() }
        .lineLimit(1)
    }
}
