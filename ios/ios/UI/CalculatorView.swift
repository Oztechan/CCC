//
//  MainView.swift
//  ios
//
//  Created by Mustafa Ozhan on 16/11/2020.
//  Copyright © 2020 orgName. All rights reserved.
//

import SwiftUI
import client
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

                    CalculationInputView(input: observable.state.input)

                    CalculationOutputView(
                        baseCurrency: observable.state.base,
                        output: observable.state.output,
                        symbol: observable.state.symbol,
                        onBarClick: {observable.event.onBarClick()}
                    )

                    Form {
                        if observable.state.loading {
                            FormProgressView()
                        } else {
                            List(
                                ExtensionsKt.toValidList(
                                    observable.state.currencyList,
                                    currentBase: observable.state.base
                                ),
                                id: \.rate
                            ) {
                                CalculatorItemView(
                                    item: $0,
                                    onItemClick: { item in
                                        observable.event.onItemClick(
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

                    KeyboardView(onKeyPress: { observable.event.onKeyPress(key: $0) })

                }
            }
            .navigationBarHidden(true)
        }
        .sheet(
            isPresented: $isBarShown,
            content: {
                BarView(
                    isBarShown: $isBarShown,
                    onDismiss: { observable.viewModel.verifyCurrentBase() }
                ).environmentObject(navigationStack)
            }
        )
        .onAppear {observable.startObserving()}
        .onReceive(observable.effect) { onEffect(effect: $0) }
    }

    private func onEffect(effect: CalculatorEffect) {
        LoggerKt.kermit.d(withMessage: {effect.description})
        switch effect {
        case is CalculatorEffect.OpenBar:
            isBarShown = true
        case is CalculatorEffect.MaximumInput:
            toast(text: MR.strings().max_input.get())
        case is CalculatorEffect.FewCurrency:
            toast(text: MR.strings().choose_at_least_two_currency.get())
        default:
            LoggerKt.kermit.d(withMessage: {"unknown effect"})
        }
    }
}

struct CalculationInputView: View {
    @EnvironmentObject private var navigationStack: NavigationStack
    @Environment(\.colorScheme) var colorScheme

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
                .onTapGesture { self.navigationStack.push(SettingsView()) }

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

struct CalculatorItemView: View {

    var item: Currency
    var onItemClick: (Currency) -> Void

    var body: some View {
        HStack {

            Text(String(item.rate)).foregroundColor(MR.colors().text.get())
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
    }
}
