package com.oztechan.ccc.client.repository

import com.oztechan.ccc.client.BuildKonfig
import com.oztechan.ccc.client.model.Device
import com.oztechan.ccc.client.repository.appconfig.AppConfigRepositoryImpl
import com.oztechan.ccc.common.datasource.settings.SettingsDataSource
import com.oztechan.ccc.config.ConfigService
import com.oztechan.ccc.config.model.AppConfig
import com.oztechan.ccc.config.model.AppReview
import com.oztechan.ccc.config.model.AppUpdate
import io.mockative.Mock
import io.mockative.classOf
import io.mockative.given
import io.mockative.mock
import io.mockative.verify
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@Suppress("TooManyFunctions")
class AppConfigRepositoryTest {
    @Mock
    private val configService = mock(classOf<ConfigService>())

    @Mock
    private val settingsDataSource = mock(classOf<SettingsDataSource>())

    private val device = Device.IOS

    private val repository: AppConfigRepositoryImpl by lazy {
        AppConfigRepositoryImpl(configService, settingsDataSource, device)
    }

    @Test
    fun getDeviceType() {
        assertEquals(device, repository.getDeviceType())
    }

    @Test
    fun getMarketLink() {
        assertEquals(device.marketLink, repository.getMarketLink())
    }

    @Test
    fun checkAppUpdate_should_return_false_when_force_and_current_version_bigger_than_current_version() {
        val mockName = device.name
        val mockAppConfig = AppConfig(
            appUpdate = listOf(
                AppUpdate(
                    name = mockName,
                    updateLatestVersion = BuildKonfig.versionCode + 1,
                    updateForceVersion = BuildKonfig.versionCode + 1
                )
            )
        )

        given(configService)
            .invocation { appConfig }
            .then { mockAppConfig }

        assertEquals(false, repository.checkAppUpdate(false))

        verify(configService)
            .invocation { appConfig }
            .wasInvoked()
    }

    @Test
    fun checkAppUpdate_should_return_true_when_forceVersion_less_than_current_and_updateVersion_bigger_than_current() {
        val mockName = device.name
        val mockAppConfig = AppConfig(
            appUpdate = listOf(
                AppUpdate(
                    name = mockName,
                    updateLatestVersion = BuildKonfig.versionCode + 1,
                    updateForceVersion = BuildKonfig.versionCode - 1
                )
            )
        )

        given(configService)
            .invocation { appConfig }
            .then { mockAppConfig }

        assertEquals(true, repository.checkAppUpdate(false))

        verify(configService)
            .invocation { appConfig }
            .wasInvoked()
    }

    @Test
    fun checkAppUpdate_should_return_null_when_update_and_force_version_is_less_than_current_version() {
        val mockName = device.name
        val mockAppConfig = AppConfig(
            appUpdate = listOf(
                AppUpdate(
                    name = mockName,
                    updateLatestVersion = BuildKonfig.versionCode - 1,
                    updateForceVersion = BuildKonfig.versionCode - 1
                )
            )
        )

        given(configService)
            .invocation { appConfig }
            .then { mockAppConfig }

        assertEquals(null, repository.checkAppUpdate(false))

        verify(configService)
            .invocation { appConfig }
            .wasInvoked()
    }

    @Test
    fun checkAppUpdate_should_return_null_when_device_name_is_different_than_remote() {
        val mockName = "mock"
        val mockAppConfig = AppConfig(
            appUpdate = listOf(
                AppUpdate(
                    name = mockName,
                    updateLatestVersion = BuildKonfig.versionCode + 1,
                    updateForceVersion = BuildKonfig.versionCode + 1
                )
            )
        )

        given(configService)
            .invocation { appConfig }
            .then { mockAppConfig }

        assertEquals(null, repository.checkAppUpdate(false))

        verify(configService)
            .invocation { appConfig }
            .wasInvoked()
    }

    @Test
    fun checkAppUpdate_should_return_null_when_it_is_already_shown() {
        val mockName = device.name
        val mockAppConfig = AppConfig(
            appUpdate = listOf(
                AppUpdate(
                    name = mockName,
                    updateLatestVersion = BuildKonfig.versionCode + 1,
                    updateForceVersion = BuildKonfig.versionCode + 1
                )
            )
        )

        given(configService)
            .invocation { appConfig }
            .then { mockAppConfig }

        assertEquals(null, repository.checkAppUpdate(true))

        verify(configService)
            .invocation { appConfig }
            .wasInvoked()
    }

    @Test
    fun shouldShowAppReview_should_return_true_when_sessionCount_is_biggerThan_remote_sessionCount() {
        val mockInteger = Random.nextInt()
        val mockAppConfig = AppConfig(
            appReview = AppReview(appReviewSessionCount = mockInteger)
        )

        given(configService)
            .invocation { appConfig }
            .then { mockAppConfig }

        given(settingsDataSource)
            .invocation { sessionCount }
            .thenReturn(mockInteger.toLong() + 1)

        assertTrue { repository.shouldShowAppReview() }

        verify(settingsDataSource)
            .invocation { sessionCount }
            .wasInvoked()

        verify(configService)
            .invocation { appConfig }
            .wasInvoked()
    }

    @Test
    fun shouldShowAppReview_should_return_false_when_sessionCount_is_less_than_remote_sessionCount() {
        val mockInteger = Random.nextInt()
        val mockAppConfig = AppConfig(
            appReview = AppReview(appReviewSessionCount = mockInteger)
        )

        given(configService)
            .invocation { appConfig }
            .then { mockAppConfig }

        given(settingsDataSource)
            .invocation { sessionCount }
            .thenReturn(mockInteger.toLong() - 1)

        assertFalse { repository.shouldShowAppReview() }

        verify(settingsDataSource)
            .invocation { sessionCount }
            .wasInvoked()

        verify(configService)
            .invocation { appConfig }
            .wasInvoked()
    }

    @Test
    fun shouldShowAppReview_should_return_false_when_sessionCount_is_equal_to_remote_sessionCount() {
        val mockInteger = Random.nextInt()
        val mockAppConfig = AppConfig(
            appReview = AppReview(appReviewSessionCount = mockInteger)
        )

        given(configService)
            .invocation { appConfig }
            .then { mockAppConfig }

        given(settingsDataSource)
            .invocation { sessionCount }
            .thenReturn(mockInteger.toLong())

        assertFalse { repository.shouldShowAppReview() }

        verify(settingsDataSource)
            .invocation { sessionCount }
            .wasInvoked()

        verify(configService)
            .invocation { appConfig }
            .wasInvoked()
    }
}
