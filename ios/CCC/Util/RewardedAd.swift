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
    var onReward: () -> Void

    var rewardedAd: GADRewardedAd?

    init(onReward: @escaping () -> Void) {
        self.onReward = onReward
    }

    func show() {
        GADRewardedAd.load(
            withAdUnitID: SecretUtil.getSecret(key: "REWARDED_AD_UNIT_ID"),
            request: GADRequest(),
            completionHandler: { rewardedAd, error in
                if let error = error {
                    logger.w(message: { "RewardedAd show error: \(error.localizedDescription)" })
                    return
                }

                self.rewardedAd = rewardedAd
                self.rewardedAd?.fullScreenContentDelegate = self

                self.rewardedAd?.present(
                    fromRootViewController: UIApplication.shared.windows.first!.rootViewController!,
                    userDidEarnRewardHandler: {
                        logger.i(message: { "RewardedAd userDidEarnReward" })
                        self.onReward()
                    }
                )
            }
        )
    }
}
