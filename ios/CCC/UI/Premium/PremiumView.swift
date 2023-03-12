//
//  PremiumView.swift
//  CCC
//
//  Created by Mustafa Ozhan on 26.02.23.
//  Copyright © 2023 orgName. All rights reserved.
//

import Res
import Provider
import SwiftUI

struct PremiumView: View {
    @StateObject var observable = ObservableSEEDViewModel<
        PremiumState,
        PremiumEffect,
        PremiumEvent,
        BaseData,
        PremiumViewModel
    >()
    @Environment(\.colorScheme) var colorScheme
    @Binding var premiumViewVisibility: Bool
    @State var isPremiumDialogShown = false

    private let analyticsManager: AnalyticsManager = koin.get()

    var body: some View {
        NavigationView {
            ZStack {
                Color(Res.colors().background_strong.get()).edgesIgnoringSafeArea(.all)

                VStack {
                    Text(Res.strings().txt_premium.get())
                        .font(relative: .title2)
                        .padding(4.cp())
                        .padding(.top, 10.cp())

                    if observable.state.loading {
                        FormProgressView()
                    } else {
                        Form {
                            List(observable.state.premiumTypes, id: \.data) { premiumType in
                                PremiumItemView(item: premiumType)
                                    .onTapGesture {
                                        observable.event.onPremiumItemClick(type: premiumType)
                                    }
                                    .frame(minWidth: 0, maxWidth: .infinity, alignment: .center)
                            }
                            .listRowInsets(.init())
                            .listRowBackground(Res.colors().background.get())

                            PremiumItemView(item: nil)
                                .listRowInsets(.init())
                                .listRowBackground(Res.colors().background.get())
                        }
                        .withClearBackground(color: Res.colors().background.get())
                    }

                    Spacer()
                }.navigationBarHidden(true)
            }
        }
        .popup(isPresented: $isPremiumDialogShown) {
            AlertView(
                title: Res.strings().txt_premium.get(),
                message: Res.strings().txt_premium_text.get(),
                buttonText: Res.strings().txt_watch.get(),
                buttonAction: {
                    observable.viewModel.showLoadingView(shouldShow: true)

                    RewardedAd(
                        onReward: {
                            observable.viewModel.updatePremiumEndDate(
                                adType: PremiumType.video,
                                startDate: DateUtilKt.nowAsLong(),
                                isRestorePurchase: false
                            )
                        },
                        onError: {
                            observable.viewModel.showLoadingView(shouldShow: false)
                        }
                    ).show()
                }
            )
        }
        .onAppear {
            observable.startObserving()
            observable.viewModel.showLoadingView(shouldShow: false)
            analyticsManager.trackScreen(screenName: ScreenName.Premium())
        }
        .onDisappear { observable.stopObserving() }
        .onReceive(observable.effect) { onEffect(effect: $0) }
    }

    private func onEffect(effect: PremiumEffect) {
        logger.i(message: { "PremiumView onEffect \(effect.description)" })
        switch effect {
        case let launchActivatePremiumFlowEffect as PremiumEffect.LaunchActivatePremiumFlow:
            if launchActivatePremiumFlowEffect.premiumType == PremiumType.video {
                isPremiumDialogShown.toggle()
            }
        default:
            logger.i(message: { "PremiumView unknown effect" })
        }
    }
}
