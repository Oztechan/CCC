//
//  MainView.swift
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

struct MainRootView: Scene {
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
	private let backgroundRepository: BackgroundRepository

	private let taskID = "com.oztechan.ccc.CCC.fetch"
	private let earliestTaskPeriod: Double = 1 * 60 * 60 // 1 hour

	init() {
		UITableView.appearance().tableHeaderView = UIView(frame: CGRect(
			x: 0,
			y: 0,
			width: 0,
			height: Double.leastNonzeroMagnitude
		))
		UICollectionView.appearance().backgroundColor = .clear
		UITableView.appearance().backgroundColor = .clear

		self.backgroundRepository = koin.get()

		registerAppRefresh()
	}

	var body: some Scene {
		WindowGroup {
			MainView(state: observable.state)
				.onAppear {
					observable.startObserving()
					observable.event.onResume()
				}
				.onDisappear {
					observable.stopObserving()
					observable.event.onPause()
				}
                .onReceive(observable.effect) {
                    onEffect(effect: $0)
                }
				.alert(isPresented: $isWatcherAlertShown) {
					AlertView(
						title: String(\.txt_watcher_alert_title),
						message: String(\.txt_watcher_alert_sub_title),
						buttonText: String(\.txt_ok)
					)
				}
		}
        .onChange(of: scenePhase) { phase in
			logger.i(message: { "Application onChange scenePhase \(phase)" })

			if phase == .background {
				scheduleAppRefresh()
			}
        }
	}

	private func onEffect(effect: MainEffect) {
		logger.i(message: { "MainRootView onEffect \(effect.description)" })
		switch effect {
		case is MainEffect.ShowInterstitialAd:
			InterstitialAd().show()
		default:
			logger.i(message: { "MainRootView unknown effect" })
		}
	}

	private func scheduleAppRefresh() {
		logger.i(message: { "Application scheduleAppRefresh" })

		let request = BGAppRefreshTaskRequest(identifier: taskID)
		request.earliestBeginDate = Date(timeIntervalSinceNow: earliestTaskPeriod)

		do {
			try BGTaskScheduler.shared.submit(request)
		} catch {
			logger.i(message: { "Application scheduleAppRefresh Could not schedule app refresh: \(error)" })
		}
	}

	private func registerAppRefresh() {
		logger.i(message: { "Application registerAppRefresh" })

		BGTaskScheduler.shared.cancelAllTaskRequests()

		BGTaskScheduler.shared.register(forTaskWithIdentifier: taskID, using: nil) { task in
			// swiftlint:disable:next force_cast
			handleAppRefresh(task: task as! BGAppRefreshTask)

			task.expirationHandler = {
				logger.i(message: { "Application registerAppRefresh BackgroundTask Expired" })

				task.setTaskCompleted(success: false)
			}
		}
	}

	private func handleAppRefresh(task: BGAppRefreshTask) {
		logger.i(message: { "Application handleAppRefresh" })

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
