//
//  WatchersRootView.swift
//  CCC
//
//  Created by Mustafa Ozhan on 26.04.22.
//  Copyright © 2022 orgName. All rights reserved.
//

import NavigationStack
import Provider
import SwiftUI

struct WatchersRootView: View {
    @Environment(\.colorScheme) private var colorScheme
    @EnvironmentObject private var navigationStack: NavigationStackCompat
    @StateObject var observable = ObservableSEEDViewModel<
        WatchersState,
        WatchersEffect,
        WatchersEvent,
        WatchersData,
        WatchersViewModel
    >()
    @StateObject var notificationManager = NotificationManager()
    @State var isSourceBarShown = false
    @State var isTargetBarShown = false
    @State var isInvalidInputSnackShown = false
    @State var isMaxWatchersSnackShown = false
    @State var isTooBigInputSnackShown = false

    private let analyticsManager: AnalyticsManager = koin.get()

    var body: some View {
        WatchersView(
            event: observable.event,
            state: observable.state,
            authorizationStatus: notificationManager.authorizationStatus,
            isSourceBarShown: $isSourceBarShown,
            isTargetBarShown: $isTargetBarShown
        )
        .snack(isPresented: $isInvalidInputSnackShown) {
            SnackView(text: String(\.text_invalid_input))
        }
        .snack(isPresented: $isMaxWatchersSnackShown) {
            SnackView(text: String(\.text_maximum_number_of_watchers))
        }
        .snack(isPresented: $isTooBigInputSnackShown) {
            SnackView(text: String(\.text_too_big_input))
        }
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
        .animation(.default, value: observable.state)
    }

    private func onEffect(effect: WatchersEffect) {
        logger.i(message: { "WatchersRootView onEffect \(effect.description)" })
        switch effect {
        case is WatchersEffect.Back:
            navigationStack.pop()
        case is WatchersEffect.SelectBase:
            isSourceBarShown.toggle()
        case is WatchersEffect.SelectTarget:
            isTargetBarShown .toggle()
        case is WatchersEffect.TooBigInput:
            isTooBigInputSnackShown.toggle()
        case is WatchersEffect.InvalidInput:
            isInvalidInputSnackShown.toggle()
        case is WatchersEffect.MaximumNumberOfWatchers:
            isMaxWatchersSnackShown.toggle()
        default:
            logger.i(message: { "WatchersRootView unknown effect" })
        }
    }

    private func onAuthorisationChange(authorizationStatus: UNAuthorizationStatus?) {
        logger.i(
            message: { "WatchersRootView onAuthorisationChange \(String(describing: authorizationStatus?.rawValue))" }
        )
        switch authorizationStatus {
        case .notDetermined:
            notificationManager.requestAuthorisation()
        case .authorized:
            notificationManager.reloadAuthorisationStatus()
        default:
            break
        }
    }
}
