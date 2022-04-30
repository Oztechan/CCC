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

typealias NotificationsObservable = ObservableSEED
<NotificationsViewModel, NotificationsState, NotificationsEffect, NotificationsEvent, BaseData>

struct NotificationsView: View {

    @State var tootleStatus = false
    @State var isBaseBarShown = false
    @State var isTargetBarShown = false
    @State var amount = ""
    @State private var favoriteColor = 0

    @EnvironmentObject private var navigationStack: NavigationStack
    @StateObject var observable: NotificationsObservable = koin.get()

    var body: some View {
        ZStack {
            MR.colors().background_strong.get().edgesIgnoringSafeArea(.all)

            VStack {

                NotificationsToolbarView(backEvent: observable.event.onBackClick)
                    .background(MR.colors().background_strong.get())

                HStack {
                    Text(MR.strings().one.get())
                        .font(.title2)

                    NotificationCurrencyItem(
                        currencyName: observable.state.base,
                        clickAction: observable.event.onBaseClick
                    )
                }

                Picker("", selection: $favoriteColor) {
                    Text(MR.strings().txt_smaller.get())
                        .font(.title2)
                        .tag(0)
                    Text(MR.strings().txt_grater.get())
                        .font(.title2)
                        .tag(1)
                }
                .pickerStyle(.segmented)
                .frame(maxWidth: 120)

                HStack {
                    TextField(MR.strings().txt_rate.get(), text: $amount)
                        .keyboardType(.decimalPad)
                        .multilineTextAlignment(TextAlignment.center)
                        .fixedSize()
                        .lineLimit(1)

                    NotificationCurrencyItem(
                        currencyName: observable.state.target,
                        clickAction: observable.event.onTargetClick
                    )
                }

                NotificationStateView(
                    isEnabled: observable.state.isEnabled,
                    onClick: observable.event.onStateClick
                )

                Spacer()

            }
            .background(MR.colors().background.get())
            .edgesIgnoringSafeArea(.bottom)
        }
        .sheet(
            isPresented: $isBaseBarShown,
            content: {
                SelectCurrencyView(
                    isBarShown: $isBaseBarShown,
                    onCurrencySelected: { observable.event.onBaseChange(base: $0)}
                ).environmentObject(navigationStack)
            }
        )
        .sheet(
            isPresented: $isTargetBarShown,
            content: {
                SelectCurrencyView(
                    isBarShown: $isTargetBarShown,
                    onCurrencySelected: { observable.event.onTargetChange(target: $0)}
                ).environmentObject(navigationStack)
            }
        )
        .onAppear { observable.startObserving() }
        .onDisappear { observable.stopObserving() }
        .onReceive(observable.effect) { onEffect(effect: $0) }
    }

    private func onEffect(effect: NotificationsEffect) {
        logger.i(message: {"NotificationsView onEffect \(effect.description)"})
        switch effect {
        case is NotificationsEffect.Back:
            navigationStack.pop()
        case is NotificationsEffect.SelectBase:
            isBaseBarShown.toggle()
        case is NotificationsEffect.SelectTarget:
            isTargetBarShown.toggle()
        default:
            logger.i(message: {"NotificationsView unknown effect"})
        }
    }
}

struct NotificationsView_Previews: PreviewProvider {
    static var previews: some View {
        NotificationsView()
    }
}
