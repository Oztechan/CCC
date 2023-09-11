//
//  CurrenciesView.swift
//  CCC
//
//  Created by Mustafa Ozhan on 21.08.23.
//  Copyright © 2023 orgName. All rights reserved.
//

import Provider
import Res
import SwiftUI

struct CurrenciesView: View {
    @Environment(\.colorScheme) private var colorScheme

    var event: CurrenciesEvent
    var state: CurrenciesState

    var body: some View {
        ZStack {
            Res.colors().background_strong.get().edgesIgnoringSafeArea(.all)

            VStack {
                if state.selectionVisibility {
                    SelectionView(
                        onCloseClick: event.onCloseClick,
                        updateAllCurrenciesState: { event.updateAllCurrenciesState(state: $0) }
                    )
                } else {
                    CurrenciesToolbarView(
                        isOnboardingVisible: state.isOnboardingVisible,
                        onBackClick: event.onCloseClick,
                        onQueryChange: { event.onQueryChange(query: $0) }
                    )
                }

                if state.loading {
                    FormProgressView()
                } else {
                    Form {
                        List(state.currencyList, id: \.code) { currency in
                            CurrenciesItemView(
                                item: currency,
                                onItemClick: { event.onItemClick(currency: currency) },
                                onItemLongClick: event.onItemLongClick
                            )
                        }
                        .listRowInsets(.init())
                        .id(UUID())
                        .listRowBackground(Res.colors().background.get())
                    }
                    .withClearBackground(color: Res.colors().background.get())
                }

                if state.isOnboardingVisible {
                    SelectCurrenciesBottomView(
                        text: String(\.txt_select_currencies),
                        buttonText: String(\.btn_done),
                        onButtonClick: event.onDoneClick
                    )
                }

                if state.isBannerAdVisible {
                    AdaptiveBannerAdView(unitID: "BANNER_AD_UNIT_ID_CURRENCIES").adapt()
                }
            }
            .animation(.default)
            .navigationBarHidden(true)
        }
    }
}
