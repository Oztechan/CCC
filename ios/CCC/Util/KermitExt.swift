//
//  KermitExt.swift
//  CCC
//
//  Created by Mustafa Ozhan on 26.09.23.
//  Copyright © 2023 orgName. All rights reserved.
//

import Provider

extension KermitLogger {
    func i(message: @escaping () -> String) {
        self.i(throwable: nil, tag: "Kermit", message: message)
    }

    func d(message: @escaping () -> String) {
        self.d(throwable: nil, tag: "Kermit", message: message)
    }

    func w(message: @escaping () -> String) {
        self.w(throwable: nil, tag: "Kermit", message: message)
    }

    func v(message: @escaping () -> String) {
        self.v(throwable: nil, tag: "Kermit", message: message)
    }

    func e(message: @escaping () -> String) {
        self.e(throwable: nil, tag: "Kermit", message: message)
    }

    func a(message: @escaping () -> String) {
        self.a(throwable: nil, tag: "Kermit", message: message)
    }
}
