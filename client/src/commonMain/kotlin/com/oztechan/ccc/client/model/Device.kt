package com.oztechan.ccc.client.model

sealed class Device(open val name: String) {

    sealed class ANDROID(override val name: String, open val versionCode: Int) : Device(name) {
        data class Google(override val versionCode: Int) : ANDROID("google", versionCode)
        data class Huawei(override val versionCode: Int) : ANDROID("huawei", versionCode)
    }

    object IOS : Device("ios")
}
