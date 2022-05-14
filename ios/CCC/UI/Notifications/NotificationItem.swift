//
//  NotificationItem.swift
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

struct NotificationItem: View {
    @Binding var isBaseBarShown: Bool
    @Binding var isTargetBarShown: Bool

    let event: NotificationEvent
    let notification: Client.Notification

    @State private var relationSelection = 0
    @State var amount = ""

    var body: some View {
        HStack {
            Text(MR.strings().one.get())
                .font(.body)

            CurrencyImageView(imageName: notification.base)
                .onTapGesture { event.onBaseClick(notification: notification) }

            Spacer()

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
            .onChange(of: relationSelection) { relation in
                event.onRelationChange(notification: notification, isGreater: relation == 1)
            }

            Spacer()

            TextField(MR.strings().txt_rate.get(), text: $amount)
                .keyboardType(.decimalPad)
                .font(.body)
                .multilineTextAlignment(TextAlignment.center)
                .fixedSize()
                .lineLimit(1)
                .onChange(of: amount) { rate in
                    event.onRateChange(notification: notification, rate: rate)
                }

            Spacer()

            CurrencyImageView(imageName: notification.target)
                .onTapGesture { event.onTargetClick(notification: notification) }

            Spacer()

            Image(systemName: "trash")
                .onTapGesture { event.onDeleteClick(notification: notification) }

        }.onAppear {
            amount = "\(notification.rate)"
            if notification.isGreater {
                relationSelection = 1
            } else {
                relationSelection = 0
            }
        }
    }
}
