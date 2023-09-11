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
            Res.colors().background_strong.get().edgesIgnoringSafeArea(.all)

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
                        .listRowBackground(Res.colors().background.get())
                        .background(Res.colors().background.get())
                    }.withClearBackground(color: Res.colors().background.get())

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
                            .foregroundColor(Res.colors().text.get())
                            .padding(.vertical, 15.cp())
                            .background(Res.colors().background_strong.get())

                            Spacer()
                        }
                    }
                    .frame(maxWidth: .infinity, alignment: .center)
                    .background(Res.colors().background_strong.get())

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
                        .background(Res.colors().background_weak.get())
                        .foregroundColor(Res.colors().text.get())
                        .cornerRadius(5.cp())
                        .padding(8.cp())
                    }.frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .center)
                        .background(Res.colors().background.get())
                }

                if state.isBannerAdVisible {
                    AdaptiveBannerAdView(unitID: "BANNER_AD_UNIT_ID_WATCHERS").adapt()
                }
            }
            .background(Res.colors().background_strong.get())
        }
    }
}
