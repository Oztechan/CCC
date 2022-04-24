//
//  CurrencyToolbarView.swift
//  CCC
//
//  Created by Mustafa Ozhan on 23.04.22.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import Resources

struct CurrenciesToolbarView: View {
    var firstRun: Bool
    var onBackClick: () -> Void
    var onQueryChange: (String) -> Void

    @State var query = ""
    @State var searchVisibilty = false

    var body: some View {
        HStack {

            if !firstRun {
                ToolbarButton(clickEvent: onBackClick, imgName: "chevron.left")
            }

            if searchVisibilty {
                Spacer()

                TextField(MR.strings().search.get(), text: $query)
                .onChange(of: query) { onQueryChange($0) }
                .background(
                    RoundedRectangle(cornerRadius: 3)
                        .fill(MR.colors().background.get())
                        .padding(.bottom, -4)
                        .padding(.top, -4)
                )
                .disableAutocorrection(true)
                .multilineTextAlignment(.center)
                .padding(.all, 4)

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

                Text(MR.strings().txt_currencies.get()).font(.title3)

                Spacer()

                ToolbarButton(
                    clickEvent: { searchVisibilty.toggle() },
                    imgName: "magnifyingglass"
                )
            }

        }.padding(EdgeInsets(top: 20, leading: 10, bottom: 5, trailing: 20))
    }
}
