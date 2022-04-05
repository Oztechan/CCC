//
//  MainView.swift
//  CCC
//
//  Created by Mustafa Ozhan on 16/11/2020.
//  Copyright © 2020 orgName. All rights reserved.
//

import SwiftUI
import Client
import NavigationStack

typealias CalculatorObservable = ObservableSEED
<CalculatorViewModel, CalculatorState, CalculatorEffect, CalculatorEvent, CalculatorData>

struct CalculatorView: View {

    @Environment(\.colorScheme) var colorScheme
    @EnvironmentObject private var navigationStack: NavigationStack
    @StateObject var observable: CalculatorObservable = koin.get()

    @State var isBarShown = false

    var body: some View {
        NavigationView {
            ZStack {
                Color(R.colors().background_strong.get()).edgesIgnoringSafeArea(.all)

                VStack {

                    CalculationInputView(
                        input: observable.state.input,
                        onSettingsClick: observable.event.onSettingsClicked
                    )

                    CalculationOutputView(
                        baseCurrency: observable.state.base,
                        output: observable.state.output,
                        symbol: observable.state.symbol,
                        onBarClick: { observable.event.onBarClick() }
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
                                id: \.rate
                            ) {
                                CalculatorItemView(
                                    item: $0,
                                    onItemClick: { observable.event.onItemClick(currency: $0) },
                                    onItemImageLongClick: { observable.event.onItemImageLongClick(currency: $0) },
                                    onItemAmountLongClick: { observable.event.onItemAmountLongClick(amount: $0) }
                                )
                            }
                            .listRowInsets(.init())
                            .listRowBackground(R.colors().background.get())
                            .animation(.default)
                        }
                    }.background(R.colors().background.get())

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
        .sheet(
            isPresented: $isBarShown,
            content: {
                ChangeBaseView(
                    isBarShown: $isBarShown,
                    onBaseChange: { observable.event.onBaseChange(base: $0)}
                ).environmentObject(navigationStack)
            }
        )
        .onAppear { observable.startObserving() }
        .onDisappear { observable.stopObserving() }
        .onReceive(observable.effect) { onEffect(effect: $0) }
    }

    private func onEffect(effect: CalculatorEffect) {
        logger.i(message: {effect.description})
        switch effect {
        case is CalculatorEffect.Error:
            showSnack(text: R.strings().error_text_unknown.get())
        case is CalculatorEffect.FewCurrency:
            showSnack(
                text: R.strings().choose_at_least_two_currency.get(),
                buttonText: R.strings().select.get(),
                action: {
                    navigationStack.push(CurrenciesView(onBaseChange: { observable.event.onBaseChange(base: $0) }))
                }
            )
        case is CalculatorEffect.MaximumInput:
            showSnack(text: R.strings().max_input.get())
        case is CalculatorEffect.OpenBar:
            isBarShown = true
        case is CalculatorEffect.OpenSettings:
            navigationStack.push(SettingsView(onBaseChange: { observable.event.onBaseChange(base: $0) }))
        // swiftlint:disable force_cast
        case is CalculatorEffect.CopyToClipboard:
            let pasteBoard = UIPasteboard.general
            pasteBoard.string = (effect as! CalculatorEffect.CopyToClipboard).amount
            showSnack(text: R.strings().copied_to_clipboard.get())
        // swiftlint:disable force_cast
        case is CalculatorEffect.ShowRate:
            showSnack(
                text: (effect as! CalculatorEffect.ShowRate).text,
                iconImage: (effect as! CalculatorEffect.ShowRate).name.getImage()
            )
        default:
            logger.i(message: {"CalculatorView unknown effect"})
        }
    }
}

struct CalculationInputView: View {
    @Environment(\.colorScheme) var colorScheme

    var input: String
    var onSettingsClick: () -> Void

