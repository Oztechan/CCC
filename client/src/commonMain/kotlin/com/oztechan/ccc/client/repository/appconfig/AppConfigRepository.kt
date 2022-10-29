package com.oztechan.ccc.client.repository.appconfig

import com.oztechan.ccc.client.model.Device

interface AppConfigRepository {

    fun getDeviceType(): Device

    fun getMarketLink(): String

    fun checkAppUpdate(isAppUpdateShown: Boolean): Boolean?

    fun shouldShowAppReview(): Boolean

    fun getVersion(): String
}
