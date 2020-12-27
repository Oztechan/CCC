//
//  BaseManager.swift
//  ios
//
//  Created by Mustafa Ozhan on 26/12/2020.
//  Copyright © 2020 orgName. All rights reserved.
//

import Foundation

protocol BaseManager: ObservableObject {
    func observeEffect()
    func observeStates()
    func stopObserving()
}
