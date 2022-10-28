package com.oztechan.ccc.client.repository

import com.oztechan.ccc.client.BuildKonfig
import com.oztechan.ccc.client.model.Device
import com.oztechan.ccc.client.repository.appconfig.AppConfigRepository
import com.oztechan.ccc.client.repository.appconfig.AppConfigRepositoryImpl
import com.oztechan.ccc.client.storage.AppStorage
import com.oztechan.ccc.config.model.ReviewConfig
import com.oztechan.ccc.config.model.UpdateConfig
import com.oztechan.ccc.config.review.ReviewConfigService
import com.oztechan.ccc.config.update.UpdateConfigService
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
        AppConfigRepositoryImpl(updateConfigService, reviewConfigService, appStorage, device)
    }

    @Mock
    private val updateConfigService = mock(classOf<UpdateConfigService>())

    @Mock
    private val reviewConfigService = mock(classOf<ReviewConfigService>())

    @Mock
    private val reviewConfigService = mock(classOf<ReviewConfigService>())

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
        given(updateConfigService)
            .invocation { config }
            .then { UpdateConfig(BuildKonfig.versionCode + 1, BuildKonfig.versionCode + 1) }

        subject.checkAppUpdate(false).let {
            assertNotNull(it)
            assertFalse { it }
        }

        verify(updateConfigService)
            .invocation { config }
            .wasInvoked()
    }

    @Test
    fun checkAppUpdate_should_return_true_when_forceVersion_less_than_current_and_updateVersion_bigger_than_current() {
        given(updateConfigService)
            .invocation { config }
            .then { UpdateConfig(BuildKonfig.versionCode + 1, BuildKonfig.versionCode - 1) }

        subject.checkAppUpdate(false).let {
            assertNotNull(it)
            assertTrue { it }
        }

        verify(updateConfigService)
            .invocation { config }
            .wasInvoked()
    }

    @Test
    fun checkAppUpdate_should_return_null_when_update_and_force_version_is_less_than_current_version() {
        given(updateConfigService)
            .invocation { config }
            .then { UpdateConfig(BuildKonfig.versionCode - 1, BuildKonfig.versionCode - 1) }

        assertNull(subject.checkAppUpdate(false))

        verify(updateConfigService)
            .invocation { config }
            .wasInvoked()
    }

    @Test
    fun checkAppUpdate_should_return_null_when_it_is_already_shown() {
        given(updateConfigService)
            .invocation { config }
            .then { UpdateConfig(BuildKonfig.versionCode + 1, BuildKonfig.versionCode + 1) }

        assertNull(subject.checkAppUpdate(true))

        verify(updateConfigService)
            .invocation { config }
            .wasInvoked()
    }

    @Test
    fun shouldShowAppReview_should_return_true_when_sessionCount_is_biggerThan_remote_sessionCount() {
        val mockInteger = Random.nextInt()

        given(reviewConfigService)
            .invocation { config }
            .then { ReviewConfig(appReviewSessionCount = mockInteger, 0L) }

        given(appStorage)
            .invocation { sessionCount }
            .thenReturn(mockInteger.toLong() + 1)

        assertTrue { subject.shouldShowAppReview() }

        verify(appStorage)
            .invocation { sessionCount }
            .wasInvoked()

        verify(reviewConfigService)
            .invocation { config }
            .wasInvoked()
    }

    @Test
    fun shouldShowAppReview_should_return_false_when_sessionCount_is_less_than_remote_sessionCount() {
        val mockInteger = Random.nextInt()

        given(reviewConfigService)
            .invocation { config }
            .then { ReviewConfig(appReviewSessionCount = mockInteger, 0L) }

        given(appStorage)
            .invocation { sessionCount }
            .thenReturn(mockInteger.toLong() - 1)

        assertFalse { subject.shouldShowAppReview() }

        verify(appStorage)
            .invocation { sessionCount }
            .wasInvoked()

        verify(reviewConfigService)
            .invocation { config }
            .wasInvoked()
    }

    @Test
    fun shouldShowAppReview_should_return_false_when_sessionCount_is_equal_to_remote_sessionCount() {
        val mockInteger = Random.nextInt()

        given(reviewConfigService)
            .invocation { config }
            .then { ReviewConfig(appReviewSessionCount = mockInteger, 0L) }

        given(appStorage)
            .invocation { sessionCount }
            .thenReturn(mockInteger.toLong())

        assertFalse { subject.shouldShowAppReview() }

        verify(appStorage)
            .invocation { sessionCount }
            .wasInvoked()

        verify(reviewConfigService)
            .invocation { config }
            .wasInvoked()
    }

    @Test
    fun getVersion() {
        assertEquals("${device.name.first()}${BuildKonfig.versionName}", subject.getVersion())
    }
}
