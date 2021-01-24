//
//  BarView.swift
//  ios
//
//  Created by Mustafa Ozhan on 23/01/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import SwiftUI
import client

struct BarView: View {

    @Environment(\.colorScheme) var colorScheme
    @ObservedObject var vmWrapper: BarVMWrapper = Koin.shared.barVMWrapper
    @Binding var isBarShown: Bool

    init(isBarShown: Binding<Bool>) {
        self._isBarShown = isBarShown
        LoggerKt.kermit.d(withMessage: {"BarView init"})
    }

    var body: some View {

        NavigationView {

            ZStack {

                Color(MR.colors().background_strong.get()).edgesIgnoringSafeArea(.all)

                Form {
                    if vmWrapper.state.loading {
                        HStack {
                            Spacer()
                            ProgressView().transition(.slide)
                            Spacer()
                        }
                        .listRowBackground(MR.colors().background.get())
                    } else {

                        List(vmWrapper.state.currencyList, id: \.name) { currency in

                            BarItemView(item: currency)
                                .onTapGesture { vmWrapper.event.onItemClick(currency: currency) }
                                .frame(minWidth: 0, maxWidth: .infinity, alignment: .center)

                        }.listRowBackground(MR.colors().background.get())
                    }
                }
                .background(MR.colors().background.get())
                .navigationBarTitle(MR.strings().txt_current_base.get())

            }
        }
        .onAppear { vmWrapper.startObserving() }
        .onReceive(vmWrapper.effect) { onEffect(effect: $0) }
        .onDisappear { vmWrapper.stopObserving() }
    }

    private func onEffect(effect: BarEffect) {
        switch effect {
        case is BarEffect.OpenCurrencies:
            isBarShown = false
        default:
            LoggerKt.kermit.d(withMessage: {"unknown effect"})
        }
    }
}

struct BarItemView: View {
    var item: Currency

    var body: some View {
        HStack {

            Image(uiImage: item.name.getImage())
                .resizable()
                .frame(width: 36, height: 36, alignment: .center)
                .shadow(radius: 3)
            Text(item.name)
                .frame(width: 45)
                .foregroundColor(MR.colors().text.get())
            Text(item.longName)
                .font(.footnote)
                .foregroundColor(MR.colors().text.get())
            Text(item.symbol)
                .font(.footnote)
                .foregroundColor(MR.colors().text.get())
            Spacer()

        }
        .contentShape(Rectangle())
        .lineLimit(1)
    }
}
