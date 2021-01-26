//
//  SettingsView.swift
//  ios
//
//  Created by Mustafa Ozhan on 23/01/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import SwiftUI
import client

typealias SettingsObservable = SEEDObservable
<SettingsViewModel, SettingsState, SettingsEffect, SettingsEvent, SettingsData>

struct SettingsView: View {
    @Environment(\.colorScheme) var colorScheme
    @ObservedObject var seed: SettingsObservable = koin.get()

    @State var currenciesNavigationToogle = false
    @Binding var settingsNavvigationToogle: Bool

    init(settingsNavvigationToogle: Binding<Bool>) {
        LoggerKt.kermit.d(withMessage: {"BarView init"})
        self._settingsNavvigationToogle = settingsNavvigationToogle
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
                }.padding(EdgeInsets(top: 20, leading: 10, bottom: 5, trailing: 20))

                Form {
                    SettingsItemView(
                        imgName: "dollarsign.circle.fill",
                        title: MR.strings().settings_item_currencies_title.get(),
                        subTitle: MR.strings().settings_item_currencies_sub_title.get(),
                        value: MR.strings().settings_item_currencies_value.get(
                            parameter: seed.state.activeCurrencyCount
                        ),
                        onClick: { seed.event.onCurrenciesClick() }
                    )

                    SettingsItemView(
                        imgName: "lightbulb.fill",
                        title: MR.strings().settings_item_theme_title.get(),
                        subTitle: MR.strings().settings_item_theme_sub_title.get(),
                        value: seed.state.appThemeType.typeName,
                        onClick: { seed.event.onThemeClick() }
                    )

                    SettingsItemView(
                        imgName: "eye.slash.fill",
                        title: MR.strings().settings_item_remove_ads_title.get(),
                        subTitle: MR.strings().settings_item_remove_ads_sub_title.get(),
                        value: seed.state.addFreeDate,
                        onClick: { seed.event.onRemoveAdsClick() }
                    )

                    SettingsItemView(
                        imgName: "arrow.2.circlepath.circle.fill",
                        title: MR.strings().settings_item_sync_title.get(),
                        subTitle: MR.strings().settings_item_sync_sub_title.get(),
                        value: "",
                        onClick: { seed.event.onSyncClick() }
                    )

                    SettingsItemView(
                        imgName: "cart.fill",
                        title: MR.strings().settings_item_support_us_title.get(),
                        subTitle: MR.strings().settings_item_support_us_sub_title.get(),
                        value: "",
                        onClick: { seed.event.onSupportUsClick() }
                    )

                    SettingsItemView(
                        imgName: "envelope.fill",
                        title: MR.strings().settings_item_feedback_title.get(),
                        subTitle: MR.strings().settings_item_feedback_sub_title.get(),
                        value: "",
                        onClick: { seed.event.onFeedBackClick() }
                    )

                    SettingsItemView(
                        imgName: "chevron.left.slash.chevron.right",
                        title: MR.strings().settings_item_on_github_title.get(),
                        subTitle: MR.strings().settings_item_on_github_sub_title.get(),
                        value: "",
                        onClick: { seed.event.onOnGitHubClick() }
                    )

                }.background(MR.colors().background.get())

                NavigationLink(
                    destination: CurrenciesView(currenciesNavigationToogle: $currenciesNavigationToogle),
                    isActive: $currenciesNavigationToogle
                ) { }.hidden()
            }
            .navigationBarHidden(true)
        }
        .onReceive(seed.effect) { onEffect(effect: $0) }
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
        HStack {
            Image(systemName: imgName)
                .frame(width: 48, height: 48, alignment: .center)
                .imageScale(.large)
                .accentColor(MR.colors().text.get())
                .padding(.bottom, 4)
                .padding(.top, 4)

            VStack {
                HStack {
                    Text(title).font(.title3)
                    Spacer()
                }.padding(4)

                HStack {
                    Text(subTitle).font(.footnote)
                    Spacer()
                }.padding(4)
            }

            Spacer()

            Text(value).font(.caption)

            Image(systemName: "chevron.right")
                .frame(width: 48, height: 48, alignment: .center)
                .imageScale(.large)
                .accentColor(MR.colors().text.get())
        }
        .listRowBackground(MR.colors().background.get())
        .onTapGesture { onClick() }
        .contentShape(Rectangle())
        .lineLimit(1)
    }
}
