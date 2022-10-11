//
//  Application.swift
//  CCC
//
//  Created by Mustafa Ozhan on 16/11/2020.
//  Copyright © 2020 orgName. All rights reserved.
//

import SwiftUI
import Res
import Client
import FirebaseCore
import GoogleMobileAds
import BackgroundTasks

var logger: KermitLogger = {
    return IOSLoggerKt.doInitLogger(isCrashlyticsEnabled: EnvironmentUtil.isRelease)
}()

@main
struct Application: App {
    @Environment(\.scenePhase) private var scenePhase

    private let notificationManager = NotificationManager()
    private let backgroundRepository: BackgroundRepository

    private let taskID = "com.oztechan.ccc.CCC.fetch"
    private let earliestTaskPeriod: Double = 1 * 60 * 60 // 1 hour

    init() {
        if EnvironmentUtil.isRelease {
            FirebaseApp.configure()
        }

        logger.i(message: {"Application init"})

        GADMobileAds.sharedInstance().start(completionHandler: nil)

        UITableView.appearance().tableHeaderView = UIView(frame: CGRect(
            x: 0,
            y: 0,
            width: 0,
            height: Double.leastNonzeroMagnitude
        ))
        UITableView.appearance().backgroundColor = MR.colors().transparent.get()

        self.backgroundRepository = koin.get()

        registerAppRefresh()
    }

    var body: some Scene {
        WindowGroup {
            MainView()
        }.onChange(of: scenePhase) { phase in
            logger.i(message: {"Application onChange scenePhase \(phase)"})

            if phase == .background {
                scheduleAppRefresh()
            }
        }
    }

    private func scheduleAppRefresh() {
        logger.i(message: {"Application scheduleAppRefresh"})

        let request = BGAppRefreshTaskRequest(identifier: taskID)
        request.earliestBeginDate = Date(timeIntervalSinceNow: earliestTaskPeriod)

        do {
            try BGTaskScheduler.shared.submit(request)
        } catch {
            logger.i(message: {"Application scheduleAppRefresh Could not schedule app refresh: \(error)"})
        }
    }

    private func registerAppRefresh() {
        logger.i(message: {"Application registerAppRefresh"})

        BGTaskScheduler.shared.cancelAllTaskRequests()

        // swiftlint:disable force_cast
        BGTaskScheduler.shared.register(forTaskWithIdentifier: taskID, using: nil) { task in
            handleAppRefresh(task: task as! BGAppRefreshTask)

            task.expirationHandler = {
                logger.i(message: {"Application registerAppRefresh BackgroundTask Expired"})

                task.setTaskCompleted(success: false)
            }
        }
    }

    private func handleAppRefresh(task: BGAppRefreshTask) {
        logger.i(message: {"Application handleAppRefresh"})

        scheduleAppRefresh()

        if backgroundRepository.shouldSendNotification() {

            if scenePhase == .background {
                self.notificationManager.sendNotification(
                    title: MR.strings().txt_watcher_alert_title.get(),
                    body: MR.strings().txt_watcher_alert_sub_title.get()
                )
            } else {
                showAlert(
                    title: MR.strings().txt_watcher_alert_title.get(),
                    text: MR.strings().txt_watcher_alert_sub_title.get(),
                    buttonText: MR.strings().txt_ok.get()
                )
            }

            task.setTaskCompleted(success: true)
        } else {
            task.setTaskCompleted(success: true)
        }
    }
}
