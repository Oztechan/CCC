//
//  NotificationsToolbarView.swift
//  CCC
//
//  Created by Mustafa Ozhan on 26.04.22.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import Resources

struct NotificationsToolbarView: View {
    var backEvent: () -> Void

    var body: some View {
        HStack {
            ToolbarButton(clickEvent: backEvent, imgName: "chevron.left")

            Text(MR.strings().txt_notifications.get())
                .font(.title3)

            Spacer()
        }
        .frame(width: .infinity, height: 36)
        .padding(EdgeInsets(top: 15, leading: 10, bottom: 6, trailing: 20))
    }
}
