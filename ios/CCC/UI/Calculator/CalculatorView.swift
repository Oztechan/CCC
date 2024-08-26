//
//  CalculatorView.swift
//  CCC
//
//  Created by Mustafa Ozhan on 21.08.23.
//  Copyright © 2023 orgName. All rights reserved.
//

import Provider
import SwiftUI

struct CalculatorView: View {
    @Environment(\.colorScheme) private var colorScheme

    var event: CalculatorEvent
    var state: CalculatorState

    var body: some View {
        ZStack {
            Color(\.background_strong).edgesIgnoringSafeArea(.all)

            VStack {
                InputView(
                    input: state.input,
                    onSettingsClick: event.onSettingsClicked,
                    onInputLongClick: event.onInputLongClick
                )

                OutputView(
                    baseCurrency: state.base,
                    output: state.output,
                    symbol: state.symbol,
                    onBarClick: event.onBarClick,
                    onOutputLongClick: event.onOutputLongClick
                )

                if state.loading {
                    FormProgressView()
                        .padding(bottom: 4.cp())
                } else {
                    Form {
                        List(
                            CalculatorUtilKt.toValidList(
                                state.currencyList,
                                currentBase: state.base
                            ),
                            id: \.code
                        ) {
                            CalculatorItemView(
                                item: $0,
                                onItemClick: { event.onItemClick(currency: $0) },
                                onItemImageLongClick: { event.onItemImageLongClick(currency: $0) },
                                onItemAmountLongClick: { event.onItemAmountLongClick(amount: $0) }
                            )
                        }
                        .listRowInsets(.init())
                        .listRowBackground(\.background)
                        .animation(.default, value: state)
                    }
                    .withClearBackground(color: Color(\.background))
                    .padding(bottom: 4.cp())
                }

                KeyboardView(onKeyPress: { event.onKeyPress(key: $0) })

                if !(state.conversionState is ConversionState.None) {
                    ConversionStateView(
                        color: state.conversionState.getColor(),
                        text: state.conversionState.getText()
                    )
                }

                if state.isBannerAdVisible {
                    AdaptiveBannerAdView(unitID: "BANNER_AD_UNIT_ID_CALCULATOR").adapt()
                }
            }
        }
        .navigationBarHidden(true)
        .background(\.background_strong)
    }
}
