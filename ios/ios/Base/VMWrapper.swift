//
//  VMWrapper.swift
//  ios
//
//  Created by Mustafa Ozhan on 26/12/2020.
//  Copyright © 2020 orgName. All rights reserved.
//

import Foundation

protocol VMWrapper: ObservableObject {
    func startObserving()
    func stopObserving()
}
