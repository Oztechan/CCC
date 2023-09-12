//
//  SettingsView.swift
//  CCC
//
//  Created by Mustafa Ozhan on 23/01/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import Provider
import Res
import SwiftUI

struct SettingsView: View {
    @Environment(\.colorScheme) private var colorScheme

    var event: SettingsEvent
    var state: SettingsState

    var body: some View {
        ZStack {
            Res.colors().background_strong.get().edgesIgnoringSafeArea(.all)

            VStack {
                SettingsToolbarView(backEvent: event.onBackClick)

                Form {
                    SettingsItemView(
                        imgName: "dollarsign.circle.fill",
                        title: String(\.settings_item_currencies_title),
                        subTitle: String(\.settings_item_currencies_sub_title),
                        value: Res.strings().settings_active_item_value.get(
                            parameter: state.activeCurrencyCount
                        ),
                        onClick: event.onCurrenciesClick
                    )

                    SettingsItemView(
                        imgName: "eyeglasses",
                        title: String(\.settings_item_watchers_title),
                        subTitle: String(\.settings_item_watchers_sub_title),
                        value: Res.strings().settings_active_item_value.get(
                            parameter: state.activeWatcherCount
                        ),
                        onClick: event.onWatchersClick
                    )

                    SettingsItemView(
                        imgName: "crown.fill",
                        title: String(\.settings_item_premium_title),
                        subTitle: String(\.settings_item_premium_sub_title_no_ads),
                        value: getPremiumText(premiumStatus: state.premiumStatus),
                        onClick: event.onPremiumClick
                    )

                    SettingsItemView(
                        imgName: "arrow.2.circlepath.circle.fill",
                        title: String(\.settings_item_sync_title),
                        subTitle: String(\.settings_item_sync_sub_title),
                        value: "",
                        onClick: event.onSyncClick
                    )

                    if MailView.canSendEmail() {
                        SettingsItemView(
                            imgName: "envelope.fill",
                            title: String(\.settings_item_feedback_title),
                            subTitle: String(\.settings_item_feedback_sub_title),
                            value: "",
                            onClick: event.onFeedBackClick
                        )
                    }

                    SettingsItemView(
                        imgName: "chevron.left.slash.chevron.right",
                        title: String(\.settings_item_on_github_title),
                        subTitle: String(\.settings_item_on_github_sub_title),
                        value: "",
                        onClick: event.onOnGitHubClick
                    )

                    SettingsItemView(
                        imgName: "textformat.123",
                        title: String(\.settings_item_version_title),
                        subTitle: String(\.settings_item_version_sub_title),
                        value: state.version,
                        onClick: {}
                    )
                }.edgesIgnoringSafeArea(.bottom)
                    .withClearBackground(color: Res.colors().background.get())

                if state.isBannerAdVisible {
                    AdaptiveBannerAdView(unitID: "BANNER_AD_UNIT_ID_SETTINGS").adapt()
                }
            }
            .navigationBarHidden(true)
        }
    }

    private func getPremiumText(premiumStatus: PremiumStatus) -> String {
        logger.i(message: { "SettingsView getPremiumText \(premiumStatus.description)" })

        switch premiumStatus {
        case is PremiumStatus.NeverActivated:
            return ""
        case let activateStatus as PremiumStatus.Active:
            return Res.strings().settings_item_premium_value_will_expire.get(parameter: activateStatus.until)
        case let expiredStatus as PremiumStatus.Expired:
            return Res.strings().settings_item_premium_value_expired.get(parameter: expiredStatus.at)
        default:
            return ""
        }
    }
}
