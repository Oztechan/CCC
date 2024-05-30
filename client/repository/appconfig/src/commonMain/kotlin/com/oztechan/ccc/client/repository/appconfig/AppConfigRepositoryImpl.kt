package com.oztechan.ccc.client.repository.appconfig

import com.github.submob.scopemob.mapTo
import com.github.submob.scopemob.whether
import com.oztechan.ccc.client.configservice.review.ReviewConfigService
import com.oztechan.ccc.client.configservice.update.UpdateConfigService
import com.oztechan.ccc.client.core.shared.Device
import com.oztechan.ccc.client.storage.app.AppStorage

internal class AppConfigRepositoryImpl(
    private val updateConfigService: UpdateConfigService,
    private val reviewConfigService: ReviewConfigService,
    private val appStorage: AppStorage,
    private val device: Device
) : AppConfigRepository {
    override fun getDeviceType(): Device = device

    override fun getMarketLink(): String = device.marketLink

    override fun checkAppUpdate(
        isAppUpdateShown: Boolean
    ): Boolean? = updateConfigService.config
        .whether(
            { !isAppUpdateShown },
            { it.updateLatestVersion > BuildKonfig.versionCode }
        )?.let {
            it.updateForceVersion > BuildKonfig.versionCode
        } ?: run { null } // do not show

    override fun shouldShowAppReview(): Boolean = reviewConfigService.config
        .whether { appStorage.sessionCount > it.appReviewSessionCount }
        ?.mapTo { true }
        ?: false

    override fun getVersion(): String = "${device.name.first()}${BuildKonfig.versionName}"
}
