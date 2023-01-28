package com.oztechan.ccc.client.repository.appconfig

import com.oztechan.ccc.client.core.shared.Device

interface AppConfigRepository {

    fun getDeviceType(): Device

    fun getMarketLink(): String

    fun checkAppUpdate(isAppUpdateShown: Boolean): Boolean?

    fun shouldShowAppReview(): Boolean

    fun getVersion(): String
}
