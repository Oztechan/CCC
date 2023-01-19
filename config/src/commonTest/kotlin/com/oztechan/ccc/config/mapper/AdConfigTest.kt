package com.oztechan.ccc.config.mapper

import com.oztechan.ccc.test.BaseTest
import kotlin.test.Test
import kotlin.test.assertEquals
import com.oztechan.ccc.config.service.ad.AdConfig as AdConfigRCModel

class AdConfigTest : BaseTest() {

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
