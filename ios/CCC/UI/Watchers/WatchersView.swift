//
//  WatchersView.swift
//  CCC
//
//  Created by Mustafa Ozhan on 26.04.22.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import Provider
import Res
import NavigationStack
import PopupView

struct WatchersView: View {

    @StateObject var observable = ObservableSEEDViewModel<
        WatchersState,
        WatchersEffect,
        WatchersEvent,
        WatchersData,
        WatchersViewModel
    >()
    @EnvironmentObject private var navigationStack: NavigationStackCompat
    @StateObject var notificationManager = NotificationManager()
    @State var baseBarInfo = BarInfo(isShown: false, watcher: nil)
    @State var targetBarInfo = BarInfo(isShown: false, watcher: nil)
    @State var isInvalidInputSnackShown = false
    @State var isMaxWatchersSnackShown = false
    @State var isTooBigNumberSnackShown = false

    private let analyticsManager: AnalyticsManager = koin.get()

    var body: some View {
        ZStack {
            MR.colors().background_strong.get().edgesIgnoringSafeArea(.all)

            VStack {
                WatchersToolbarView(backEvent: observable.event.onBackClick)

                switch notificationManager.authorizationStatus {
                case nil:
                    Spacer()
                case .authorized:
                    Form {
                        List(observable.state.watcherList, id: \.id) { watcher in
                            WatcherItem(
                                isBaseBarShown: $baseBarInfo.isShown,
                                isTargetBarShown: $targetBarInfo.isShown,
                                watcher: watcher,
                                event: observable.event
                            )
                        }
                        .listRowInsets(.init())
                        .listRowBackground(MR.colors().background.get())
                        .background(MR.colors().background.get())
                    }.withClearBackground(color: MR.colors().background.get())

                    Spacer()

                    VStack {
                        HStack {
                            Spacer()

                            Button {
                                observable.event.onAddClick()
                            } label: {
                                Label(MR.strings().txt_add.get(), systemImage: "plus")
                                    .imageScale(.large)
                                    .frame(width: 108.cp(), height: 24.cp(), alignment: .center)
                                    .font(relative: .body)
                            }
                            .foregroundColor(MR.colors().text.get())
                            .padding(.vertical, 15.cp())
                            .background(MR.colors().background_strong.get())

                            Spacer()

                        }
                    }
                    .frame(maxWidth: .infinity, alignment: .center)
                    .background(MR.colors().background_strong.get())

                default:
                    VStack {
                        Text(MR.strings().txt_enable_notification_permission.get())
                            .font(relative: .footnote)
                            .multilineTextAlignment(.center)
                        Button {
                            if let url = URL(
                                string: UIApplication.openSettingsURLString
                            ), UIApplication.shared.canOpenURL(url) {
                                UIApplication.shared.open(url)
                            }
                        } label: {
                            Label(MR.strings().txt_settings.get(), systemImage: "gear")
                                .imageScale(.large)
                                .frame(width: 108.cp(), height: 32.cp(), alignment: .center)
                                .font(relative: .body)
                        }
                        .padding(4.cp())
                        .background(MR.colors().background_weak.get())
                        .foregroundColor(MR.colors().text.get())
                        .cornerRadius(5.cp())
                        .padding(8.cp())

                    }.frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .center)
                        .background(MR.colors().background.get())
                }

                if observable.viewModel.shouldShowBannerAd() {
                    AdaptiveBannerAdView(unitID: "BANNER_AD_UNIT_ID_WATCHERS").adapt()
                }
            }
            .background(MR.colors().background_strong.get())
        }
        .popup(
            isPresented: $isInvalidInputSnackShown,
            type: .toast,
            autohideIn: 2.0
        ) {
            SnackView(text: MR.strings().text_invalid_input.get())
        }
        .popup(
            isPresented: $isMaxWatchersSnackShown,
            type: .toast,
            autohideIn: 2.0
        ) {
            SnackView(text: MR.strings().text_maximum_number_of_watchers.get())
        }
        .popup(
            isPresented: $isTooBigNumberSnackShown,
            type: .toast,
            autohideIn: 2.0
        ) {
            SnackView(text: MR.strings().text_too_big_number.get())
        }
        .sheet(
            isPresented: $baseBarInfo.isShown,
            content: {
                SelectCurrencyView(
                    isBarShown: $baseBarInfo.isShown,
                    onCurrencySelected: {
                        observable.event.onBaseChanged(
                            watcher: baseBarInfo.watcher!,
                            newBase: $0
                        )
                    }
                ).environmentObject(navigationStack)
            }
        )
        .sheet(
            isPresented: $targetBarInfo.isShown,
            content: {
                SelectCurrencyView(
                    isBarShown: $targetBarInfo.isShown,
                    onCurrencySelected: {
                        observable.event.onTargetChanged(
                            watcher: targetBarInfo.watcher!,
                            newTarget: $0
                        )
                    }
                ).environmentObject(navigationStack)
            }
        )
        .onAppear {
            observable.startObserving()
            notificationManager.reloadAuthorisationStatus()
            analyticsManager.trackScreen(screenName: ScreenName.Watchers())
        }
        .onDisappear { observable.stopObserving() }
        .onReceive(observable.effect) { onEffect(effect: $0) }
        .onReceive(NotificationCenter.default.publisher(
            for: UIApplication.willEnterForegroundNotification
        )) { _ in
            notificationManager.reloadAuthorisationStatus()
        }
        .onChange(of: notificationManager.authorizationStatus) {
            onAuthorisationChange(authorizationStatus: $0)
        }
        .animation(.default)
    }

    private func onEffect(effect: WatchersEffect) {
        logger.i(message: {"WatchersView onEffect \(effect.description)"})
        switch effect {
        case is WatchersEffect.Back:
            navigationStack.pop()
        // swiftlint:disable force_cast
        case is WatchersEffect.SelectBase:
            baseBarInfo.watcher = (effect as! WatchersEffect.SelectBase).watcher
            baseBarInfo.isShown.toggle()
        // swiftlint:disable force_cast
        case is WatchersEffect.SelectTarget:
            targetBarInfo.watcher = (effect as! WatchersEffect.SelectTarget).watcher
            targetBarInfo.isShown.toggle()
        case is WatchersEffect.TooBigNumber:
            isTooBigNumberSnackShown.toggle()
        case is WatchersEffect.InvalidInput:
            isInvalidInputSnackShown.toggle()
        case is WatchersEffect.MaximumNumberOfWatchers:
            isMaxWatchersSnackShown.toggle()
        default:
            logger.i(message: {"WatchersView unknown effect"})
        }
    }

    private func onAuthorisationChange(authorizationStatus: UNAuthorizationStatus?) {
        logger.i(message: {"WatchersView onAuthorisationChange \(String(describing: authorizationStatus?.rawValue))"})
        switch authorizationStatus {
        case .notDetermined:
            notificationManager.requestAuthorisation()
        case .authorized:
            notificationManager.reloadAuthorisationStatus()
        default:
            break
        }
    }

    struct BarInfo {
        var isShown: Bool
        var watcher: Provider.Watcher?
    }
}
