//
//  WatcherItem.swift
//  CCC
//
//  Created by Mustafa Ozhan on 05.05.22.
//  Copyright © 2022 orgName. All rights reserved.
//

import Combine
import NavigationStack
import Provider
import Res
import SwiftUI

struct WatcherItem: View {
    @Environment(\.colorScheme) private var colorScheme
    @State private var relationSelection = 0
    @State private var amount = ""

    @Binding var isBaseBarShown: Bool
    @Binding var isTargetBarShown: Bool

    let watcher: Provider.Watcher
    let event: WatchersEvent

    var body: some View {
        HStack {
            Text(String(\.one)).font(relative: .body)

            CurrencyImageView(imageName: watcher.base)
                .onTapGesture { event.onBaseClick(watcher: watcher) }

            Picker("", selection: $relationSelection) {
                Text(String(\.txt_smaller))
                    .font(relative: .title)
                    .tag(0)
                Text(String(\.txt_grater))
                    .font(relative: .title)
                    .tag(1)
            }
            .pickerStyle(.segmented)
            .frame(maxWidth: 75.cp())
            .onChange(of: relationSelection) {
                event.onRelationChange(watcher: watcher, isGreater: $0 == 1)
            }

            Spacer()

            TextField(String(\.txt_rate), text: $amount)
                .keyboardType(.decimalPad)
                .font(relative: .body)
                .multilineTextAlignment(TextAlignment.center)
                .fixedSize()
                .lineLimit(1)
                .padding(top: 5.cp(), leading: 15.cp(), bottom: 5.cp(), trailing: 15.cp())
                .background(Res.colors().background_weak.get())
                .cornerRadius(7.cp())
                .onChange(of: amount) {
                    amount = event.onRateChange(watcher: watcher, rate: $0)
                }

            Spacer()

            CurrencyImageView(imageName: watcher.target)
                .onTapGesture { event.onTargetClick(watcher: watcher) }

            Image(systemName: "trash")
                .resize(widthAndHeight: 20.cp())
                .imageScale(.large)
                .padding(.leading, 10.cp())
                .onTapGesture { event.onDeleteClick(watcher: watcher) }
        }
        .padding(.vertical, 4.cp())
        .onAppear {
            relationSelection = watcher.isGreater ? 1 : 0
            amount = "\(watcher.rate)"
        }
    }
}
