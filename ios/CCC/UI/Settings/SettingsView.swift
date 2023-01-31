//
//  SettingsView.swift
//  CCC
//
//  Created by Mustafa Ozhan on 23/01/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import GoogleMobileAds
import NavigationStack
import PopupView
import Provider
import Res
import SwiftUI

struct SettingsView: View {
    @StateObject var observable = ObservableSEEDViewModel<
        SettingsState,
        SettingsEffect,
        SettingsEvent,
        SettingsData,
        SettingsViewModel
    >()
    @Environment(\.colorScheme) var colorScheme
    @EnvironmentObject private var navigationStack: NavigationStackCompat
    @State var emailViewVisibility = false
    @State var webViewVisibility = false
    @State var isPremiumDialogShown = false
    @State var isAdsAlreadyDisabledSnackShown = false
    @State var isAlreadySyncedSnackShown = false
    @State var isSynchronisingShown = false
    @State var isSyncedSnackShown = false

    private let analyticsManager: AnalyticsManager = koin.get()

    var onBaseChange: ((String) -> Void)

    var body: some View {
        ZStack {
            Res.colors().background_strong.get().edgesIgnoringSafeArea(.all)

            VStack {
                SettingsToolbarView(backEvent: observable.event.onBackClick)

                Form {
                    SettingsItemView(
                        imgName: "dollarsign.circle.fill",
                        title: Res.strings().settings_item_currencies_title.get(),
                        subTitle: Res.strings().settings_item_currencies_sub_title.get(),
                        value: Res.strings().settings_active_item_value.get(
                            parameter: observable.state.activeCurrencyCount
                        ),
                        onClick: observable.event.onCurrenciesClick
                    )

                    SettingsItemView(
                        imgName: "eyeglasses",
                        title: Res.strings().settings_item_watchers_title.get(),
                        subTitle: Res.strings().settings_item_watchers_sub_title.get(),
                        value: Res.strings().settings_active_item_value.get(
                            parameter: observable.state.activeWatcherCount
                        ),
                        onClick: observable.event.onWatchersClick
                    )

                    SettingsItemView(
                        imgName: "crown.fill",
                        title: Res.strings().settings_item_premium_title.get(),
                        subTitle: Res.strings().settings_item_premium_sub_title_no_ads.get(),
                        value: getPremiumText(),
                        onClick: observable.event.onPremiumClick
                    )

                    SettingsItemView(
                        imgName: "arrow.2.circlepath.circle.fill",
                        title: Res.strings().settings_item_sync_title.get(),
                        subTitle: Res.strings().settings_item_sync_sub_title.get(),
                        value: "",
                        onClick: observable.event.onSyncClick
                    )

                    if MailView.canSendEmail() {
                        SettingsItemView(
                            imgName: "envelope.fill",
                            title: Res.strings().settings_item_feedback_title.get(),
                            subTitle: Res.strings().settings_item_feedback_sub_title.get(),
                            value: "",
                            onClick: observable.event.onFeedBackClick
                        )
                    }

                    SettingsItemView(
                        imgName: "chevron.left.slash.chevron.right",
                        title: Res.strings().settings_item_on_github_title.get(),
                        subTitle: Res.strings().settings_item_on_github_sub_title.get(),
                        value: "",
                        onClick: observable.event.onOnGitHubClick
                    )

                    SettingsItemView(
                        imgName: "textformat.123",
                        title: Res.strings().settings_item_version_title.get(),
                        subTitle: Res.strings().settings_item_version_sub_title.get(),
                        value: observable.state.version,
                        onClick: {}
                    )
                }.edgesIgnoringSafeArea(.bottom)
                    .withClearBackground(color: Res.colors().background.get())

                if observable.viewModel.shouldShowBannerAd() {
                    AdaptiveBannerAdView(unitID: "BANNER_AD_UNIT_ID_SETTINGS").adapt()
                }
            }
            .navigationBarHidden(true)
        }
        .popup(
            isPresented: $isAdsAlreadyDisabledSnackShown,
            type: .toast,
            autohideIn: 2.0
        ) {
            SnackView(text: Res.strings().txt_you_already_have_premium.get())
        }
        .popup(
            isPresented: $isAlreadySyncedSnackShown,
            type: .toast,
            autohideIn: 2.0
        ) {
            SnackView(text: Res.strings().txt_already_synced.get())
        }
        .popup(
            isPresented: $isSynchronisingShown,
            type: .toast,
            autohideIn: 2.0
        ) {
            SnackView(text: Res.strings().txt_synchronising.get())
        }
        .popup(
            isPresented: $isSyncedSnackShown,
            type: .toast,
            autohideIn: 2.0
        ) {
            SnackView(text: Res.strings().txt_synced.get())
        }
        .popup(isPresented: $isPremiumDialogShown) {
            AlertView(
                title: Res.strings().txt_premium.get(),
                message: Res.strings().txt_premium_text.get(),
                buttonText: Res.strings().txt_watch.get(),
                buttonAction: {
                    RewardedAd(
                        onReward: { observable.viewModel.updatePremiumEndDate() }
                    ).show()
                }
            )
        }
        .sheet(isPresented: $emailViewVisibility) {
            MailView(isShowing: $emailViewVisibility)
        }
        .sheet(isPresented: $webViewVisibility) {
            WebView(url: NSURL(string: Res.strings().github_url.get())! as URL)
        }
        .onAppear {
            observable.startObserving()
            analyticsManager.trackScreen(screenName: ScreenName.Settings())
        }
        .onDisappear { observable.stopObserving() }
        .onReceive(observable.effect) { onEffect(effect: $0) }
    }

    private func onEffect(effect: SettingsEffect) {
        // swiftlint:disable:this cyclomatic_complexity

        logger.i(message: { "SettingsView onEffect \(effect.description)" })
        switch effect {
        case is SettingsEffect.Back:
            navigationStack.pop()
        case is SettingsEffect.OpenCurrencies:
            navigationStack.push(CurrenciesView(onBaseChange: onBaseChange))
        case is SettingsEffect.OpenWatchers:
            navigationStack.push(WatchersView())
        case is SettingsEffect.FeedBack:
            emailViewVisibility.toggle()
        case is SettingsEffect.OnGitHub:
            webViewVisibility.toggle()
        case is SettingsEffect.Synchronising:
            isSynchronisingShown.toggle()
        case is SettingsEffect.Synchronised:
            isSyncedSnackShown.toggle()
        case is SettingsEffect.OnlyOneTimeSync:
            isAlreadySyncedSnackShown.toggle()
        case is SettingsEffect.AlreadyPremium:
            isAdsAlreadyDisabledSnackShown.toggle()
        case is SettingsEffect.Premium:
            isPremiumDialogShown.toggle()
        default:
            logger.i(message: { "SettingsView unknown effect" })
        }
    }

    private func getPremiumText() -> String {
        if observable.viewModel.isPremiumEverActivated() {
            return ""
        } else {
            if observable.viewModel.isPremiumExpired() {
                return Res.strings().settings_item_premium_value_expired.get()
            } else {
                return Res.strings().settings_item_premium_value_will_expire.get(
                    parameter: observable.state.premiumEndDate
                )
            }
        }
    }
}
