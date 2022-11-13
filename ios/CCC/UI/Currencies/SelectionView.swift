//
//  SelectionView.swift
//  CCC
//
//  Created by Mustafa Ozhan on 23.04.22.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import Res

struct SelectionView: View {
    var onCloseClick: () -> Void
    var updateAllCurrenciesState: (Bool) -> Void

    var body: some View {
        HStack {

            ToolbarButton(clickEvent: onCloseClick, imgName: "xmark")

            Spacer()
            Button(
                action: { updateAllCurrenciesState(true) },
                label: { Text(MR.strings().btn_select_all.get()).foregroundColor(MR.colors().text.get()) }
            ).padding(.trailing, 10)
            Button(
                action: { updateAllCurrenciesState(false) },
                label: { Text(MR.strings().btn_de_select_all.get()).foregroundColor(MR.colors().text.get()) }
            )

        }
        .padding(top: 15, leading: 10, bottom: 15, trailing: 20)
        .background(MR.colors().background_weak.get())
        .frame(maxHeight: 50)
    }
}
