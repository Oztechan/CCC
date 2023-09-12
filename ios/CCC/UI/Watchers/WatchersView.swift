//
//  WatchersView.swift
//  CCC
//
//  Created by Mustafa Ozhan on 24.08.23.
//  Copyright © 2023 orgName. All rights reserved.
//

import Provider
import Res
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
                        .listRowBackground(Color(\.background))
                        .background(Color(\.background))
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
                            .foregroundColor(Color(\.text))
                            .padding(.vertical, 15.cp())
                            .background(Color(\.background_strong))

                            Spacer()
                        }
                    }
                    .frame(maxWidth: .infinity, alignment: .center)
                    .background(Color(\.background_strong))

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
                        .background(Color(\.background_weak))
                        .foregroundColor(Color(\.text))
                        .cornerRadius(5.cp())
                        .padding(8.cp())
                    }.frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .center)
                        .background(Color(\.background))
                }

                if state.isBannerAdVisible {
                    AdaptiveBannerAdView(unitID: "BANNER_AD_UNIT_ID_WATCHERS").adapt()
                }
            }
            .background(Color(\.background_strong))
        }
    }
}
