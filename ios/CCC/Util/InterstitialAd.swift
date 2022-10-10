//
//  InterstitialAd.swift
//  CCC
//
//  Created by Mustafa Ozhan on 07/07/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import GoogleMobileAds
import Res
import LogMob

final class InterstitialAd: NSObject, GADFullScreenContentDelegate {
    func show() {
        GADInterstitialAd.load(
            withAdUnitID: "INTERSTITIAL_AD_ID".getSecretValue(),
            request: GADRequest(),
            completionHandler: { interstitialAd, error in
                if let error = error {
                    LoggerKt.w(message: {"InterstitialAd show \(error.localizedDescription)"})
                    return
                }

                interstitialAd?.fullScreenContentDelegate = self
                interstitialAd?.present(
                    fromRootViewController: UIApplication.shared.windows.first!.rootViewController!
                )
            }
        )
    }
}
