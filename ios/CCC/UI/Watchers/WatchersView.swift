//
//  WatchersView.swift
//  CCC
//
//  Created by Mustafa Ozhan on 24.08.23.
//  Copyright © 2023 orgName. All rights reserved.
//

import Provider
import SwiftUI

struct WatchersView: View {
    @Environment(\.colorScheme) private var colorScheme

    var event: WatchersEvent
    var state: WatchersState
    var authorizationStatus: UNAuthorizationStatus?

    @Binding var baseBarInfo: WatchersRootView.BarInfo
    @Binding var targetBarInfo: WatchersRootView.BarInfo

    var body: some View {
        ZStack {
            Color(\.background_strong).edgesIgnoringSafeArea(.all)

            VStack {
                WatchersToolbarView(backEvent: event.onBackClick)

                switch authorizationStatus {
                case nil:
                    Spacer()
                case .authorized:
                    Form {
                        List(state.watcherList, id: \.id) { watcher in
                            WatcherItem(
                                isBaseBarShown: $baseBarInfo.isShown,
                                isTargetBarShown: $targetBarInfo.isShown,
                                watcher: watcher,
                                event: event
                            )
                        }
                        .listRowInsets(.init())
                        .listRowBackground(\.background)
                        .background(\.background)
                    }.withClearBackground(color: Color(\.background))

                    Spacer()

                    VStack {
                        HStack {
                            Spacer()

                            Button {
                                event.onAddClick()
                            } label: {
                                Label(String(\.txt_add), systemImage: "plus")
                                    .imageScale(.large)
                                    .frame(width: 108.cp(), height: 24.cp(), alignment: .center)
                                    .font(relative: .body)
                            }
                            .foregroundColor(\.text)
                            .padding(.vertical, 15.cp())
                            .background(\.background_strong)

                            Spacer()
                        }
                    }
                    .frame(maxWidth: .infinity, alignment: .center)
                    .background(\.background_strong)

                default:
                    VStack {
                        Text(String(\.txt_enable_notification_permission))
                            .font(relative: .footnote)
                            .multilineTextAlignment(.center)
                        Button {
                            if let url = URL(
                                string: UIApplication.openSettingsURLString
                            ), UIApplication.shared.canOpenURL(url) {
                                UIApplication.shared.open(url)
                            }
                        } label: {
                            Label(String(\.txt_settings), systemImage: "gear")
                                .imageScale(.large)
                                .frame(width: 108.cp(), height: 32.cp(), alignment: .center)
                                .font(relative: .body)
                        }
                        .padding(4.cp())
                        .background(\.background_weak)
                        .foregroundColor(\.text)
                        .cornerRadius(5.cp())
                        .padding(8.cp())
                    }.frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .center)
                        .background(\.background)
                }

                if state.isBannerAdVisible {
                    AdaptiveBannerAdView(unitID: "BANNER_AD_UNIT_ID_WATCHERS").adapt()
                }
            }
            .background(\.background_strong)
        }
    }
}
