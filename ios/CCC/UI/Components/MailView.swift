//
//  MailView.swift
//  CCC
//
//  Created by Mustafa Ozhan on 13.03.22.
//  Copyright © 2022 orgName. All rights reserved.
//

import MessageUI
import SwiftUI

struct MailView: UIViewControllerRepresentable {
    @Binding var isShowing: Bool

    class Coordinator: NSObject, MFMailComposeViewControllerDelegate {
        @Binding var isShowing: Bool

        init(isShowing: Binding<Bool>) {
            _isShowing = isShowing
        }

        func mailComposeController(
            _ controller: MFMailComposeViewController,
            didFinishWith result: MFMailComposeResult,
            error: Error?
        ) {
            isShowing = false
        }
    }

    static func canSendEmail() -> Bool {
        return MFMailComposeViewController.canSendMail()
    }

    func makeCoordinator() -> Coordinator {
        return Coordinator(isShowing: $isShowing)
    }

    func makeUIViewController(context: UIViewControllerRepresentableContext<MailView>) -> MFMailComposeViewController {
        let controller = MFMailComposeViewController()
        controller.mailComposeDelegate = context.coordinator
        controller.setToRecipients([String(\.mail_developer)])
        controller.setSubject(String(\.mail_feedback_subject))
        controller.setMessageBody(String(\.mail_extra_text), isHTML: false)
        return controller
    }

    func updateUIViewController(
        _ uiViewController: MFMailComposeViewController,
        context: UIViewControllerRepresentableContext<MailView>
    ) {
        // no impl
    }
}
