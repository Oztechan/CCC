package com.oztechan.ccc.client.configservice.ad.mapper

import kotlin.test.Test
import kotlin.test.assertEquals
import com.oztechan.ccc.client.core.remoteconfig.model.AdConfig as AdConfigRCModel

internal class AdConfigMapperTest {

    @Test
    fun toAdConfigModel() {
        val rcModel = AdConfigRCModel()
        val model = rcModel.toAdConfigModel()

        assertEquals(rcModel.bannerAdSessionCount, model.bannerAdSessionCount)
        assertEquals(rcModel.interstitialAdSessionCount, model.interstitialAdSessionCount)
        assertEquals(rcModel.interstitialAdInitialDelay, model.interstitialAdInitialDelay)
        assertEquals(rcModel.interstitialAdPeriod, model.interstitialAdPeriod)
    }

    @Test
    fun `session counts are capped at MAX_AD_SESSION_COUNT`() {
        val rcModel = AdConfigRCModel(
            bannerAdSessionCount = MAX_AD_SESSION_COUNT + 1,
            interstitialAdSessionCount = Int.MAX_VALUE
        )
        val model = rcModel.toAdConfigModel()

        assertEquals(MAX_AD_SESSION_COUNT, model.bannerAdSessionCount)
        assertEquals(MAX_AD_SESSION_COUNT, model.interstitialAdSessionCount)
    }

    @Test
    fun `session counts below the cap are left unchanged`() {
        val rcModel = AdConfigRCModel(bannerAdSessionCount = 2, interstitialAdSessionCount = 5)
        val model = rcModel.toAdConfigModel()

        assertEquals(2, model.bannerAdSessionCount)
        assertEquals(5, model.interstitialAdSessionCount)
    }
}
