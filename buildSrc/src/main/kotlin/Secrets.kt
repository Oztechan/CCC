import org.gradle.api.Project
import java.io.IOException
import java.util.Properties

object Keys {
    const val baseUrlBackend = "BASE_URL_BACKEND"
    const val baseUrlApi = "BASE_URL_API"
    const val baseUrlDev = "BASE_URL_DEV"

    object Release {
        const val admobAppId = "ANDROID_RELEASE_ADMOB_APP_ID"
        const val bannerAdUnitIdCalculator = "ANDROID_RELEASE_BANNER_AD_UNIT_ID_CALCULATOR"
        const val bannerAdUnitIdSettings = "ANDROID_RELEASE_BANNER_AD_UNIT_ID_SETTINGS"
        const val bannerAdUnitIdCurrencies = "ANDROID_RELEASE_BANNER_AD_UNIT_ID_CURRENCIES"
        const val interstitialAdId = "ANDROID_RELEASE_INTERSTITIAL_AD_ID"
        const val rewardedAdUnitId = "ANDROID_RELEASE_REWARDED_AD_UNIT_ID"
    }

    object Debug {
        const val admobAppId = "ANDROID_DEBUG_ADMOB_APP_ID"
        const val bannerAdUnitIdCalculator = "ANDROID_DEBUG_BANNER_AD_UNIT_ID_CALCULATOR"
        const val bannerAdUnitIdSettings = "ANDROID_DEBUG_BANNER_AD_UNIT_ID_SETTINGS"
        const val bannerAdUnitIdCurrencies = "ANDROID_DEBUG_BANNER_AD_UNIT_ID_CURRENCIES"
        const val interstitialAdId = "ANDROID_DEBUG_INTERSTITIAL_AD_ID"
        const val rewardedAdUnitId = "ANDROID_DEBUG_REWARDED_AD_UNIT_ID"
    }

    object Signing {
        const val androidKeyStorePath = "ANDROID_KEY_STORE_PATH"
        const val androidStorePassword = "ANDROID_STORE_PASSWORD"
        const val androidKeyAlias = "ANDROID_KEY_ALIAS"
        const val androidKeyPassword = "ANDROID_KEY_PASSWORD"
    }
}

object Fakes {
    const val privateUrl = "http://www.private-url.com"

    const val admobAppId = "ca-app-pub-3940256099942544~3347511713"
    const val bannerAdUnitId = "ca-app-pub-3940256099942544/6300978111"
    const val interstitialAdId = "ca-app-pub-3940256099942544/1033173712"
    const val rewardedAdUnitId = "ca-app-pub-3940256099942544/5224354917"
}

fun String.removeVariant() = replace(
    oldValue = "_${BuildType.release}_",
    newValue = "_",
    ignoreCase = true
).replace(
    oldValue = "_${BuildType.debug}_",
    newValue = "_",
    ignoreCase = true
)

fun Project.getSecret(
    key: String,
    default: String = ""
): String = System.getenv(key).let {
    if (it.isNullOrEmpty()) {
        getSecretProperties()?.get(key)?.toString() ?: default
    } else {
        it
    }
}

fun Project.getSecretProperties() = try {
    Properties().apply { load(file("../secret.properties").inputStream()) }
} catch (e: IOException) {
    logger.debug(e.message, e)
    null
}
