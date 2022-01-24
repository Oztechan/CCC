package com.github.mustafaozhan.ccc.client.model

sealed class Device(open val name: String) {

    sealed class ANDROID(override val name: String) : Device(name) {
        object GOOGLE : ANDROID("google")
        object HUAWEI : ANDROID("huawei")
    }

    object IOS : Device("ios")
}
