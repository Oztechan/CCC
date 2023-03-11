package com.oztechan.ccc.client.core.shared

sealed class Device(
    open val name: String,
    open val marketLink: String
) {

    sealed class Android(
        override val name: String,
        override val marketLink: String,
        open val versionCode: Int
    ) : Device(name, marketLink) {

        data class Google(override val versionCode: Int) : Android(
            name = "google",
            marketLink = "https://play.google.com/store/apps/details?id=mustafaozhan.github.com.mycurrencies",
            versionCode = versionCode
        )

        data class Huawei(override val versionCode: Int) : Android(
            name = "huawei",
            marketLink = "https://appgallery.huawei.com/app/C104920917",
            versionCode = versionCode
        )
    }

    object IOS : Device(
        name = "ios",
        marketLink = "https://apps.apple.com/us/app/currency-converter-calculator/id1617484510"
    )
}
