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
    @Environment(\.colorScheme) var colorScheme
    @ObservedObject var vmWrapper: SettingsVMWrapper = Koin.shared.settingsVMWrapper

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
                        title: MR.strings().txt_currencies.get(),
                        subTitle: MR.strings().txt_select_currencies.get(),
                        value: MR.plurals().settings_item_currencies_value.get(
                            quantitiy: vmWrapper.state.activeCurrencyCount
                        ),
                        onClick: { vmWrapper.event.onCurrenciesClick() }
                    )

                    SettingsItemView(
                        imgName: "lightbulb.fill",
                        title: MR.strings().settings_item_theme_title.get(),
                        subTitle: MR.strings().settings_item_theme_sub_title.get(),
                        value: vmWrapper.state.appThemeType.typeName,
                        onClick: { vmWrapper.event.onThemeClick() }
                    )

                    SettingsItemView(
                        imgName: "eye.slash.fill",
                        title: MR.strings().settings_item_remove_ads_title.get(),
                        subTitle: MR.strings().settings_item_remove_ads_sub_title.get(),
                        value: vmWrapper.state.addFreeDate,
                        onClick: { vmWrapper.event.onRemoveAdsClick() }
                    )

                    SettingsItemView(
                        imgName: "arrow.2.circlepath.circle.fill",
                        title: MR.strings().settings_item_sync_title.get(),
                        subTitle: MR.strings().settings_item_sync_sub_title.get(),
                        value: "",
                        onClick: { vmWrapper.event.onSyncClick() }
                    )

                    SettingsItemView(
                        imgName: "cart.fill",
                        title: MR.strings().settings_item_support_us_title.get(),
                        subTitle: MR.strings().settings_item_support_us_sub_title.get(),
                        value: "",
                        onClick: { vmWrapper.event.onSupportUsClick() }
                    )

                    SettingsItemView(
                        imgName: "envelope.fill",
                        title: MR.strings().settings_item_feedback_title.get(),
                        subTitle: MR.strings().settings_item_feedback_sub_title.get(),
                        value: "",
                        onClick: { vmWrapper.event.onFeedBackClick() }
                    )

                    SettingsItemView(
                        imgName: "chevron.left.slash.chevron.right",
                        title: MR.strings().settings_item_on_github_title.get(),
                        subTitle: MR.strings().settings_item_on_github_sub_title.get(),
                        value: "",
                        onClick: { vmWrapper.event.onOnGitHubClick() }
                    )

                }.background(MR.colors().background.get())

                NavigationLink(
                    destination: CurrenciesView(currenciesNavigationToogle: $currenciesNavigationToogle),
                    isActive: $currenciesNavigationToogle
                ) { }.hidden()
            }
            .navigationBarHidden(true)
        }
        .onReceive(vmWrapper.effect) { onEffect(effect: $0) }
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
                }

                Spacer()

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
        .listRowBackground(MR.colors().background.get())
        .onTapGesture { onClick() }
        .contentShape(Rectangle())
        .lineLimit(1)
    }
}
