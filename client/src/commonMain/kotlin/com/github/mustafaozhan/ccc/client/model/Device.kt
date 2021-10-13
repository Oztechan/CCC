package com.github.mustafaozhan.ccc.client.model

sealed class Device {
    sealed class ANDROID : Device() {
        object GOOGLE : ANDROID()
        object HUAWEI : ANDROID()
    }

    object IOS : Device()
}
