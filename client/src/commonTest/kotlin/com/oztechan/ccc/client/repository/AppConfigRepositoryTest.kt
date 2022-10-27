package com.oztechan.ccc.client.repository

import com.oztechan.ccc.client.BuildKonfig
import com.oztechan.ccc.client.model.Device
import com.oztechan.ccc.client.repository.appconfig.AppConfigRepository
import com.oztechan.ccc.client.repository.appconfig.AppConfigRepositoryImpl
import com.oztechan.ccc.client.storage.AppStorage
import com.oztechan.ccc.config.ConfigService
import com.oztechan.ccc.config.model.AdConfig
import com.oztechan.ccc.config.model.AppConfig
import com.oztechan.ccc.config.model.AppReview
import com.oztechan.ccc.config.model.AppUpdate
import com.oztechan.ccc.test.BaseSubjectTest
import io.mockative.Mock
import io.mockative.classOf
import io.mockative.given
import io.mockative.mock
import io.mockative.verify
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@Suppress("TooManyFunctions")
internal class AppConfigRepositoryTest : BaseSubjectTest<AppConfigRepository>() {

    override val subject: AppConfigRepository by lazy {
        AppConfigRepositoryImpl(configService, appStorage, device)
    }

    @Mock
    private val configService = mock(classOf<ConfigService>())

    @Mock
    private val appStorage = mock(classOf<AppStorage>())

    private val device = Device.IOS

    @Test
    fun getDeviceType() {
        assertEquals(device, subject.getDeviceType())
    }

    @Test
    fun getMarketLink() {
        assertEquals(device.marketLink, subject.getMarketLink())
    }

    @Test
    fun checkAppUpdate_should_return_false_when_force_and_current_version_bigger_than_current_version() {
        val mockName = device.name
        val mockAppConfig = AppConfig(
            AdConfig(0, 0, 0L, 0L),
            AppReview(0, 0L),
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

        subject.checkAppUpdate(false).let {
            assertNotNull(it)
            assertFalse { it }
        }

        verify(configService)
            .invocation { appConfig }
            .wasInvoked()
    }

    @Test
    fun checkAppUpdate_should_return_true_when_forceVersion_less_than_current_and_updateVersion_bigger_than_current() {
        val mockName = device.name
        val mockAppConfig = AppConfig(
            AdConfig(0, 0, 0L, 0L),
            AppReview(0, 0L),
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

        subject.checkAppUpdate(false).let {
            assertNotNull(it)
            assertTrue { it }
        }

        verify(configService)
            .invocation { appConfig }
            .wasInvoked()
    }

    @Test
    fun checkAppUpdate_should_return_null_when_update_and_force_version_is_less_than_current_version() {
        val mockName = device.name
        val mockAppConfig = AppConfig(
            AdConfig(0, 0, 0L, 0L),
            AppReview(0, 0L),
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

        assertNull(subject.checkAppUpdate(false))

        verify(configService)
            .invocation { appConfig }
            .wasInvoked()
    }

    @Test
    fun checkAppUpdate_should_return_null_when_device_name_is_different_than_remote() {
        val mockName = "mock"
        val mockAppConfig = AppConfig(
            AdConfig(0, 0, 0L, 0L),
            AppReview(0, 0L),
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

        assertNull(subject.checkAppUpdate(false))

        verify(configService)
            .invocation { appConfig }
            .wasInvoked()
    }

    @Test
    fun checkAppUpdate_should_return_null_when_it_is_already_shown() {
        val mockName = device.name
        val mockAppConfig = AppConfig(
            AdConfig(0, 0, 0L, 0L),
            AppReview(0, 0L),
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

        assertNull(subject.checkAppUpdate(true))

        verify(configService)
            .invocation { appConfig }
            .wasInvoked()
    }

    @Test
    fun shouldShowAppReview_should_return_true_when_sessionCount_is_biggerThan_remote_sessionCount() {
        val mockInteger = Random.nextInt()
        val mockAppConfig = AppConfig(
            AdConfig(0, 0, 0L, 0L),
            appReview = AppReview(appReviewSessionCount = mockInteger, appReviewDialogDelay = 0L),
            listOf()
        )

        given(configService)
            .invocation { appConfig }
            .then { mockAppConfig }

        given(appStorage)
            .invocation { sessionCount }
            .thenReturn(mockInteger.toLong() + 1)

        assertTrue { subject.shouldShowAppReview() }

        verify(appStorage)
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
            AdConfig(0, 0, 0L, 0L),
            appReview = AppReview(appReviewSessionCount = mockInteger, 0L),
            listOf()
        )

        given(configService)
            .invocation { appConfig }
            .then { mockAppConfig }

        given(appStorage)
            .invocation { sessionCount }
            .thenReturn(mockInteger.toLong() - 1)

        assertFalse { subject.shouldShowAppReview() }

        verify(appStorage)
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
            AdConfig(0, 0, 0L, 0L),
            appReview = AppReview(appReviewSessionCount = mockInteger, 0L),
            listOf()
        )

        given(configService)
            .invocation { appConfig }
            .then { mockAppConfig }

        given(appStorage)
            .invocation { sessionCount }
            .thenReturn(mockInteger.toLong())

        assertFalse { subject.shouldShowAppReview() }

        verify(appStorage)
            .invocation { sessionCount }
            .wasInvoked()

        verify(configService)
            .invocation { appConfig }
            .wasInvoked()
    }

    @Test
    fun getVersion() {
        assertEquals("${device.name.first()}${BuildKonfig.versionName}", subject.getVersion())
    }
}
