//
//  Application.swift
//  CCC
//
//  Created by Mustafa Ozhan on 16/11/2020.
//  Copyright © 2020 orgName. All rights reserved.
//

import SwiftUI
import Resources
import Client
import Firebase
import GoogleMobileAds
import BackgroundTasks

let logger = LoggerKt.doInitLogger()

@main
struct Application: App {
    @Environment(\.scenePhase) private var scenePhase
    @State var alertVisibility: Bool = false

    private let notificationManager = NotificationManager()
    private let backgroundManager: BackgroundManager

    private let taskID = "com.oztechan.ccc.CCC.fetch"
    private let earliestTaskPeriod: Double = 1 * 60 * 60 // 1 hour

    init() {
        logger.i(message: {"Application init"})

        #if RELEASE
            FirebaseApp.configure()
        #endif

//        GADMobileAds.sharedInstance().start(completionHandler: nil)

        startKoin()

        UITableView.appearance().tableHeaderView = UIView(frame: CGRect(
            x: 0,
            y: 0,
            width: 0,
            height: Double.leastNonzeroMagnitude
        ))
        UITableView.appearance().backgroundColor = MR.colors().transparent.get()

        self.backgroundManager = koin.get()
    }

    var body: some Scene {
        WindowGroup {
            MainView()
                .alert(isPresented: $alertVisibility) {
                    Alert(
                        title: Text(MR.strings().txt_watcher_alert_title.get()),
                        message: Text(MR.strings().txt_watcher_alert_sub_title.get()),
                        dismissButton: .destructive(Text(MR.strings().txt_ok.get()))
                    )
                }
        }.onChange(of: scenePhase) { phase in
            logger.i(message: {"Application \(phase)"})

            switch phase {
            case .active:
                registerAppRefresh()
            case .background:
                scheduleAppRefresh()
            default: break
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
                logger.i(message: {"Application handleAppRefresh BackgroundTask Expired"})

                task.setTaskCompleted(success: false)
            }
        }
    }

    private func handleAppRefresh(task: BGAppRefreshTask) {
        logger.i(message: {"Application handleAppRefresh"})

        scheduleAppRefresh()

        if backgroundManager.shouldSendNotification() {

            if scenePhase == .background {
                self.notificationManager.sendNotification(
                    title: MR.strings().txt_watcher_alert_title.get(),
                    body: MR.strings().txt_watcher_alert_sub_title.get()
                )
            } else {
                self.alertVisibility = true
            }

            task.setTaskCompleted(success: true)
        } else {
            task.setTaskCompleted(success: true)
        }
    }
}
