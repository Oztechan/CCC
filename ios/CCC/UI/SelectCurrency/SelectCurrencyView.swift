//
//  SelectCurrencyView.swift
//  CCC
//
//  Created by Mustafa Ozhan on 24.08.23.
//  Copyright © 2023 orgName. All rights reserved.
//

import Provider
import Res
import SwiftUI

struct SelectCurrencyView: View {
    @Environment(\.colorScheme) private var colorScheme

    var event: SelectCurrencyEvent
    var state: SelectCurrencyState

    var body: some View {
        ZStack {
            Color(Res.colors().background_strong.get()).edgesIgnoringSafeArea(.all)

            VStack {
                Text(Res.strings().txt_select_base_currency.get())
                    .font(relative: .title2)
                    .padding(4.cp())
                    .padding(.top, 10.cp())

                if state.loading {
                    FormProgressView()
                } else {
                    Form {
                        List(state.currencyList, id: \.code) { currency in
                            SelectCurrencyItemView(item: currency)
                                .onTapGesture { event.onItemClick(currency: currency) }
                                .frame(minWidth: 0, maxWidth: .infinity, alignment: .center)
                        }
                        .listRowInsets(.init())
                        .listRowBackground(Res.colors().background.get())
                    }
                    .withClearBackground(color: Res.colors().background.get())
                }

                Spacer()

                SelectCurrenciesBottomView(
                    text: state.enoughCurrency ?
                    Res.strings().txt_update_favorite_currencies.get() :
                        Res.strings().choose_at_least_two_currency.get(),
                    buttonText: state.enoughCurrency ?
                    Res.strings().update.get() :
                        Res.strings().select.get(),
                    onButtonClick: event.onSelectClick
                ).listRowBackground(Res.colors().background.get())
            }.navigationBarHidden(true)
        }
    }
}
