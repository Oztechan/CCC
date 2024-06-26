package com.oztechan.ccc.client.repository.appconfig

import com.oztechan.ccc.client.configservice.review.ReviewConfigService
import com.oztechan.ccc.client.configservice.review.model.ReviewConfig
import com.oztechan.ccc.client.configservice.update.UpdateConfigService
import com.oztechan.ccc.client.configservice.update.model.UpdateConfig
import com.oztechan.ccc.client.core.shared.Device
import com.oztechan.ccc.client.storage.app.AppStorage
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.mock
import dev.mokkery.verify
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

internal class AppConfigRepositoryTest {

    private val subject: AppConfigRepository by lazy {
        AppConfigRepositoryImpl(updateConfigService, reviewConfigService, appStorage, device)
    }

    private val updateConfigService = mock<UpdateConfigService>()

    private val reviewConfigService = mock<ReviewConfigService>()

    private val appStorage = mock<AppStorage>()

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
    fun `checkAppUpdate should return true when force + current version bigger than current version`() {
        every { updateConfigService.config }
            .returns(UpdateConfig(BuildKonfig.versionCode + 1, BuildKonfig.versionCode + 1))

        subject.checkAppUpdate(false).let {
            assertNotNull(it)
            assertTrue { it }
        }

        verify { updateConfigService.config }
    }

    @Test
    fun `checkAppUpdate should return false when forceVersion less than current + updateVersion bigger than current`() {
        every { updateConfigService.config }
            .returns(UpdateConfig(BuildKonfig.versionCode + 1, BuildKonfig.versionCode - 1))

        subject.checkAppUpdate(false).let {
            assertNotNull(it)
            assertFalse { it }
        }

        verify { updateConfigService.config }
    }

    @Test
    fun `checkAppUpdate should return null when update is less than current version`() {
        every { updateConfigService.config }
            .returns(UpdateConfig(BuildKonfig.versionCode - 1, Random.nextInt()))

        assertNull(subject.checkAppUpdate(false))

        verify { updateConfigService.config }
    }

    @Test
    fun `checkAppUpdate should return null when update version is equal to current version`() {
        every { updateConfigService.config }
            .returns(UpdateConfig(BuildKonfig.versionCode, Random.nextInt()))

        assertNull(subject.checkAppUpdate(false))

        verify { updateConfigService.config }
    }

    @Test
    fun `checkAppUpdate should return null when it is already shown`() {
        every { updateConfigService.config }
            .returns(UpdateConfig(BuildKonfig.versionCode + 1, BuildKonfig.versionCode + 1))

        assertNull(subject.checkAppUpdate(true))

        verify { updateConfigService.config }
    }

    @Test
    fun `shouldShowAppReview should return true when sessionCount is biggerThan remote sessionCount`() {
        val mockInteger = Random.nextInt()

        every { reviewConfigService.config }
            .returns(ReviewConfig(appReviewSessionCount = mockInteger, 0L))

        every { appStorage.sessionCount }
            .returns(mockInteger.toLong() + 1)

        assertTrue { subject.shouldShowAppReview() }

        verify { appStorage.sessionCount }

        verify { reviewConfigService.config }
    }

    @Test
    fun `shouldShowAppReview should return false when sessionCount is less than remote sessionCount`() {
        val mockInteger = Random.nextInt()

        every { reviewConfigService.config }
            .returns(ReviewConfig(appReviewSessionCount = mockInteger, 0L))

        every { appStorage.sessionCount }
            .returns(mockInteger.toLong() - 1)

        assertFalse { subject.shouldShowAppReview() }

        verify { appStorage.sessionCount }

        verify { reviewConfigService.config }
    }

    @Test
    fun `shouldShowAppReview should return false when sessionCount is equal to remote sessionCount`() {
        val mockInteger = Random.nextInt()

        every { reviewConfigService.config }
            .returns(ReviewConfig(appReviewSessionCount = mockInteger, 0L))

        every { appStorage.sessionCount }
            .returns(mockInteger.toLong())

        assertFalse { subject.shouldShowAppReview() }

        verify { appStorage.sessionCount }

        verify { reviewConfigService.config }
    }

    @Test
    fun getVersion() {
        assertEquals("${device.name.first()}${BuildKonfig.versionName}", subject.getVersion())
    }
}