    var body: some View {
        HStack {

            Spacer()

            Text(input)
                .multilineTextAlignment(.center)
                .lineLimit(2)
                .fixedSize(horizontal: false, vertical: true)
                .foregroundColor(R.colors().text.get())
                .font(.title2)

            Spacer()

            ToolbarButton(
                clickEvent: onSettingsClick,
                imgName: "gear"
            ).padding(.trailing, 5)

        }
        .frame(width: .none, height: 36, alignment: .center)
        .padding(.top, 4)
    }
}

struct CalculationOutputView: View {

    var baseCurrency: String
    var output: String
    var symbol: String
    var onBarClick: () -> Void

    var body: some View {
        VStack(alignment: .leading) {

            HStack {
                if baseCurrency != "" {
                    Image(uiImage: baseCurrency.getImage())
                        .resizable()
                        .frame(width: 36, height: 36, alignment: .center)
                        .shadow(radius: 3)
                }

                Text(baseCurrency).foregroundColor(R.colors().text.get())

                if !output.isEmpty {
                    Text("=  \(output)").foregroundColor(R.colors().text.get())
                }

                Text(symbol).foregroundColor(R.colors().text.get())
            }
            .frame(minWidth: 0, maxWidth: .infinity, alignment: .bottomLeading)
            .padding(EdgeInsets(top: 0, leading: 20, bottom: 0, trailing: 20))

        }
        .contentShape(Rectangle())
        .lineLimit(1)
        .onTapGesture { onBarClick() }
    }
}

struct KeyboardView: View {
    var onKeyPress: (String) -> Void

    // swiftlint:disable line_length
    let keys = [
        [R.strings().seven.get(), R.strings().eight.get(), R.strings().nine.get(), R.strings().multiply.get()],
        [R.strings().four.get(), R.strings().five.get(), R.strings().six.get(), R.strings().divide.get()],
        [R.strings().one.get(), R.strings().two.get(), R.strings().three.get(), R.strings().minus.get()],
        [R.strings().dot.get(), R.strings().zero.get(), R.strings().percent.get(), R.strings().plus.get()],
        [R.strings().open_parentheses.get(), R.strings().triple_zero.get(), R.strings().ac.get(), R.strings().delete_.get(), R.strings().close_parentheses.get()]
    ]

    var body: some View {

        VStack(alignment: .center) {
            ForEach(keys, id: \.self) { items in

                HStack(alignment: .center) {
                    ForEach(items, id: \.self) { item in

                        Button(
                            action: { onKeyPress(item)},
                            label: {
                                Text(item)
                                    .font(.title2)
                                    .foregroundColor(R.colors().text.get())
                                    .frame(minWidth: 0, maxWidth: .infinity, minHeight: 0, maxHeight: .infinity)
                            }
                        )

                    }
                }

            }
        }.background(R.colors().background_strong.get())
    }
}

struct RateStateView: View {
    var color: Color
    var text: String

    var body: some View {
        HStack {
            Circle()
                .frame(width: 12, height: 12, alignment: .center)
                .foregroundColor(color)
            Text(text).font(.caption)
        }.padding(.bottom, 5)
    }
}

struct CalculatorItemView: View {

    var item: Currency
    var onItemClick: (Currency) -> Void
    var onItemImageLongClick: (Currency) -> Void
    var onItemAmountLongClick: (String) -> Void

    var body: some View {
        HStack {

            Text(IOSCalculatorUtilKt.getFormatted(item.rate))
                .foregroundColor(R.colors().text.get())
                .onLongPressGesture { onItemAmountLongClick(IOSCalculatorUtilKt.getFormatted(item.rate)) }
            Text(item.symbol).foregroundColor(R.colors().text.get())
            Spacer()
            Text(item.name).foregroundColor(R.colors().text.get())
            Image(uiImage: item.name.getImage())
                .resizable()
                .frame(width: 36, height: 36, alignment: .center)
                .shadow(radius: 3)
                .onLongPressGesture { onItemImageLongClick(item) }

        }
        .contentShape(Rectangle())
        .onTapGesture { onItemClick(item) }
    }
}
