//
//  SettingsItemView.swift
//  CCC
//
//  Created by Mustafa Ozhan on 23.04.22.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI

struct SettingsItemView: View {
    @Environment(\.colorScheme) private var colorScheme
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
                .accentColor(\.text)
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
                .accentColor(\.text)
        }
        .listRowInsets(.init())
        .listRowBackground(\.background)
        .contentShape(Rectangle())
        .onTapGesture { onClick() }
        .lineLimit(1)
    }
}
