//
//  MainScene.swift
//  CCC
//
//  Created by Mustafa Ozhan on 28/01/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import GoogleMobileAds
import NavigationStack
import Provider
import SwiftUI
import BackgroundTasks

struct MainScene: Scene {
	@Environment(\.scenePhase) private var scenePhase
	@State private var isWatcherAlertShown = false
	@StateObject var observable = ObservableSEEDViewModel<
		MainState,
		MainEffect,
		MainEvent,
		MainData,
		MainViewModel
	>()
	private let notificationManager = NotificationManager()
	private let backgroundRepository: BackgroundRepository = koin.get()

	private let taskID = "com.oztechan.ccc.CCC.fetch"
	private let earliestTaskPeriod: Double = 1 * 60 * 60 // 1 hour

	init() {
		registerAppRefresh()
	}

    var body: some Scene {
        WindowGroup {
            NavigationStackView(
                transitionType: .default,
                easing: Animation.easeInOut
            ) {
                if observable.state.shouldOnboardUser {
                    IntroSlideRootView()
                } else {
                    CalculatorRootView()
                }
            }.onAppear {
                observable.startObserving()
            }.onDisappear {
                observable.stopObserving()
            }.onReceive(observable.effect) {
                onEffect(effect: $0)
            }.alert(isPresented: $isWatcherAlertShown) {
                AlertView(
                    title: String(\.txt_watcher_alert_title),
                    message: String(\.txt_watcher_alert_sub_title),
                    buttonText: String(\.txt_ok)
                )
            }
        }.onChange(of: scenePhase) { phase in
            onScenePhaseChange(phase: phase)
        }
    }

    private func onScenePhaseChange(phase: ScenePhase) {
        logger.i(message: { "MainScene onChange scenePhase \(phase)" })

        switch phase {
        case .active:
            observable.event.onAppForeground()
        case .inactive:
            // only when come from active, since it visits inactive while coming from background
            if scenePhase == .active {
                observable.event.onAppBackground()
            }
        case .background:
            scheduleAppRefresh()
        @unknown default:
            logger.w(message: { "MainScene unknown scenePhase \(phase)" })
        }
    }

	private func onEffect(effect: MainEffect) {
		logger.i(message: { "MainScene onEffect \(effect.description)" })
		switch effect {
		case is MainEffect.ShowInterstitialAd:
			InterstitialAd().show()
		default:
			logger.i(message: { "MainScene unknown effect" })
		}
	}

	private func scheduleAppRefresh() {
		logger.i(message: { "MainScene scheduleAppRefresh" })

		let request = BGAppRefreshTaskRequest(identifier: taskID)
		request.earliestBeginDate = Date(timeIntervalSinceNow: earliestTaskPeriod)

		do {
			try BGTaskScheduler.shared.submit(request)
		} catch {
			logger.i(message: { "MainScene scheduleAppRefresh Could not schedule app refresh: \(error)" })
		}
	}

	private func registerAppRefresh() {
		logger.i(message: { "MainScene registerAppRefresh" })

		BGTaskScheduler.shared.cancelAllTaskRequests()

		BGTaskScheduler.shared.register(forTaskWithIdentifier: taskID, using: nil) { task in
			// swiftlint:disable:next force_cast
			handleAppRefresh(task: task as! BGAppRefreshTask)

			task.expirationHandler = {
				logger.i(message: { "MainScene registerAppRefresh BackgroundTask Expired" })

				task.setTaskCompleted(success: false)
			}
		}
	}

	private func handleAppRefresh(task: BGAppRefreshTask) {
		logger.i(message: { "MainScene handleAppRefresh" })

		scheduleAppRefresh()

		if backgroundRepository.shouldSendNotification() {
			if scenePhase == .background {
				self.notificationManager.sendNotification(
					title: String(\.txt_watcher_alert_title),
					body: String(\.txt_watcher_alert_sub_title)
				)
			} else {
				isWatcherAlertShown.toggle()
			}

			task.setTaskCompleted(success: true)
		} else {
			task.setTaskCompleted(success: true)
		}
	}
}