//
//  CurrencyToolbarView.swift
//  CCC
//
//  Created by Mustafa Ozhan on 23.04.22.
//  Copyright © 2022 orgName. All rights reserved.
//

import Res
import SwiftUI

struct CurrenciesToolbarView: View {
    var firstRun: Bool
    var onBackClick: () -> Void
    var onQueryChange: (String) -> Void

    @State var query = ""
    @State var searchVisibilty = false

    var body: some View {
        HStack {
            if firstRun {
                Text("").padding(trailing: 8.cp())
            } else {
                ToolbarButton(clickEvent: onBackClick, imgName: "chevron.left")
            }

            if searchVisibilty {
                Spacer()

                TextField(Res.strings().search.get(), text: $query)
                    .font(relative: .headline)
                    .onChange(of: query) { onQueryChange($0) }
                    .padding(8.cp())
                    .background(
                        RoundedRectangle(cornerRadius: 3.cp())
                            .fill(Res.colors().background.get())
                    )
                    .disableAutocorrection(true)
                    .multilineTextAlignment(.center)
                    .frame(height: 24.cp())

                Spacer()

                ToolbarButton(
                    clickEvent: {
                        query = ""
                        onQueryChange("")
                        searchVisibilty.toggle()
                    },
                    imgName: "xmark"
                )
            } else {
                Text(Res.strings().txt_currencies.get()).font(relative: .title3)

                Spacer()

                ToolbarButton(
                    clickEvent: { searchVisibilty.toggle() },
                    imgName: "magnifyingglass"
                )
            }
        }.padding(top: 20.cp(), leading: 10.cp(), bottom: 10.cp(), trailing: 20.cp())
    }
}
