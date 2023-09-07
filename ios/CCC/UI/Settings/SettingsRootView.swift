//
//  SettingsRootView.swift
//  CCC
//
//  Created by Mustafa Ozhan on 23/01/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import GoogleMobileAds
import NavigationStack
import Provider
import Res
import SwiftUI

struct SettingsRootView: View {
    @StateObject var observable = ObservableSEEDViewModel<
        SettingsState,
        SettingsEffect,
        SettingsEvent,
        SettingsData,
        SettingsViewModel
    >()
    @Environment(\.colorScheme) private var colorScheme
    @EnvironmentObject private var navigationStack: NavigationStackCompat
    @State var premiumViewVisibility = false
    @State var emailViewVisibility = false
    @State var webViewVisibility = false
    @State var isAdsAlreadyDisabledSnackShown = false
    @State var isAlreadySyncedSnackShown = false
    @State var isSynchronisingShown = false
    @State var isSyncedSnackShown = false

    private let analyticsManager: AnalyticsManager = koin.get()

    var onBaseChange: ((String) -> Void)

    var body: some View {
        SettingsView(
            event: observable.event,
            state: observable.state
        )
        .snack(isPresented: $isAdsAlreadyDisabledSnackShown) {
            SnackView(text: Res.strings().txt_you_already_have_premium.get())
        }
        .snack(isPresented: $isAlreadySyncedSnackShown) {
            SnackView(text: Res.strings().txt_already_synced.get())
        }
        .snack(isPresented: $isSynchronisingShown) {
            SnackView(text: Res.strings().txt_synchronising.get())
        }
        .snack(isPresented: $isSyncedSnackShown) {
            SnackView(text: Res.strings().txt_synced.get())
        }
        .sheet(isPresented: $premiumViewVisibility) {
            PremiumRootView(premiumViewVisibility: $premiumViewVisibility)
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

    // swiftlint:disable:next cyclomatic_complexity
    private func onEffect(effect: SettingsEffect) {
        logger.i(message: { "SettingsRootView onEffect \(effect.description)" })
        switch effect {
        case is SettingsEffect.Back:
            navigationStack.pop()
        case is SettingsEffect.OpenCurrencies:
            navigationStack.push(CurrenciesRootView(onBaseChange: onBaseChange))
        case is SettingsEffect.OpenWatchers:
            navigationStack.push(WatchersRootView())
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
            premiumViewVisibility.toggle()
        default:
            logger.i(message: { "SettingsRootView unknown effect" })
        }
    }
}
