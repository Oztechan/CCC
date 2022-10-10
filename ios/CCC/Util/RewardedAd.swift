//
//  RewardedAd.swift
//  CCC
//
//  Created by Mustafa Ozhan on 07/07/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import GoogleMobileAds
import Res
import LogMob

final class RewardedAd: NSObject, GADFullScreenContentDelegate {

    var rewardFunction: () -> Void
    var errorFunction: () -> Void

    var rewardedAd: GADRewardedAd?

    init(
        rewardFunction: @escaping () -> Void,
        errorFunction: @escaping () -> Void
    ) {
        self.rewardFunction = rewardFunction
        self.errorFunction = errorFunction
    }

    func show() {
        GADRewardedAd.load(
            withAdUnitID: "REWARDED_AD_UNIT_ID".getSecretValue(),
            request: GADRequest(),
            completionHandler: { (rewardedAd, error) in
                if let error = error {
                    LoggerKt.w(message: {"RewardedAd show error: \(error.localizedDescription)"})
                    self.errorFunction()
                    return
                }

                self.rewardedAd = rewardedAd
                self.rewardedAd?.fullScreenContentDelegate = self

                self.rewardedAd?.present(
                    fromRootViewController: UIApplication.shared.windows.first!.rootViewController!,
                    userDidEarnRewardHandler: {
                        LoggerKt.i(message: {"RewardedAd userDidEarnReward"})
                        self.rewardFunction()
                    }
                )
            }
        )
    }
}
