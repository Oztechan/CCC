package config

object BuildValues {
    const val BASE_URL_BACKEND = "BASE_URL_BACKEND"
    const val BASE_URL_API = "BASE_URL_API"
    const val BASE_URL_API_PREMIUM = "BASE_URL_API_PREMIUM"
    const val API_KEY_PREMIUM = "API_KEY_PREMIUM"

    object Release {
        const val ADMOB_APP_ID = "ANDROID_RELEASE_ADMOB_APP_ID"
        const val BANNER_AD_UNIT_ID_CALCULATOR = "ANDROID_RELEASE_BANNER_AD_UNIT_ID_CALCULATOR"
        const val BANNER_AD_UNIT_ID_SETTINGS = "ANDROID_RELEASE_BANNER_AD_UNIT_ID_SETTINGS"
        const val BANNER_AD_UNIT_ID_CURRENCIES = "ANDROID_RELEASE_BANNER_AD_UNIT_ID_CURRENCIES"
        const val INTERSTITIAL_AD_ID = "ANDROID_RELEASE_INTERSTITIAL_AD_ID"
        const val REWARDED_AD_UNIT_ID = "ANDROID_RELEASE_REWARDED_AD_UNIT_ID"
    }

    object Debug {
        const val ADMOB_APP_ID = "ANDROID_DEBUG_ADMOB_APP_ID"
        const val BANNER_AD_UNIT_ID_CALCULATOR = "ANDROID_DEBUG_BANNER_AD_UNIT_ID_CALCULATOR"
        const val BANNER_AD_UNIT_ID_SETTINGS = "ANDROID_DEBUG_BANNER_AD_UNIT_ID_SETTINGS"
        const val BANNER_AD_UNIT_ID_CURRENCIES = "ANDROID_DEBUG_BANNER_AD_UNIT_ID_CURRENCIES"
        const val INTERSTITIAL_AD_ID = "ANDROID_DEBUG_INTERSTITIAL_AD_ID"
        const val REWARDED_AD_UNIT_ID = "ANDROID_DEBUG_REWARDED_AD_UNIT_ID"
    }

    object Signing {
        const val ANDROID_KEY_STORE_PATH = "ANDROID_KEY_STORE_PATH"
        const val ANDROID_STORE_PASSWORD = "ANDROID_STORE_PASSWORD"
        const val ANDROID_KEY_ALIAS = "ANDROID_KEY_ALIAS"
        const val ANDROID_KEY_PASSWORD = "ANDROID_KEY_PASSWORD"
    }

    object Fakes {
        const val PRIVATE_URL = "http://www.private-url.com"

        const val ADMOB_APP_ID = "ca-app-pub-3940256099942544~3347511713"
        const val BANNER_AD_UNIT_ID = "ca-app-pub-3940256099942544/6300978111"
        const val INTERSTITIAL_AD_ID = "ca-app-pub-3940256099942544/1033173712"
        const val REWARDED_AD_UNIT_ID = "ca-app-pub-3940256099942544/5224354917"
    }
}
