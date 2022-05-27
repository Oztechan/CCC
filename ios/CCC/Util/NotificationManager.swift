//
//  NotificationManager.swift
//  CCC
//
//  Created by Mustafa Ozhan on 27.05.22.
//  Copyright © 2022 orgName. All rights reserved.
//

import Client
import UserNotifications

final class NotificationManager: ObservableObject {
    @Published var authorizationStatus: UNAuthorizationStatus = .notDetermined

    func reloadAuthorisationStatus() {
        logger.i(message: {"NotificationManager reloadAuthorisationStatus"})

        UNUserNotificationCenter.current().getNotificationSettings { settings in
            DispatchQueue.main.async {
                self.authorizationStatus = settings.authorizationStatus
            }
        }
    }

    func requestAuthorisation() {
        logger.i(message: {"NotificationManager requestAuthorisation"})
        UNUserNotificationCenter.current().requestAuthorization(
            options: [.badge, .alert, .sound]
        ) { isGranted, error in

            logger.i(message: {
                "NotificationManager requestAuthorisation error: \(String(describing: error)) isGradted: \(isGranted)"
            })
            DispatchQueue.main.async {
                self.authorizationStatus = isGranted ? .authorized : .denied
            }
        }
    }
}
