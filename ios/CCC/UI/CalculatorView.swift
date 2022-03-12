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
                Color(MR.colors().background_strong.get()).edgesIgnoringSafeArea(.all)

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
                                    onItemLongClick: { observable.event.onItemLongClick(currency: $0) }
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
            showSnack(text: MR.strings().error_text_unknown.get())
        case is CalculatorEffect.FewCurrency:
            showSnack(
                text: MR.strings().choose_at_least_two_currency.get(),
                buttonText: MR.strings().select.get(),
                action: {
                    navigationStack.push(CurrenciesView(onBaseChange: { observable.event.onBaseChange(base: $0) }))
                }
            )
        case is CalculatorEffect.MaximumInput:
            showSnack(text: MR.strings().max_input.get())
        case is CalculatorEffect.OpenBar:
            isBarShown = true
        case is CalculatorEffect.OpenSettings:
            navigationStack.push(SettingsView(onBaseChange: { observable.event.onBaseChange(base: $0) }))
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
                .foregroundColor(MR.colors().text.get())
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
                Image(uiImage: baseCurrency.getImage())
                    .resizable()
                    .frame(width: 36, height: 36, alignment: .center)
                    .shadow(radius: 3)
                Text(baseCurrency).foregroundColor(MR.colors().text.get())

                if !output.isEmpty {
                    Text("=  \(output)").foregroundColor(MR.colors().text.get())
                }

                Text(symbol).foregroundColor(MR.colors().text.get())
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
        [MR.strings().seven.get(), MR.strings().eight.get(), MR.strings().nine.get(), MR.strings().multiply.get()],
        [MR.strings().four.get(), MR.strings().five.get(), MR.strings().six.get(), MR.strings().divide.get()],
        [MR.strings().one.get(), MR.strings().two.get(), MR.strings().three.get(), MR.strings().minus.get()],
        [MR.strings().dot.get(), MR.strings().zero.get(), MR.strings().percent.get(), MR.strings().plus.get()],
        [MR.strings().open_parentheses.get(), MR.strings().triple_zero.get(), MR.strings().ac.get(), MR.strings().delete_.get(), MR.strings().close_parentheses.get()]
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
                                    .foregroundColor(MR.colors().text.get())
                                    .frame(minWidth: 0, maxWidth: .infinity, minHeight: 0, maxHeight: .infinity)
                            }
                        )

                    }
                }

            }
        }.background(MR.colors().background_strong.get())
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
    var onItemLongClick: (Currency) -> Void

    var body: some View {
        HStack {

            Text(IOSCalculatorUtilKt.getFormatted(item.rate))
                .foregroundColor(MR.colors().text.get())
            Text(item.symbol).foregroundColor(MR.colors().text.get())
            Spacer()
            Text(item.name).foregroundColor(MR.colors().text.get())
            Image(uiImage: item.name.getImage())
                .resizable()
                .frame(width: 36, height: 36, alignment: .center)
                .shadow(radius: 3)

        }
        .contentShape(Rectangle())
        .onTapGesture { onItemClick(item) }
        .onLongPressGesture { onItemLongClick(item) }
    }
}
