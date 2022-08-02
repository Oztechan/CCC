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
                .frame(width: 48, height: 48, alignment: .center)
                .font(.system(size: 24))
                .imageScale(.large)
                .accentColor(MR.colors().text.get())
                .padding(EdgeInsets(top: 8, leading: 0, bottom: 8, trailing: 8))

            VStack {
                HStack {
                    Text(title).font(.title3)
                    Spacer()
                }

                Spacer()

                HStack {
                    Text(subTitle).font(.footnote)
                    Spacer()
                }
            }.frame(height: 30)

            Spacer()

            Text(value)
                .lineLimit(2)
                .multilineTextAlignment(.trailing)
                .font(.caption)

            Image(systemName: "chevron.right")
                .frame(width: 48, height: 48, alignment: .center)
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
