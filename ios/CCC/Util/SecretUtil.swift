//
//  SecretExt.swift
//  CCC
//
//  Created by Mustafa Ozhan on 14.11.22.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation

public struct SecretUtil {
    public static  func getSecret(key: String) -> String {
        (Bundle.main.infoDictionary?[key] as? String) ?? "this is a secret value"
    }
}
