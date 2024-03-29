//
//  NotificationManager.swift
//  CCC
//
//  Created by Mustafa Ozhan on 27.05.22.
//  Copyright © 2022 orgName. All rights reserved.
//

import Provider
import UserNotifications

final class NotificationManager: ObservableObject {
    @Published var authorizationStatus: UNAuthorizationStatus?

    init() {
        logger.v(message: { "NotificationManager init" })
    }

    func reloadAuthorisationStatus() {
        logger.v(message: { "NotificationManager reloadAuthorisationStatus" })

        UNUserNotificationCenter.current().getNotificationSettings { settings in
            DispatchQueue.main.async {
                self.authorizationStatus = settings.authorizationStatus
            }
        }
    }

    func requestAuthorisation() {
        logger.v(message: { "NotificationManager requestAuthorisation" })
        UNUserNotificationCenter.current().requestAuthorization(
            options: [.badge, .alert, .sound]
        ) { isGranted, error in
            logger.v(message: {
                "NotificationManager requestAuthorisation error: \(String(describing: error)) isGradted: \(isGranted)"
            })
            DispatchQueue.main.async {
                self.authorizationStatus = isGranted ? .authorized : .denied
            }
        }
    }

    func sendNotification(title: String, body: String) {
        logger.v(message: { "NotificationManager sendNotification title:\(title) body:\(body)" })

        let trigger = UNTimeIntervalNotificationTrigger(timeInterval: 3, repeats: false)

        let content = UNMutableNotificationContent()
        content.sound = .default
        content.title = title
        content.body = body

        let request = UNNotificationRequest(
            identifier: UUID().uuidString,
            content: content,
            trigger: trigger
        )

        UNUserNotificationCenter.current().add(request) { error in
            logger.v(message: {
                "NotificationManager sendNotification error: \(String(describing: error))"
            })
        }
    }
}
