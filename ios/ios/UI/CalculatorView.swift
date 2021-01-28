//
//  MainView.swift
//  ios
//
//  Created by Mustafa Ozhan on 16/11/2020.
//  Copyright © 2020 orgName. All rights reserved.
//

import SwiftUI
import client

typealias CalculatorObservable = ObservableSEED
<CalculatorViewModel, CalculatorState, CalculatorEffect, CalculatorEvent, CalculatorData>

struct CalculatorView: View {

    @Environment(\.colorScheme) var colorScheme
    @StateObject var seed: CalculatorObservable = koin.get()

    @State var isBarShown = false
    @State var fewCurrencyAlert = false
    @State var maximumInputAlert = false
    @State var currenciesNavigationToggle = false

    init() {
        LoggerKt.kermit.d(withMessage: {"CalculatorView init"})

        UITableView.appearance().tableHeaderView = UIView(frame: CGRect(
            x: 0,
            y: 0,
            width: 0,
            height: Double.leastNonzeroMagnitude
        ))
        UITableView.appearance().backgroundColor = MR.colors().transparent.get()
    }

    var body: some View {
        NavigationView {
            ZStack {
                Color(MR.colors().background_strong.get()).edgesIgnoringSafeArea(.all)

                VStack {

                    CalculationInputView(input: seed.state.input)

                    CalculationOutputView(
                        baseCurrency: seed.state.base,
                        output: seed.state.output,
                        symbol: seed.state.symbol,
                        onBarClick: {seed.event.onBarClick()}
                    )

                    Form {
                        if seed.state.loading {
                            HStack {
                                Spacer()
                                ProgressView().transition(.slide)
                                Spacer()
                            }
                            .listRowBackground(MR.colors().background.get())
                        } else {

                            List(
                                ExtensionsKt.toValidList(seed.state.currencyList, currentBase: seed.state.base),
                                id: \.rate
                            ) {
                                CalculatorItemView(
                                    item: $0,
                                    onItemClick: { item in
                                        seed.event.onItemClick(
                                            currency: item,
                                            conversion: ExtensionsKt.toStandardDigits(
                                                IOSExtensionsKt.getFormatted(item.rate)
                                            )
                                        )
                                    }
                                )
                            }
                            .listRowBackground(MR.colors().background.get())
                        }
                    }.background(MR.colors().background.get())

                    KeyboardView(onKeyPress: { seed.event.onKeyPress(key: $0) })

                }

                NavigationLink(
                    destination: CurrenciesView(currenciesNavigationToggle: $currenciesNavigationToggle),
                    isActive: $currenciesNavigationToggle
                ) { }.hidden()

            }
            .navigationBarHidden(true)
        }
        .sheet(
            isPresented: $isBarShown,
            content: {
                BarView(
                    isBarShown: $isBarShown,
                    dismissEvent: { seed.viewModel.verifyCurrentBase() }
                )
            }
        )
        .alert(isPresented: $maximumInputAlert) {
            Alert(
                title: Text(MR.strings().max_input.get()),
                dismissButton: .default(Text(MR.strings().txt_ok.get()))
            )
        }
        .alert(isPresented: $fewCurrencyAlert) {
            Alert(
                title: Text(MR.strings().txt_select_currencies.get()),
                primaryButton: .default(Text(MR.strings().txt_ok.get())) {
                    currenciesNavigationToggle.toggle()
                },
                secondaryButton: .cancel()
            )
        }
        .onAppear {seed.startObserving()}
        .onReceive(seed.effect) { onEffect(effect: $0) }
    }

    private func onEffect(effect: CalculatorEffect) {
        LoggerKt.kermit.d(withMessage: {effect.description})
        switch effect {
        case is CalculatorEffect.OpenBar:
            isBarShown = true
        case is CalculatorEffect.MaximumInput:
            maximumInputAlert = true
        case is CalculatorEffect.FewCurrency:
            fewCurrencyAlert = true
        default:
            LoggerKt.kermit.d(withMessage: {"unknown effect"})
        }
    }
}

struct CalculationInputView: View {
    @Environment(\.colorScheme) var colorScheme
    @State var settingsNavigationToggle = false

    var input: String

    var body: some View {
        HStack {

            Spacer()
            Text(input)
                .foregroundColor(MR.colors().text.get())
                .font(.title2)
            Spacer()

            Image(systemName: "gear")
                .imageScale(.large)
                .accentColor(MR.colors().text.get())
                .padding(.trailing, 15)
                .onTapGesture { settingsNavigationToggle.toggle()}

            NavigationLink(
                destination: SettingsView(settingsNavigationToggle: $settingsNavigationToggle),
                isActive: $settingsNavigationToggle
            ) { }.hidden()

        }.frame(width: .none, height: 40, alignment: .center)
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
                    Text(output).foregroundColor(MR.colors().text.get())
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

    let keys = [
        [MR.strings().seven.get(), MR.strings().eight.get(), MR.strings().nine.get(), MR.strings().multiply.get()],
        [MR.strings().four.get(), MR.strings().five.get(), MR.strings().six.get(), MR.strings().divide.get()],
        [MR.strings().one.get(), MR.strings().two.get(), MR.strings().three.get(), MR.strings().minus.get()],
        [MR.strings().dot.get(), MR.strings().zero.get(), MR.strings().percent.get(), MR.strings().plus.get()],
        [
            MR.strings().open_parentheses.get(),
            MR.strings().triple_zero.get(),
            MR.strings().ac.get(),
            MR.strings().delete_.get(),
            MR.strings().close_parentheses.get()
        ]
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
                                    .frame(
                                        minWidth: 0,
                                        maxWidth: .infinity,
                                        minHeight: 0,
                                        maxHeight: .infinity
                                    )
                            }
                        )

                    }
                }

            }
        }.background(MR.colors().background_strong.get())
    }
}

struct CalculatorItemView: View {

    var item: Currency
    var onItemClick: (Currency) -> Void

    var body: some View {
        HStack {

            Text(String(item.rate)).foregroundColor(Color(MR.colors().text.get()))
            Text(item.symbol).foregroundColor(Color(MR.colors().text.get()))
            Spacer()
            Text(item.name).foregroundColor(Color(MR.colors().text.get()))
            Image(uiImage: item.name.getImage())
                .resizable()
                .frame(width: 36, height: 36, alignment: .center)
                .shadow(radius: 3)

        }
        .contentShape(Rectangle())
        .onTapGesture { onItemClick(item) }
    }
}
