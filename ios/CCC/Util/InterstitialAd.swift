//
//  InterstitialAd.swift
//  CCC
//
//  Created by Mustafa Ozhan on 07/07/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import GoogleMobileAds
import Client

final class InterstitialAd: NSObject, GADFullScreenContentDelegate {

    var errorFunction: (() -> Void)?

    var interstitialAd: GADInterstitialAd?

    func show() {
        GADInterstitialAd.load(
            withAdUnitID: "INTERSTITIAL_AD_ID".getSecretValue(),
            request: GADRequest(),
            completionHandler: { interstitialAd, error in
                if let error = error {
                    logger.w(message: {"InterstitialAd show \(error.localizedDescription)"})
                    self.errorFunction?()
                    return
                }

                self.interstitialAd = interstitialAd
                self.interstitialAd?.fullScreenContentDelegate = self

                self.interstitialAd?.present(
                    fromRootViewController: UIApplication.shared.windows.first!.rootViewController!
                )
            }
        )
    }
}
