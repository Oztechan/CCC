//
//  SelectionView.swift
//  CCC
//
//  Created by Mustafa Ozhan on 23.04.22.
//  Copyright © 2022 orgName. All rights reserved.
//

import Res
import SwiftUI

struct SelectionView: View {
    var onCloseClick: () -> Void
    var updateAllCurrenciesState: (Bool) -> Void

    var body: some View {
        HStack {
            ToolbarButton(clickEvent: onCloseClick, imgName: "xmark")

            Spacer()

            Button(
                action: { updateAllCurrenciesState(true) },
                label: {
                    Text(Res.strings().btn_select_all.get())
                        .font(relative: .headline)
                        .foregroundColor(Res.colors().text.get())
                }
            ).padding(.trailing, 10.cp())

            Button(
                action: { updateAllCurrenciesState(false) },
                label: {
                    Text(Res.strings().btn_de_select_all.get())
                        .font(relative: .headline)
                        .foregroundColor(Res.colors().text.get())
                }
            )
        }
        .padding(top: 15.cp(), leading: 10.cp(), bottom: 15.cp(), trailing: 20.cp())
        .background(Res.colors().background_weak.get())
        .frame(maxHeight: 54.cp(), alignment: .bottom)
    }
}
