//
//  Application.swift
//  CCC
//
//  Created by Mustafa Ozhan on 16/11/2020.
//  Copyright © 2020 orgName. All rights reserved.
//

import BackgroundTasks
import FirebaseCore
import GoogleMobileAds
import Provider
import SwiftUI

var logger: KermitLogger = {
    return LoggerKt.doInitLogger(isCrashlyticsEnabled: EnvironmentUtil.isRelease)
}()

@main
struct Application: App {
    @Environment(\.scenePhase) private var scenePhase

    private let notificationManager = NotificationManager()
    private let backgroundRepository: BackgroundRepository

    private let taskID = "com.oztechan.ccc.CCC.fetch"
    private let earliestTaskPeriod: Double = 1 * 60 * 60 // 1 hour

    @State private var isWatcherAlertShown = false

    init() {
        if EnvironmentUtil.isRelease {
            FirebaseApp.configure()
        }

        logger.i(message: { "Application init" })

        GADMobileAds.sharedInstance().start(completionHandler: nil)
        GADMobileAds.sharedInstance().applicationMuted = true
        GADMobileAds.sharedInstance().applicationVolume = 0

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
            MainRootView()
                .alert(isPresented: $isWatcherAlertShown) {
                    AlertView(
                        title: String(\.txt_watcher_alert_title),
                        message: String(\.txt_watcher_alert_sub_title),
                        buttonText: String(\.txt_ok)
                    )
                }
        }.onChange(of: scenePhase) { phase in
            logger.i(message: { "Application onChange scenePhase \(phase)" })

            if phase == .background {
                scheduleAppRefresh()
            }
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
