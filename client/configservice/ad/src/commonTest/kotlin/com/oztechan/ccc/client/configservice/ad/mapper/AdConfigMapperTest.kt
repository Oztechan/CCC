package com.oztechan.ccc.client.configservice.ad.mapper

import kotlin.test.Test
import kotlin.test.assertEquals
import com.oztechan.ccc.client.core.remoteconfig.model.AdConfig as AdConfigRCModel

class AdConfigMapperTest {

    @Test
    fun toAdConfigModel() {
        val rcModel = AdConfigRCModel()
        val model = rcModel.toAdConfigModel()

        assertEquals(rcModel.bannerAdSessionCount, model.bannerAdSessionCount)
        assertEquals(rcModel.interstitialAdSessionCount, model.interstitialAdSessionCount)
        assertEquals(rcModel.interstitialAdInitialDelay, model.interstitialAdInitialDelay)
        assertEquals(rcModel.interstitialAdPeriod, model.interstitialAdPeriod)
    }
}
