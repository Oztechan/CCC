import org.gradle.api.Project
import java.io.IOException
import java.util.Locale
import java.util.Properties

object Keys {
    const val backendUrl = "BASE_URL_BACKEND"
    const val apiUrl = "BASE_URL_API"
    const val devUrl = "BASE_URL_DEV"

    const val admobAppId = "ADMOB_APP_ID"
    const val bannerAdUnitIdCalculator = "BANNER_AD_UNIT_ID_CALCULATOR"
    const val bannerAdUnitIdSettings = "BANNER_AD_UNIT_ID_SETTINGS"
    const val bannerAdUnitIdCurrencies = "BANNER_AD_UNIT_ID_CURRENCIES"
    const val interstitialAdId = "INTERSTITIAL_AD_ID"
    const val rewardedAdUnitId = "REWARDED_AD_UNIT_ID"

    const val keyStorePath = "KEY_STORE_PATH"
    const val storePassword = "STORE_PASSWORD"
    const val keyAlias = "KEY_ALIAS"
    const val keyPassword = "KEY_PASSWORD"
}

object Fakes {
    const val privateUrl = "http://www.private-url.com"

    const val admobAppId = "ca-app-pub-3940256099942544~3347511713"
    const val bannerAdUnitId = "ca-app-pub-3940256099942544/6300978111"
    const val interstitialAdId = "ca-app-pub-3940256099942544/1033173712"
    const val rewardedAdUnitId = "ca-app-pub-3940256099942544/5224354917"
}

object Type {
    const val string = "String"
}

fun Project.getSecretProperties() = try {
    Properties().apply { load(file("../secret.properties").inputStream()) }
} catch (e: IOException) {
    logger.debug(e.message, e)
    null
}

fun Properties?.get(key: String, default: String = "") = this?.get(key)?.toString() ?: default

fun String.toResource() = toLowerCase(Locale.ROOT)

fun String.toDebug() = "DEBUG_$this"

fun String.toRelease() = "RELEASE_$this"
