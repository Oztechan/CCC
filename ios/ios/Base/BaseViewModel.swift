//
//  BaseViewModel.swift
//  ios
//
//  Created by Mustafa Ozhan on 25/12/2020.
//  Copyright © 2020 orgName. All rights reserved.
//

import Foundation

protocol BaseViewModel: ObservableObject {
    func startObserving()
    func stopObserving()
}
