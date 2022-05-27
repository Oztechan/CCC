//
//  WatchersView.swift
//  CCC
//
//  Created by Mustafa Ozhan on 26.04.22.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import Client
import Resources
import NavigationStack

typealias WatchersObservable = ObservableSEED
<WatchersViewModel, WatchersState, WatchersEffect, WatchersEvent, WatchersData>

struct WatchersView: View {
    @EnvironmentObject private var navigationStack: NavigationStack
    @StateObject var observable: WatchersObservable = koin.get()
    @StateObject var notificationManager = NotificationManager()
    @State var baseBarInfo = BarInfo(isShown: false, watcher: nil)
    @State var targetBarInfo = BarInfo(isShown: false, watcher: nil)

    var watcher: Client.Watcher?

    var body: some View {
        ZStack {
            MR.colors().background_strong.get().edgesIgnoringSafeArea(.all)

            VStack {
                WatchersToolbarView(backEvent: observable.event.onBackClick)

                if notificationManager.authorizationStatus == .authorized {
                    Text(MR.strings().txt_txt_watchers_description.get())
                        .font(.footnote)
                        .padding(18)
                        .multilineTextAlignment(.center)
                        .background(MR.colors().background_strong.get())
                        .foregroundColor(MR.colors().text_weak.get())
                        .contentShape(Rectangle())
                        .padding(-8)

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
                    }

                    VStack {
                        Button {
                            observable.event.onAddClick()
                        } label: {
                            Label(MR.strings().txt_add.get(), systemImage: "plus")
                        }
                        .foregroundColor(MR.colors().text.get())
                        .padding(.top, 10)
                        .padding(.bottom, 20)
                    }
                    .padding(12)
                    .frame(maxWidth: .infinity, alignment: .center)
                    .background(MR.colors().background_strong.get())

                } else {
                    VStack {
                        Button {
                            notificationManager.requestAuthorisation()
                        } label: {
                            Text("Request Permission")
                        }
                    }.frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .center)
                }
            }
            .background(MR.colors().background.get())
            .edgesIgnoringSafeArea(.bottom)
        }
        .sheet(
            isPresented: $baseBarInfo.isShown,
            content: {
                SelectCurrencyView(
                    isBarShown: $baseBarInfo.isShown,
                    onCurrencySelected: {
                        observable.event.onBaseChanged(
                            watcher: baseBarInfo.watcher,
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
                            watcher: targetBarInfo.watcher,
                            newTarget: $0
                        )
                    }
                ).environmentObject(navigationStack)
            }
        )
        .onAppear {
            observable.startObserving()
            notificationManager.reloadAuthorisationStatus()
        }
        .onDisappear { observable.stopObserving() }
        .onReceive(observable.effect) { onEffect(effect: $0) }
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
        case is WatchersEffect.MaximumInput:
            showSnack(text: MR.strings().text_max_input.get(), isTop: true)
        case is WatchersEffect.InvalidInput:
            showSnack(text: MR.strings().text_invalid_input.get(), isTop: true)
        case is WatchersEffect.MaximumNumberOfWatchers:
            showSnack(text: MR.strings().text_maximum_number_of_watchers.get(), isTop: true)
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
        var watcher: Client.Watcher?
    }
}
