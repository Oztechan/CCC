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
    @State private var relationSelection = 0
    @State private var amount = ""

    @Binding var isBaseBarShown: Bool
    @Binding var isTargetBarShown: Bool

    let notification: Client.Notification
    let event: NotificationEvent

    var body: some View {
        HStack {
            Text(MR.strings().one.get()).font(.body)

            CurrencyImageView(imageName: notification.base)
                .onTapGesture { event.onBaseClick(notification: notification) }

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
                event.onRelationChange(notification: notification, isGreater: $0 == 1)
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
                    amount = event.onRateChange(notification: notification, rate: $0)
                }

            Spacer()

            CurrencyImageView(imageName: notification.target)
                .onTapGesture { event.onTargetClick(notification: notification) }

            Image(systemName: "trash")
                .padding(.leading, 10)
                .onTapGesture { event.onDeleteClick(notification: notification) }

        }.onAppear {
            relationSelection = notification.isGreater ? 1 : 0
            amount = "\(notification.rate)"
        }
    }
}
