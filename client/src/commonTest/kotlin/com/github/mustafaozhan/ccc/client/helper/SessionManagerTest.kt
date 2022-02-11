package com.github.mustafaozhan.ccc.client.helper

import com.github.mustafaozhan.ccc.client.util.getRandomDateLong
import com.github.mustafaozhan.ccc.client.util.isRewardExpired
import com.github.mustafaozhan.ccc.common.settings.SettingsRepository
import com.github.mustafaozhan.ccc.common.util.nowAsLong
import com.github.mustafaozhan.config.ConfigManager
import com.github.mustafaozhan.config.model.AdConfig
import com.github.mustafaozhan.config.model.AppConfig
import io.mockative.Mock
import io.mockative.classOf
import io.mockative.given
import io.mockative.mock
import io.mockative.verify
import kotlin.random.Random
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class SessionManagerTest {
    @Mock
    private val configManager = mock(classOf<ConfigManager>())

    @Mock
    private val settingsRepository = mock(classOf<SettingsRepository>())

    private val sessionManager: SessionManagerImpl by lazy {
        SessionManagerImpl(configManager, settingsRepository)
    }

    private val mockSessionCount = Random.nextLong()

    @BeforeTest
    fun setup() {
        given(settingsRepository)
            .invocation { sessionCount }
            .thenReturn(mockSessionCount)
    }

    @Test
    fun shouldShowBannerAd() {
        val mockLong = Random.getRandomDateLong()
        val mockBoolean = Random.nextBoolean()
        val mockBannerSessionCount = Random.nextInt()

        val mockAppConfig = AppConfig(AdConfig(bannerAdSessionCount = mockBannerSessionCount))

        given(settingsRepository)
            .invocation { adFreeEndDate }
            .thenReturn(mockLong)

        given(configManager)
            .invocation { appConfig }
            .then { mockAppConfig }

        given(settingsRepository)
            .invocation { firstRun }
            .thenReturn(mockBoolean)

        assertEquals(
            !mockBoolean && mockLong.isRewardExpired() && mockSessionCount > mockBannerSessionCount,
            sessionManager.shouldShowBannerAd()
        )

        verify(settingsRepository)
            .invocation { adFreeEndDate }
            .wasInvoked()

        verify(settingsRepository)
            .invocation { firstRun }
            .wasInvoked()

        verify(settingsRepository)
            .invocation { sessionCount }
            .wasInvoked()

        verify(configManager)
            .invocation { appConfig }
            .wasInvoked()
    }

    @Test
    fun shouldShowBannerAd_while_first_run_is_false() {
        val mockLong = Random.getRandomDateLong()
        val mockBannerSessionCount = Random.nextInt()

        val mockAppConfig = AppConfig(AdConfig(bannerAdSessionCount = mockBannerSessionCount))

        given(settingsRepository)
            .invocation { adFreeEndDate }
            .thenReturn(mockLong)

        given(configManager)
            .invocation { appConfig }
            .then { mockAppConfig }

        given(settingsRepository)
            .invocation { firstRun }
            .thenReturn(false)

        assertEquals(
            mockLong.isRewardExpired() && mockSessionCount > mockBannerSessionCount,
            sessionManager.shouldShowBannerAd()
        )

        verify(settingsRepository)
            .invocation { adFreeEndDate }
            .wasInvoked()

        verify(settingsRepository)
            .invocation { firstRun }
            .wasInvoked()

        verify(settingsRepository)
            .invocation { sessionCount }
            .wasInvoked()

        verify(configManager)
            .invocation { appConfig }
            .wasInvoked()
    }

    @Test
    fun shouldShowBannerAd_while_first_run_is_false_and_when_reward_expired() {
        val mockBannerSessionCount = Random.nextInt()

        val mockAppConfig = AppConfig(AdConfig(bannerAdSessionCount = mockBannerSessionCount))

        given(settingsRepository)
            .invocation { adFreeEndDate }
            .thenReturn(nowAsLong() - 1)

        given(configManager)
            .invocation { appConfig }
            .then { mockAppConfig }

        given(settingsRepository)
            .invocation { firstRun }
            .thenReturn(false)

        assertEquals(
            mockSessionCount > mockBannerSessionCount,
            sessionManager.shouldShowBannerAd()
        )

        verify(settingsRepository)
            .invocation { adFreeEndDate }
            .wasInvoked()

        verify(settingsRepository)
            .invocation { firstRun }
            .wasInvoked()

        verify(settingsRepository)
            .invocation { sessionCount }
            .wasInvoked()

        verify(configManager)
            .invocation { appConfig }
            .wasInvoked()
    }

    @Test
    fun shouldShowInterstitialAd() {

        val mockInterstitialAdSessionCount = Random.nextInt()

        val mockAppConfig =
            AppConfig(AdConfig(bannerAdSessionCount = mockInterstitialAdSessionCount))

        given(configManager)
            .invocation { appConfig }
            .then { mockAppConfig }

        assertEquals(
            mockSessionCount > mockInterstitialAdSessionCount,
            sessionManager.shouldShowInterstitialAd()
        )

        verify(settingsRepository)
            .invocation { sessionCount }
            .wasInvoked()

        verify(configManager)
            .invocation { appConfig }
            .wasInvoked()
    }
}
