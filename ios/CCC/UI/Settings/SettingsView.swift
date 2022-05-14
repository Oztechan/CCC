//
//  SettingsView.swift
//  CCC
//
//  Created by Mustafa Ozhan on 23/01/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import SwiftUI
import Resources
import Client
import NavigationStack
import GoogleMobileAds

typealias SettingsObservable = ObservableSEED
<SettingsViewModel, SettingsState, SettingsEffect, SettingsEvent, SettingsData>

struct SettingsView: View {
    @Environment(\.colorScheme) var colorScheme
    @EnvironmentObject private var navigationStack: NavigationStack
    @StateObject var observable: SettingsObservable = koin.get()
    @State var dialogVisibility: Bool = false
    @State var emailViewVisibility: Bool = false
    @State var webViewVisibility: Bool = false
    @State var activeDialog: Dialogs = Dialogs.error

    enum Dialogs {
        case removeAd, error
    }

    var onBaseChange: ((String) -> Void)

    var body: some View {

        ZStack {
            MR.colors().background_strong.get().edgesIgnoringSafeArea(.all)

            VStack {

                SettingsToolbarView(backEvent: observable.event.onBackClick)

                Form {
                    SettingsItemView(
                        imgName: "dollarsign.circle.fill",
                        title: MR.strings().settings_item_currencies_title.get(),
                        subTitle: MR.strings().settings_item_currencies_sub_title.get(),
                        value: MR.strings().settings_item_currencies_value.get(
                            parameter: observable.state.activeCurrencyCount
                        ),
                        onClick: observable.event.onCurrenciesClick
                    )

                    SettingsItemView(
                        imgName: "bell",
                        title: MR.strings().settings_item_notifications_title.get(),
                        subTitle: MR.strings().settings_item_notifications_sub_title.get(),
                        value: "",
                        onClick: observable.event.onNotificationsClicked
                    )

//                    SettingsItemView(
//                        imgName: "eye.slash.fill",
//                        title: MR.strings().settings_item_remove_ads_title.get(),
//                        subTitle: MR.strings().settings_item_remove_ads_sub_title.get(),
//                        value: getAdFreeText(),
//                        onClick: observable.event.onRemoveAdsClick
//                    )

                    SettingsItemView(
                        imgName: "arrow.2.circlepath.circle.fill",
                        title: MR.strings().settings_item_sync_title.get(),
                        subTitle: MR.strings().settings_item_sync_sub_title.get(),
                        value: "",
                        onClick: observable.event.onSyncClick
                    )

                    if MailView.canSendEmail() {
                        SettingsItemView(
                            imgName: "envelope.fill",
                            title: MR.strings().settings_item_feedback_title.get(),
                            subTitle: MR.strings().settings_item_feedback_sub_title.get(),
                            value: "",
                            onClick: observable.event.onFeedBackClick
                        )
                    }

                    SettingsItemView(
                        imgName: "chevron.left.slash.chevron.right",
                        title: MR.strings().settings_item_on_github_title.get(),
                        subTitle: MR.strings().settings_item_on_github_sub_title.get(),
                        value: "",
                        onClick: observable.event.onOnGitHubClick
                    )
                }
                .background(MR.colors().background.get())
                .edgesIgnoringSafeArea(.bottom)

//                if observable.viewModel.shouldShowBannerAd() {
//                    BannerAdView(
//                        unitID: "BANNER_AD_UNIT_ID_SETTINGS".getSecretValue()
//                    ).frame(maxHeight: 50)
//                    .padding(.bottom, 20)
//                }

            }
            .navigationBarHidden(true)
        }
        .sheet(isPresented: $emailViewVisibility) {
            MailView(isShowing: $emailViewVisibility)
        }
        .sheet(isPresented: $webViewVisibility) {
            WebView(url: NSURL(string: MR.strings().github_url.get())! as URL)
        }
        .alert(isPresented: $dialogVisibility) {
            switch activeDialog {
            case .error:
                return Alert(
                    title: Text(MR.strings().txt_remove_ads.get()),
                    message: Text(MR.strings().error_text_unknown.get()),
                    dismissButton: .destructive(Text(MR.strings().cancel.get()))
                )
            case .removeAd:
                return Alert(
                    title: Text(MR.strings().txt_remove_ads.get()),
                    message: Text(MR.strings().txt_remove_ads_text.get()),
                    primaryButton: .default(Text(MR.strings().txt_ok.get()), action: {
                        RewardedAd(
                            rewardFunction: { observable.viewModel.updateAddFreeDate() },
                            errorFunction: {
                                activeDialog = Dialogs.error
                                self.dialogVisibility.toggle()
                            }
                        ).show()
                    }),
                    secondaryButton: .destructive(Text(MR.strings().cancel.get()))
                )
            }
        }
        .onAppear { observable.startObserving() }
        .onDisappear { observable.stopObserving() }
        .onReceive(observable.effect) { onEffect(effect: $0) }
    }

    // swiftlint:disable cyclomatic_complexity
    private func onEffect(effect: SettingsEffect) {
        logger.i(message: {"SettingsView onEffect \(effect.description)"})
        switch effect {
        case is SettingsEffect.Back:
            navigationStack.pop()
        case is SettingsEffect.OpenCurrencies:
            navigationStack.push(CurrenciesView(onBaseChange: onBaseChange))
        case is SettingsEffect.OpenNotifications:
            navigationStack.push(NotificationView())
        case is SettingsEffect.FeedBack:
            emailViewVisibility.toggle()
        case is SettingsEffect.OnGitHub:
            webViewVisibility.toggle()
        case is SettingsEffect.Synchronising:
            showSnack(text: MR.strings().txt_synchronising.get())
        case is SettingsEffect.Synchronised:
            showSnack(text: MR.strings().txt_synced.get())
        case is SettingsEffect.OnlyOneTimeSync:
            showSnack(text: MR.strings().txt_already_synced.get())
        case is SettingsEffect.AlreadyAdFree:
            showSnack(text: MR.strings().txt_ads_already_disabled.get())
        case is SettingsEffect.RemoveAds:
            activeDialog = Dialogs.removeAd
            dialogVisibility.toggle()
        default:
            logger.i(message: {"SettingsView unknown effect"})
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
