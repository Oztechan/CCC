//
//  CurrencyToolbarView.swift
//  ios
//
//  Created by Mustafa Ozhan on 21/01/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import SwiftUI
import client

struct CurrencyToolbarView: View {

    @Environment(\.colorScheme) var colorScheme

    var firstRun: Bool
    var backClickEvent: () -> Void
    var updateAllEvent: (Bool) -> Void

    var body: some View {
        HStack {

            if !firstRun {
                Button(
                    action: backClickEvent,
                    label: {
                        Image(systemName: "chevron.left")
                            .imageScale(.large)
                            .accentColor(MR.colors().text.get())
                            .padding(.leading, 10)

                        Text(MR.strings().txt_back.get()).foregroundColor(MR.colors().text.get())
                    }
                ).padding(.trailing, 10)

            }

            Spacer()
            Button(
                action: { updateAllEvent(true) },
                label: { Text(MR.strings().btn_select_all.get()).foregroundColor(MR.colors().text.get()) }
            ).padding(.trailing, 10)
            Button(
                action: { updateAllEvent(false) },
                label: { Text(MR.strings().btn_de_select_all.get()).foregroundColor(MR.colors().text.get()) }
            )

        }.padding(EdgeInsets(top: 20, leading: 10, bottom: 5, trailing: 20))
    }
}

#if DEBUG
struct CurrencyToolbarViewPreview: PreviewProvider {
    static var previews: some View {
        CurrencyToolbarView(
            firstRun: false,
            backClickEvent: {},
            updateAllEvent: {_ in }
        ).makeForPreviewProvider()
    }
}
#endif
