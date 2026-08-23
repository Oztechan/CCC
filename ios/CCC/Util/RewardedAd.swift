//
//  RewardedAd.swift
//  CCC
//
//  Created by Mustafa Ozhan on 07/07/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import GoogleMobileAds
import Provider

final class RewardedAdManager: NSObject, FullScreenContentDelegate {

    // below variables have to be local otherwise userDidEarnRewardHandler is not called
    let onReward: () -> Void
    let onError: () -> Void
    private var rewardedAd: RewardedAd?

    init(
        onReward: @escaping () -> Void,
        onError: @escaping () -> Void
    ) {
        self.onReward = onReward
        self.onError = onError
    }

    func show() {
        RewardedAd.load(
            with: SecretUtil.getSecret(key: "REWARDED_AD_UNIT_ID"),
            request: Request(),
            completionHandler: {rewardedAd, error in
                if error != nil {
                    let throwable = KotlinThrowable(
                        message: "RewardedAd show error: \(String(describing: error?.localizedDescription))"
                    )
                    logger.e(throwable: throwable, tag: logger.tag, message: { String(describing: throwable.message) })
                    self.onError()
                    return
                }

                self.rewardedAd = rewardedAd
                self.rewardedAd?.fullScreenContentDelegate = self

                self.rewardedAd?.present(
                    from: WindowUtil.getCurrentController(),
                    userDidEarnRewardHandler: {
                        logger.v(message: { "RewardedAd userDidEarnReward" })
                        self.onReward()
                    }
                )
            }
        )
    }
}
