import java.util.Locale

object Keys {
    const val backendUrl = "BASE_URL_BACKEND"
    const val apiUrl = "BASE_URL_API"

    const val admobAppId = "ADMOB_APP_ID"
    const val bannerAdUnitIdCalculator = "BANNER_AD_UNIT_ID_CALCULATOR"
    const val bannerAdUnitIdSettings = "BANNER_AD_UNIT_ID_SETTINGS"
    const val bannerAdUnitIdCurrencies = "BANNER_AD_UNIT_ID_CURRENCIES"
    const val interstitialAdId = "INTERSTITIAL_AD_ID"
    const val rewardedAdUnitId = "REWARDED_AD_UNIT_ID"
}

object Values {
    val backendUrl = System.getenv(Keys.backendUrl)?.toString()
        ?: Fakes.backendUrl
    val apiUrl = System.getenv(Keys.apiUrl)?.toString()
        ?: Fakes.apiUrl

    object Debug {
        val admobAppId = Keys.admobAppId.toDebug()
            .getEnvVar(Fakes.admobAppId)
        val bannerAdUnitIdCalculator = Keys.bannerAdUnitIdCalculator.toDebug()
            .getEnvVar(Fakes.bannerAdUnitIdCalculator)
        val bannerAdUnitIdSettings = Keys.bannerAdUnitIdSettings.toDebug()
            .getEnvVar(Fakes.bannerAdUnitIdSettings)
        val bannerAdUnitIdCurrencies = Keys.bannerAdUnitIdCurrencies.toDebug()
            .getEnvVar(Fakes.bannerAdUnitIdCurrencies)
        val interstitialAdId = Keys.interstitialAdId.toDebug()
            .getEnvVar(Fakes.interstitialAdId)
        val rewardedAdUnitId = Keys.rewardedAdUnitId.toDebug()
            .getEnvVar(Fakes.rewardedAdUnitId)
    }

    object Release {
        val admobAppId = Keys.admobAppId.toRelease()
            .getEnvVar(Fakes.admobAppId)
        val bannerAdUnitIdCalculator = Keys.bannerAdUnitIdCalculator.toRelease()
            .getEnvVar(Fakes.bannerAdUnitIdCalculator)
        val bannerAdUnitIdSettings = Keys.bannerAdUnitIdSettings.toRelease()
            .getEnvVar(Fakes.bannerAdUnitIdSettings)
        val bannerAdUnitIdCurrencies = Keys.bannerAdUnitIdCurrencies.toRelease()
            .getEnvVar(Fakes.bannerAdUnitIdCurrencies)
        val interstitialAdId = Keys.interstitialAdId.toRelease()
            .getEnvVar(Fakes.interstitialAdId)
        val rewardedAdUnitId = Keys.rewardedAdUnitId.toRelease()
            .getEnvVar(Fakes.rewardedAdUnitId)
    }
}

object Fakes {
    const val backendUrl = "http://private.backend.url"
    const val apiUrl = "http://private.api.url"

    const val admobAppId = "ca-app-pub-3940256099942544~3347511713"
    const val bannerAdUnitIdCalculator = "ca-app-pub-3940256099942544/6300978111"
    const val bannerAdUnitIdSettings = "ca-app-pub-3940256099942544/6300978111"
    const val bannerAdUnitIdCurrencies = "ca-app-pub-3940256099942544/6300978111"
    const val interstitialAdId = "ca-app-pub-3940256099942544/1033173712"
    const val rewardedAdUnitId = "ca-app-pub-3940256099942544/5224354917"
}

object Type {
    const val string = "String"
}

private fun String.getEnvVar(defaultValue: String) = System.getenv(this)?.toString() ?: defaultValue

fun String.toResource() = toLowerCase(Locale.ROOT)

fun String.toDebug() = "DEBUG_$this"

fun String.toRelease() = "RELEASE_$this"
