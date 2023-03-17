//
//  MainView.swift
//  CCC
//
//  Created by Mustafa Ozhan on 16/11/2020.
//  Copyright © 2020 orgName. All rights reserved.
//

import NavigationStack
import PopupView
import Provider
import Res
import SwiftUI

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
    @State var isTooBigInputSnackShown = false
    @State var isTooBigOutputSnackShown = false
    @State var isGenericErrorSnackShown = false
    @State var isFewCurrencySnackShown = false
    @State var isCopyClipboardSnackShown = false
    @State var isPasteRequestSnackShown = false

    @State var isConversionSnackShown = false
    static var conversionText: String?
    static var conversionCode: String?

    private let analyticsManager: AnalyticsManager = koin.get()

    var body: some View {
        NavigationView {
            ZStack {
                Color(Res.colors().background_strong.get()).edgesIgnoringSafeArea(.all)

                VStack {
                    InputView(
                        input: observable.state.input,
                        onSettingsClick: observable.event.onSettingsClicked,
                        onInputLongClick: observable.event.onInputLongClick
                    )

                    OutputView(
                        baseCurrency: observable.state.base,
                        output: observable.state.output,
                        symbol: observable.state.symbol,
                        onBarClick: observable.event.onBarClick,
                        onOutputLongClick: observable.event.onOutputLongClick
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
                            .listRowBackground(Res.colors().background.get())
                            .animation(.default)
                        }
                        .withClearBackground(color: Res.colors().background.get())
                        .padding(bottom: 4.cp())
                    }

                    KeyboardView(onKeyPress: { observable.event.onKeyPress(key: $0) })

                    if !(observable.state.conversionState is ConversionState.None) {
                        ConversionStateView(
                            color: observable.state.conversionState.getColor(),
                            text: observable.state.conversionState.getText()
                        )
                    }

                    if observable.viewModel.shouldShowBannerAd() {
                        AdaptiveBannerAdView(unitID: "BANNER_AD_UNIT_ID_CALCULATOR").adapt()
                    }
                }
            }
            .background(Res.colors().background_strong.get())
            .navigationBarHidden(true)
        }
        .navigationViewStyle(StackNavigationViewStyle())
        .popup(
            isPresented: $isTooBigNumberSnackShown,
            type: .toast,
            autohideIn: 2.0
        ) {
            SnackView(text: Res.strings().text_too_big_number.get())
        }
        .popup(
            isPresented: $isTooBigInputSnackShown,
            type: .toast,
            autohideIn: 2.0
        ) {
            SnackView(text: Res.strings().text_too_big_input.get())
        }
        .popup(
            isPresented: $isTooBigOutputSnackShown,
            type: .toast,
            autohideIn: 2.0
        ) {
            SnackView(text: Res.strings().text_too_big_output.get())
        }
        .popup(isPresented: $isPasteRequestSnackShown,
               type: .toast,
               autohideIn: 2.0
        ) {
            SnackView(
                text: Res.strings().text_paste_request.get(),
                buttonText: Res.strings().text_paste.get(),
                buttonAction: {
                    observable.event.pasteToInput(text: UIPasteboard.general.string ?? "")
                }
            )
        }
        .popup(
            isPresented: $isGenericErrorSnackShown,
            type: .toast,
            autohideIn: 2.0
        ) {
            SnackView(text: Res.strings().error_text_unknown.get())
        }
        .popup(
            isPresented: $isFewCurrencySnackShown,
            type: .toast,
            autohideIn: 2.0
        ) {
            SnackView(
                text: Res.strings().choose_at_least_two_currency.get(),
                buttonText: Res.strings().select.get(),
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
            SnackView(text: Res.strings().copied_to_clipboard.get())
        }
        .popup(
            isPresented: $isConversionSnackShown,
            type: .toast,
            autohideIn: 2.0
        ) {
            if CalculatorView.conversionText != nil && CalculatorView.conversionCode != nil {
                SnackView(
                    text: CalculatorView.conversionText!,
                    iconName: CalculatorView.conversionCode!
                )
            }
        }
        .sheet(
            isPresented: $isBarShown,
            content: {
                SelectCurrencyView(
                    isBarShown: $isBarShown,
                    onCurrencySelected: { observable.event.onBaseChange(base: $0) }
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

    // swiftlint:disable:next cyclomatic_complexity
    private func onEffect(effect: CalculatorEffect) {
        logger.i(message: { "CalculatorView onEffect \(effect.description)" })
        switch effect {
        case is CalculatorEffect.Error:
            isGenericErrorSnackShown.toggle()
        case is CalculatorEffect.FewCurrency:
            isFewCurrencySnackShown.toggle()
        case is CalculatorEffect.TooBigNumber:
            isTooBigNumberSnackShown.toggle()
        case is CalculatorEffect.TooBigInput:
            isTooBigInputSnackShown.toggle()
        case is CalculatorEffect.TooBigOutput:
            isTooBigOutputSnackShown.toggle()
        case is CalculatorEffect.OpenBar:
            isBarShown = true
        case is CalculatorEffect.OpenSettings:
            navigationStack.push(SettingsView(onBaseChange: { observable.event.onBaseChange(base: $0) }))
        case is CalculatorEffect.ShowPasteRequest:
            isPasteRequestSnackShown.toggle()
        case let copyToClipboardEffect as CalculatorEffect.CopyToClipboard:
            let pasteBoard = UIPasteboard.general
            pasteBoard.string = copyToClipboardEffect.amount
            isCopyClipboardSnackShown.toggle()
        case let showConversionEffect as CalculatorEffect.ShowConversion:
            CalculatorView.conversionText = showConversionEffect.text
            CalculatorView.conversionCode = showConversionEffect.code
            isConversionSnackShown.toggle()
        default:
            logger.i(message: { "CalculatorView unknown effect" })
        }
    }
}
