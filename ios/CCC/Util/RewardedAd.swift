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

    // below variables have to be local otherwise userDidEarnRewardHandler is not called
    let onReward: () -> Void
    let onError: () -> Void
    var rewardedAd: GADRewardedAd?

    init(
        onReward: @escaping () -> Void,
        onError: @escaping () -> Void
    ) {
        self.onReward = onReward
        self.onError = onError
    }

    func show() {
        GADRewardedAd.load(
            withAdUnitID: SecretUtil.getSecret(key: "REWARDED_AD_UNIT_ID"),
            request: GADRequest(),
            completionHandler: {rewardedAd, error in
                if error != nil {
                    logger.w(message: { "RewardedAd show error: \(String(describing: error?.localizedDescription))" })
                    self.onError()
                    return
                }

                self.rewardedAd = rewardedAd
                self.rewardedAd?.fullScreenContentDelegate = self

                self.rewardedAd?.present(
                    fromRootViewController: WindowUtil.getCurrentController(),
                    userDidEarnRewardHandler: {
                        logger.v(message: { "RewardedAd userDidEarnReward" })
                        self.onReward()
                    }
                )
            }
        )
    }
}
