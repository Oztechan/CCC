//
//  SelectCurrencyView.swift
//  CCC
//
//  Created by Mustafa Ozhan on 24.08.23.
//  Copyright © 2023 orgName. All rights reserved.
//

import Provider
import SwiftUI

struct SelectCurrencyView: View {
    @Environment(\.colorScheme) private var colorScheme

    var event: SelectCurrencyEvent
    var state: SelectCurrencyState
    var purpose: SelectCurrencyPurpose

    var body: some View {
        ZStack {
            Color(\.background_strong).edgesIgnoringSafeArea(.all)

            VStack {
                Text(String(\.txt_select_base_currency))
                    .font(relative: .title2)
                    .padding(4.cp())
                    .padding(.top, 10.cp())

                if state.loading {
                    FormProgressView()
                } else {
                    Form {
                        List(state.currencyList, id: \.code) { currency in
                            SelectCurrencyItemView(item: currency)
                                .onTapGesture { event.onItemClick(currency: currency, purpose: purpose) }
                                .frame(minWidth: 0, maxWidth: .infinity, alignment: .center)
                        }
                        .listRowInsets(.init())
                        .listRowBackground(\.background)
                    }
                    .withClearBackground(color: Color(\.background))
                }

                Spacer()

                SelectCurrenciesBottomView(
                    text: state.enoughCurrency ?
                    String(\.txt_update_favorite_currencies) :
                        String(\.choose_at_least_two_currency),
                    buttonText: state.enoughCurrency ? String(\.update) : String(\.select),
                    onButtonClick: event.onSelectClick
                ).listRowBackground(\.background)
            }.navigationBarHidden(true)
        }
    }
}
