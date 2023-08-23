//
//  CalculatorRootView.swift
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

struct CalculatorRootView: View {
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
            CalculatorView(
                event: observable.event,
                state: observable.state,
                shouldShowBannerAd: observable.viewModel.shouldShowBannerAd()
            )
        }
        .navigationViewStyle(StackNavigationViewStyle())
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
                    navigationStack.push(CurrenciesRootView(onBaseChange: { observable.event.onBaseChange(base: $0) }))
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
            if CalculatorRootView.conversionText != nil && CalculatorRootView.conversionCode != nil {
                SnackView(
                    text: CalculatorRootView.conversionText!,
                    iconName: CalculatorRootView.conversionCode!
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

    private func onEffect(effect: CalculatorEffect) {
        logger.i(message: { "CalculatorRootView onEffect \(effect.description)" })
        switch effect {
        case is CalculatorEffect.Error:
            isGenericErrorSnackShown.toggle()
        case is CalculatorEffect.FewCurrency:
            isFewCurrencySnackShown.toggle()
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
            CalculatorRootView.conversionText = showConversionEffect.text
            CalculatorRootView.conversionCode = showConversionEffect.code
            isConversionSnackShown.toggle()
        default:
            logger.i(message: { "CalculatorRootView unknown effect" })
        }
    }
}
