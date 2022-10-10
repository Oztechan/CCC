//
//  MainView.swift
//  CCC
//
//  Created by Mustafa Ozhan on 16/11/2020.
//  Copyright © 2020 orgName. All rights reserved.
//

import SwiftUI
import Res
import Client
import NavigationStack
import LogMob

typealias CalculatorObservable = ObservableSEED
<CalculatorViewModel, CalculatorState, CalculatorEffect, CalculatorEvent, CalculatorData>

struct CalculatorView: View {

    @Environment(\.colorScheme) var colorScheme
    @EnvironmentObject private var navigationStack: NavigationStackCompat
    @StateObject var observable = CalculatorObservable(viewModel: koin.get())

    @State var isBarShown = false

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
                        onBarClick: { fatalError("some error in SwiftUI") }
                    )

                    Form {
                        if observable.state.loading {
                            FormProgressView()
                        } else {
                            List(
                                CalculatorUtilKt.toValidList(
                                    observable.state.currencyList,
                                    currentBase: observable.state.base
                                ),
                                id: \.name
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
                    }.background(MR.colors().background.get())

                    KeyboardView(onKeyPress: { observable.event.onKeyPress(key: $0) })

                    if !(observable.state.rateState is RateState.None) {
                        RateStateView(
                            color: observable.state.rateState.getColor(),
                            text: observable.state.rateState.getText()
                        )
                    }

                    if observable.viewModel.shouldShowBannerAd() {
                        BannerAdView(unitID: "BANNER_AD_UNIT_ID_CALCULATOR".getSecretValue())
                            .frame(maxHeight: 50)
                            .padding(.bottom, 20)
                    }

                }
            }
            .navigationBarHidden(true)
        }
        .navigationViewStyle(StackNavigationViewStyle())
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
        LoggerKt.i(message: {"CalculatorView onEffect \(effect.description)"})
        switch effect {
        case is CalculatorEffect.Error:
            showSnack(text: MR.strings().error_text_unknown.get())
        case is CalculatorEffect.FewCurrency:
            showSnack(
                text: MR.strings().choose_at_least_two_currency.get(),
                buttonText: MR.strings().select.get(),
                action: {
                    navigationStack.push(CurrenciesView(onBaseChange: { observable.event.onBaseChange(base: $0) }))
                }
            )
        case is CalculatorEffect.TooBigNumber:
            showSnack(text: MR.strings().text_too_big_number.get())
        case is CalculatorEffect.OpenBar:
            isBarShown = true
        case is CalculatorEffect.OpenSettings:
            navigationStack.push(SettingsView(onBaseChange: { observable.event.onBaseChange(base: $0) }))
        // swiftlint:disable force_cast
        case is CalculatorEffect.CopyToClipboard:
            let pasteBoard = UIPasteboard.general
            pasteBoard.string = (effect as! CalculatorEffect.CopyToClipboard).amount
            showSnack(text: MR.strings().copied_to_clipboard.get())
        // swiftlint:disable force_cast
        case is CalculatorEffect.ShowRate:
            showSnack(
                text: (effect as! CalculatorEffect.ShowRate).text,
                iconImage: (effect as! CalculatorEffect.ShowRate).name.getImage()
            )
        default:
            LoggerKt.i(message: {"CalculatorView unknown effect"})
        }
    }
}
