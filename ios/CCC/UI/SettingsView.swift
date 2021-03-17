//
//  SettingsView.swift
//  CCC
//
//  Created by Mustafa Ozhan on 23/01/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import SwiftUI
import Client
import NavigationStack

typealias SettingsObservable = ObservableSEED
<SettingsViewModel, SettingsState, SettingsEffect, SettingsEvent, SettingsData>

struct SettingsView: View {
    @Environment(\.colorScheme) var colorScheme
    @EnvironmentObject private var navigationStack: NavigationStack
    @StateObject var observable: SettingsObservable = koin.get()
    @State var removeAdDialogVisibility: Bool = false

    var onBaseChange: ((String) -> Void)

    var body: some View {

        ZStack {
            MR.colors().background_strong.get().edgesIgnoringSafeArea(.all)

            VStack {

                SettingsToolbarView(backEvent: { observable.event.onBackClick() })

                Form {
                    SettingsItemView(
                        imgName: "dollarsign.circle.fill",
                        title: MR.strings().settings_item_currencies_title.get(),
                        subTitle: MR.strings().settings_item_currencies_sub_title.get(),
                        value: MR.strings().settings_item_currencies_value.get(
                            parameter: observable.state.activeCurrencyCount
                        ),
                        onClick: { observable.event.onCurrenciesClick() }
                    )
                    SettingsItemView(
                        imgName: "eye.slash.fill",
                        title: MR.strings().settings_item_remove_ads_title.get(),
                        subTitle: MR.strings().settings_item_remove_ads_sub_title.get(),
                        value: getAdFreeText(),
                        onClick: { observable.event.onRemoveAdsClick() }
                    )
                    SettingsItemView(
                        imgName: "arrow.2.circlepath.circle.fill",
                        title: MR.strings().settings_item_sync_title.get(),
                        subTitle: MR.strings().settings_item_sync_sub_title.get(),
                        value: "",
                        onClick: { observable.event.onSyncClick() }
                    )
                    SettingsItemView(
                        imgName: "envelope.fill",
                        title: MR.strings().settings_item_feedback_title.get(),
                        subTitle: MR.strings().settings_item_feedback_sub_title.get(),
                        value: "",
                        onClick: { observable.event.onFeedBackClick() }
                    )
                    SettingsItemView(
                        imgName: "chevron.left.slash.chevron.right",
                        title: MR.strings().settings_item_on_github_title.get(),
                        subTitle: MR.strings().settings_item_on_github_sub_title.get(),
                        value: "",
                        onClick: { observable.event.onOnGitHubClick() }
                    )
                }.background(MR.colors().background.get())
            }
            .navigationBarHidden(true)
        }
        .alert(isPresented: $removeAdDialogVisibility) {
            Alert(
                title: Text(MR.strings().txt_remove_ads.get()),
                message: Text(MR.strings().txt_remove_ads_text.get()),
                primaryButton: .default(Text(MR.strings().txt_ok.get()), action: {
                    // todo show rewarded ad
                }),
                secondaryButton: .destructive(Text(MR.strings().cancel.get()))
            )
        }
        .onAppear { observable.startObserving() }
        .onDisappear { observable.stopObserving() }
        .onReceive(observable.effect) { onEffect(effect: $0) }
    }

    private func onEffect(effect: SettingsEffect) {
        LoggerKt.kermit.d(withMessage: {effect.description})
        switch effect {
        case is SettingsEffect.Back:
            navigationStack.pop()
        case is SettingsEffect.OpenCurrencies:
            navigationStack.push(CurrenciesView(onBaseChange: onBaseChange))
        case is SettingsEffect.FeedBack:
            EmailHelper().sendFeedback()
        case is SettingsEffect.OnGitHub:
            UIApplication.shared.open(NSURL(string: MR.strings().github_url.get())! as URL)
        case is SettingsEffect.Synchronising:
            showToast(text: MR.strings().txt_synchronising.get())
        case is SettingsEffect.Synchronised:
            showToast(text: MR.strings().txt_synced.get())
        case is SettingsEffect.OnlyOneTimeSync:
            showToast(text: MR.strings().txt_already_synced.get())
        case is SettingsEffect.AlreadyAdFree:
            showToast(text: MR.strings().txt_ads_already_disabled.get())
        case is SettingsEffect.RemoveAds:
            removeAdDialogVisibility = true
        default:
            LoggerKt.kermit.d(withMessage: {"SettingsView unknown effect"})
        }
    }

    private func getAdFreeText() -> String {
        if observable.viewModel.isAdFreeNeverActivated() {
            return ""
        } else {
            if observable.viewModel.isRewardExpired() {
                return MR.strings().settings_item_remove_ads_value_expired.get()
            } else {
                return MR.strings().settings_item_remove_ads_value_will_expire.get(
                    parameter: observable.state.addFreeEndDate
                )
            }
        }
    }

}

struct SettingsToolbarView: View {
    @Environment(\.colorScheme) var colorScheme
    var backEvent: () -> Void

    var body: some View {
        HStack {

            Button(
                action: { backEvent() },
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
                .font(.system(size: 24))
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
        .contentShape(Rectangle())
        .onTapGesture { onClick() }
        .lineLimit(1)
    }
}
