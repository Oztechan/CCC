//
//  MailView.swift
//  CCC
//
//  Created by Mustafa Ozhan on 13.03.22.
//  Copyright © 2022 orgName. All rights reserved.
//

import MessageUI
import SwiftUI
import Res

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
        controller.setToRecipients([Res.strings().mail_developer.get()])
        controller.setSubject(Res.strings().mail_feedback_subject.get())
        controller.setMessageBody(Res.strings().mail_extra_text.get(), isHTML: false)
        return controller
    }

    func updateUIViewController(
        _ uiViewController: MFMailComposeViewController,
        context: UIViewControllerRepresentableContext<MailView>
    ) {

    }
}
