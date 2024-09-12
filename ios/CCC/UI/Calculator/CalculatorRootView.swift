//
//  CalculatorRootView.swift
//  CCC
//
//  Created by Mustafa Ozhan on 16/11/2020.
//  Copyright © 2020 orgName. All rights reserved.
//

import NavigationStack
import Provider
import SwiftUI

struct CalculatorRootView: View {
    @StateObject var observable = ObservableSEEDViewModel<
        CalculatorState,
        CalculatorEffect,
        CalculatorEvent,
        CalculatorData,
        CalculatorViewModel
    >()
    @Environment(\.colorScheme) private var colorScheme
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
        CalculatorView(
            event: observable.event,
            state: observable.state
        )
        .snack(isPresented: $isTooBigInputSnackShown) {
            SnackView(text: String(\.text_too_big_input))
        }
        .snack(isPresented: $isTooBigOutputSnackShown) {
            SnackView(text: String(\.text_too_big_output))
        }
        .snack(isPresented: $isPasteRequestSnackShown) {
            SnackView(
                text: String(\.text_paste_request),
                buttonText: String(\.text_paste),
                buttonAction: {
                    observable.event.onPasteToInput(text: UIPasteboard.general.string ?? "")
                }
            )
        }
        .snack(isPresented: $isGenericErrorSnackShown) {
            SnackView(text: String(\.error_text_unknown))
        }
        .snack(isPresented: $isFewCurrencySnackShown) {
            SnackView(
                text: String(\.choose_at_least_two_currency),
                buttonText: String(\.select),
                buttonAction: {
                    navigationStack.push(CurrenciesRootView())
                }
            )
        }
        .snack(isPresented: $isCopyClipboardSnackShown) {
            SnackView(text: String(\.copied_to_clipboard))
        }
        .snack(isPresented: $isConversionSnackShown) {
            if CalculatorRootView.conversionText != nil && CalculatorRootView.conversionCode != nil {
                SnackView(
                    text: CalculatorRootView.conversionText!,
                    iconName: CalculatorRootView.conversionCode!
                )
            }
        }
        .sheet(
            isPresented: $isBarShown,
            onDismiss: { observable.event.onSheetDismissed() },
            content: {
                SelectCurrencyRootView(
                    isBarShown: $isBarShown,
                    purpose: .base
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
            navigationStack.push(SettingsRootView())
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
