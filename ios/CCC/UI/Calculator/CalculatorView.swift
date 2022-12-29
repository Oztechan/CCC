//
//  MainView.swift
//  CCC
//
//  Created by Mustafa Ozhan on 16/11/2020.
//  Copyright © 2020 orgName. All rights reserved.
//

import SwiftUI
import Res
import Provider
import NavigationStack
import PopupView

struct CalculatorView: View {

    @StateObject var observable = ObservableSEEDViewModel<
        CalculatorState,
        CalculatorEffect,
        CalculatorEvent,
        CalculatorData,
        CalculatorViewModel
    >()
    @Environment(\.colorScheme) var colorScheme
    @EnvironmentObject private var navigationStack: NavigationStackCompat
    @State var isBarShown = false
    @State var isTooBigNumberSnackShown = false
    @State var isGenericErrorSnackShown = false
    @State var isFewCurrencySnackShown = false
    @State var isCopyClipboardSnackShown = false

    @State var isShowRatesSnackShown = false
    static var ratesText: String?
    static var ratesIcon: String?

    private let analyticsManager: AnalyticsManager = koin.get()

    var body: some View {
        NavigationView {
            ZStack {
                Color(MR.colors().background_strong.get()).edgesIgnoringSafeArea(.all)

                VStack {

                    InputView(
                        input: observable.state.input,
                        onSettingsClick: observable.event.onSettingsClicked
                    )

                    OutputView(
                        baseCurrency: observable.state.base,
                        output: observable.state.output,
                        symbol: observable.state.symbol,
                        onBarClick: { observable.event.onBarClick() }
                    )

                    if observable.state.loading {
                        FormProgressView()
                            .padding(bottom: 4.cp())
                    } else {
                        Form {
                            List(
                                CalculatorUtilKt.toValidList(
                                    observable.state.currencyList,
                                    currentBase: observable.state.base
                                ),
                                id: \.code
                            ) {
                                CalculatorItemView(
                                    item: $0,
                                    onItemClick: { observable.event.onItemClick(currency: $0) },
                                    onItemImageLongClick: { observable.event.onItemImageLongClick(currency: $0) },
                                    onItemAmountLongClick: { observable.event.onItemAmountLongClick(amount: $0) }
                                )
                            }
                            .listRowInsets(.init())
                            .listRowBackground(MR.colors().background.get())
                            .animation(.default)
                        }
                        .withClearBackground(color: MR.colors().background.get())
                        .padding(bottom: 4.cp())
                    }

                    KeyboardView(onKeyPress: { observable.event.onKeyPress(key: $0) })

                    if !(observable.state.rateState is RateState.None) {
                        RateStateView(
                            color: observable.state.rateState.getColor(),
                            text: observable.state.rateState.getText()
                        )
                    }

                    if observable.viewModel.shouldShowBannerAd() {
                        AdaptiveBannerAdView(unitID: "BANNER_AD_UNIT_ID_CALCULATOR").adapt()
                    }

                }
            }
            .background(MR.colors().background_strong.get())
            .navigationBarHidden(true)
        }
        .navigationViewStyle(StackNavigationViewStyle())
        .popup(
            isPresented: $isTooBigNumberSnackShown,
            type: .toast,
            autohideIn: 2.0
        ) {
            SnackView(text: MR.strings().text_too_big_number.get())
        }
        .popup(
            isPresented: $isGenericErrorSnackShown,
            type: .toast,
            autohideIn: 2.0
        ) {
            SnackView(text: MR.strings().error_text_unknown.get())
        }
        .popup(
            isPresented: $isFewCurrencySnackShown,
            type: .toast,
            autohideIn: 2.0
        ) {
            SnackView(
                text: MR.strings().choose_at_least_two_currency.get(),
                buttonText: MR.strings().select.get(),
                buttonAction: {
                    navigationStack.push(CurrenciesView(onBaseChange: { observable.event.onBaseChange(base: $0) }))
                }
            )
        }
        .popup(
            isPresented: $isCopyClipboardSnackShown,
            type: .toast,
            autohideIn: 2.0
        ) {
            SnackView(text: MR.strings().copied_to_clipboard.get())
        }
        .popup(
            isPresented: $isShowRatesSnackShown,
            type: .toast,
            autohideIn: 2.0
        ) {
            if CalculatorView.ratesText != nil && CalculatorView.ratesIcon != nil {
                SnackView(
                    text: CalculatorView.ratesText!,
                    iconName: CalculatorView.ratesIcon!
                )
            }
        }
        .sheet(
            isPresented: $isBarShown,
            content: {
                SelectCurrencyView(
                    isBarShown: $isBarShown,
                    onCurrencySelected: { observable.event.onBaseChange(base: $0)}
                ).environmentObject(navigationStack)
            }
        )
        .onAppear {
            observable.startObserving()
            analyticsManager.trackScreen(screenName: ScreenName.Calculator())
        }
        .onDisappear { observable.stopObserving() }
        .onReceive(observable.effect) { onEffect(effect: $0) }
    }

    private func onEffect(effect: CalculatorEffect) {
        logger.i(message: {"CalculatorView onEffect \(effect.description)"})
        switch effect {
        case is CalculatorEffect.Error:
            isGenericErrorSnackShown.toggle()
        case is CalculatorEffect.FewCurrency:
            isFewCurrencySnackShown.toggle()
        case is CalculatorEffect.TooBigNumber:
            isTooBigNumberSnackShown.toggle()
        case is CalculatorEffect.OpenBar:
            isBarShown = true
        case is CalculatorEffect.OpenSettings:
            navigationStack.push(SettingsView(onBaseChange: { observable.event.onBaseChange(base: $0) }))
        // swiftlint:disable force_cast
        case is CalculatorEffect.CopyToClipboard:
            let pasteBoard = UIPasteboard.general
            pasteBoard.string = (effect as! CalculatorEffect.CopyToClipboard).amount
            isCopyClipboardSnackShown.toggle()
        // swiftlint:disable force_cast
        case is CalculatorEffect.ShowRate:
            CalculatorView.ratesText = (effect as! CalculatorEffect.ShowRate).text
            CalculatorView.ratesIcon = (effect as! CalculatorEffect.ShowRate).code
            isShowRatesSnackShown.toggle()
        default:
            logger.i(message: {"CalculatorView unknown effect"})
        }
    }
}
