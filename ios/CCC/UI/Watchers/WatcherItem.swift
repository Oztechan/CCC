//
//  WatcherItem.swift
//  CCC
//
//  Created by Mustafa Ozhan on 05.05.22.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import Client
import Resources
import NavigationStack
import Combine

struct WatcherItem: View {
    @State private var relationSelection = 0
    @State private var amount = ""

    @Binding var isBaseBarShown: Bool
    @Binding var isTargetBarShown: Bool

    let watcher: Client.Watcher
    let event: WatchersEvent

    var body: some View {
        HStack {
            Text(MR.strings().one.get()).font(.body)

            CurrencyImageView(imageName: watcher.base)
                .onTapGesture { event.onBaseClick(watcher: watcher) }

            Picker("", selection: $relationSelection) {
                Text(MR.strings().txt_smaller.get())
                    .font(.title)
                    .tag(0)
                Text(MR.strings().txt_grater.get())
                    .font(.title)
                    .tag(1)
            }
            .pickerStyle(.segmented)
            .frame(maxWidth: 80)
            .onChange(of: relationSelection) {
                event.onRelationChange(watcher: watcher, isGreater: $0 == 1)
            }

            Spacer()

            TextField(MR.strings().txt_rate.get(), text: $amount)
                .keyboardType(.decimalPad)
                .font(.body)
                .multilineTextAlignment(TextAlignment.center)
                .fixedSize()
                .lineLimit(1)
                .padding(EdgeInsets(top: 5, leading: 15, bottom: 5, trailing: 15))
                .background(MR.colors().background_weak.get())
                .cornerRadius(7)
                .onChange(of: amount) {
                    amount = event.onRateChange(watcher: watcher, rate: $0)
                }

            Spacer()

            CurrencyImageView(imageName: watcher.target)
                .onTapGesture { event.onTargetClick(watcher: watcher) }

            Image(systemName: "trash")
                .padding(.leading, 10)
                .onTapGesture { event.onDeleteClick(watcher: watcher) }

        }.onAppear {
            relationSelection = watcher.isGreater ? 1 : 0
            amount = "\(watcher.rate)"
        }
    }
}
