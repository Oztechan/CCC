//
//  RewardedAd.swift
//  CCC
//
//  Created by Mustafa Ozhan on 07/07/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import GoogleMobileAds
import Res

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
            withAdUnitID: SecretUtil.getSecret(key: "REWARDED_AD_UNIT_ID"),
            request: GADRequest(),
            completionHandler: { (rewardedAd, error) in
                if let error = error {
                    logger.w(message: {"RewardedAd show error: \(error.localizedDescription)"})
                    self.errorFunction()
                    return
                }

                self.rewardedAd = rewardedAd
                self.rewardedAd?.fullScreenContentDelegate = self

                self.rewardedAd?.present(
                    fromRootViewController: UIApplication.shared.windows.first!.rootViewController!,
                    userDidEarnRewardHandler: {
                        logger.i(message: {"RewardedAd userDidEarnReward"})
                        self.rewardFunction()
                    }
                )
            }
        )
    }
}
