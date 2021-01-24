//
//  SettingsView.swift
//  ios
//
//  Created by Mustafa Ozhan on 23/01/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import SwiftUI
import client

struct SettingsView: View {
    @ObservedObject var vmWrapper: SettingsVMWrapper = Koin.shared.settingsVMWrapper
    @State var currenciesNavigationToogle = false
    @Binding var settingsNavvigationToogle: Bool

    init(settingsNavvigationToogle: Binding<Bool>) {
        self._settingsNavvigationToogle = settingsNavvigationToogle
        LoggerKt.kermit.d(withMessage: {"BarView init"})
    }

    var body: some View {

        ZStack {
            MR.colors().background_strong.get().edgesIgnoringSafeArea(.all)

            VStack {

                HStack {

                    Button(
                        action: { settingsNavvigationToogle.toggle() },
                        label: {
                            Image(systemName: "chevron.left")
                                .imageScale(.large)
                                .accentColor(MR.colors().text.get())
                                .padding(.leading, 20)
                        }
                    ).padding(.trailing, 10)

                    Text(MR.strings().txt_settings.get())
                        .font(.title2)

                    Spacer()
                }

                Form {
                    SettingsItemView(
                        imgName: "dollarsign.circle.fill",
                        title: MR.strings().txt_currencies.get(),
                        subTitle: MR.strings().txt_select_currencies.get(),
                        value: MR.plurals().settings_item_currencies_value.get(
                            quantitiy: vmWrapper.state.activeCurrencyCount
                        ),
                        onClick: { vmWrapper.event.onCurrenciesClick() }
                    ).listRowBackground(MR.colors().background.get())

                }.background(MR.colors().background.get())

                NavigationLink(
                    destination: CurrenciesView(currenciesNavigationToogle: $currenciesNavigationToogle),
                    isActive: $currenciesNavigationToogle
                ) { }.hidden()
            }
            .navigationBarHidden(true)
        }
        .onAppear { vmWrapper.startObserving() }
        .onReceive(vmWrapper.effect) { onEffect(effect: $0) }
        .onDisappear { vmWrapper.stopObserving() }
    }

    private func onEffect(effect: SettingsEffect) {
        switch effect {
        case is SettingsEffect.OpenCurrencies:
            currenciesNavigationToogle.toggle()
        default:
            LoggerKt.kermit.d(withMessage: {"unknown effect"})
        }
    }
}

struct SettingsItemView: View {

    let imgName: String
    let title: String
    let subTitle: String
    let value: String
    let onClick: () -> Void

    var body: some View {
        VStack {
            HStack {
                Image(systemName: imgName)
                    .frame(width: 48, height: 48, alignment: .center)
                    .imageScale(.large)
                    .accentColor(MR.colors().text.get())

                VStack {
                    HStack {
                        Text(title).font(.title3)
                        Spacer()
                    }

                    HStack {
                        Text(subTitle).font(.footnote)
                        Spacer()
                    }
                }

                Spacer()

                Text(value).font(.footnote)

                Image(systemName: "chevron.right")
                    .frame(width: 48, height: 48, alignment: .center)
                    .imageScale(.large)
                    .accentColor(MR.colors().text.get())
            }

            Divider()
        }
        .onTapGesture { onClick() }
        .contentShape(Rectangle())
        .lineLimit(1)
    }
}
