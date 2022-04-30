//
//  NotificationStateView.swift
//  CCC
//
//  Created by Mustafa Ozhan on 30.04.22.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import Resources

struct NotificationStateView: View {
    var isEnabled: Bool
    var onClick: () -> Void

    var body: some View {
        HStack {

            Text(
                isEnabled
                ? MR.strings().txt_notifications_enabled.get()
                : MR.strings().txt_notifications_disabled.get()
            )
            .font(.body)
            .foregroundColor(MR.colors().text.get())

            Spacer()
            Image(systemName: isEnabled ? "checkmark.circle.fill" : "circle")
                .foregroundColor(MR.colors().accent.get())

        }
        .padding(25)
        .contentShape(Rectangle())
        .onTapGesture { onClick() }
        .lineLimit(1)
    }
}
