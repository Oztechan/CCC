//
//  NotificationsView.swift
//  CCC
//
//  Created by Mustafa Ozhan on 26.04.22.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import Client
import Resources
import NavigationStack

typealias NotificationObservable = ObservableSEED
<NotificationViewModel, NotificationState, NotificationEffect, NotificationEvent, BaseData>

struct NotificationView: View {
    @EnvironmentObject private var navigationStack: NavigationStack
    @StateObject var observable: NotificationObservable = koin.get()
    @State var baseBarInfo = BarInfo(isShown: false, notification: nil)
    @State var targetBarInfo = BarInfo(isShown: false, notification: nil)

    var notification: Client.Notification?

    var body: some View {
        ZStack {
            MR.colors().background_strong.get().edgesIgnoringSafeArea(.all)

            VStack {
                NotificationsToolbarView(backEvent: observable.event.onBackClick)
                    .background(MR.colors().background_strong.get())

                Form {
                    List(observable.state.notificationList, id: \.id) { notification in
                        NotificationItem(
                            isBaseBarShown: $baseBarInfo.isShown,
                            isTargetBarShown: $targetBarInfo.isShown,
                            event: observable.event,
                            notification: notification
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
                            notification: baseBarInfo.notification,
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
                            notification: targetBarInfo.notification,
                            newTarget: $0
                        )
                    }
                ).environmentObject(navigationStack)
            }
        )
        .onAppear { observable.startObserving() }
        .onDisappear { observable.stopObserving() }
        .onReceive(observable.effect) { onEffect(effect: $0) }
        .animation(.default)
    }

    private func onEffect(effect: NotificationEffect) {
        logger.i(message: {"NotificationView onEffect \(effect.description)"})
        switch effect {
        case is NotificationEffect.Back:
            navigationStack.pop()
        // swiftlint:disable force_cast
        case is NotificationEffect.SelectBase:
            baseBarInfo.notification = (effect as! NotificationEffect.SelectBase).notification
            baseBarInfo.isShown.toggle()
        // swiftlint:disable force_cast
        case is NotificationEffect.SelectTarget:
            targetBarInfo.notification = (effect as! NotificationEffect.SelectTarget).notification
            targetBarInfo.isShown.toggle()
        default:
            logger.i(message: {"NotificationView unknown effect"})
        }
    }

    struct BarInfo {
        var isShown: Bool
        var notification: Client.Notification?
    }
}
