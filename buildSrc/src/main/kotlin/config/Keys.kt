package config

import getSecret
import org.gradle.api.Project
import java.util.Locale

class Keys(private val project: Project) {

    private var buildType: BuildType? = null

    val typeString = "string"

    constructor(project: Project, buildType: BuildType) : this(project) {
        this.buildType = buildType
    }

    val admobAppId = FlavoredVariable(
        releaseKey = BuildValues.Release.ADMOB_APP_ID,
        debugKey = BuildValues.Debug.ADMOB_APP_ID,
        fakeKey = BuildValues.Fakes.ADMOB_APP_ID
    )
    val bannerAdIdCalculator = FlavoredVariable(
        releaseKey = BuildValues.Release.BANNER_AD_UNIT_ID_CALCULATOR,
        debugKey = BuildValues.Debug.BANNER_AD_UNIT_ID_CALCULATOR,
        fakeKey = BuildValues.Fakes.BANNER_AD_UNIT_ID
    )
    val bannerAdIdSettings = FlavoredVariable(
        releaseKey = BuildValues.Release.BANNER_AD_UNIT_ID_SETTINGS,
        debugKey = BuildValues.Debug.BANNER_AD_UNIT_ID_SETTINGS,
        fakeKey = BuildValues.Fakes.BANNER_AD_UNIT_ID
    )
    val bannerAdIdCurrencies = FlavoredVariable(
        releaseKey = BuildValues.Release.BANNER_AD_UNIT_ID_CURRENCIES,
        debugKey = BuildValues.Debug.BANNER_AD_UNIT_ID_CURRENCIES,
        fakeKey = BuildValues.Fakes.BANNER_AD_UNIT_ID
    )
    val interstitialAdId = FlavoredVariable(
        releaseKey = BuildValues.Release.INTERSTITIAL_AD_ID,
        debugKey = BuildValues.Debug.INTERSTITIAL_AD_ID,
        fakeKey = BuildValues.Fakes.INTERSTITIAL_AD_ID
    )
    val rewardedAdId = FlavoredVariable(
        releaseKey = BuildValues.Release.REWARDED_AD_UNIT_ID,
        debugKey = BuildValues.Debug.REWARDED_AD_UNIT_ID,
        fakeKey = BuildValues.Fakes.REWARDED_AD_UNIT_ID
    )
    val baseUrlBackend = UnFlavoredVariable(
        key = BuildValues.BASE_URL_BACKEND,
        fakeKey = BuildValues.Fakes.PRIVATE_URL
    )
    val baseUrlApi = UnFlavoredVariable(
        key = BuildValues.BASE_URL_API,
        fakeKey = BuildValues.Fakes.PRIVATE_URL
    )
    val baseUrlApiPremium = UnFlavoredVariable(
        key = BuildValues.BASE_URL_API_PREMIUM,
        fakeKey = BuildValues.Fakes.PRIVATE_URL
    )
    val apiKeyPremium = UnFlavoredVariable(
        key = BuildValues.API_KEY_PREMIUM,
        fakeKey = BuildValues.Fakes.PRIVATE_URL
    )
    val androidKeyStorePath = UnFlavoredVariable(
        key = BuildValues.Signing.ANDROID_KEY_STORE_PATH,
        fakeKey = BuildValues.Fakes.PRIVATE_PATH
    )
    val androidStorePassword = UnFlavoredVariable(
        key = BuildValues.Signing.ANDROID_STORE_PASSWORD
    )
    val androidKeyAlias = UnFlavoredVariable(
        key = BuildValues.Signing.ANDROID_KEY_ALIAS
    )
    val androidKeyPassword = UnFlavoredVariable(
        key = BuildValues.Signing.ANDROID_KEY_PASSWORD
    )

    inner class UnFlavoredVariable(
        override val key: String,
        fakeKey: String = ""
    ) : BaseVariable(fakeKey)

    inner class FlavoredVariable(
        val releaseKey: String,
        val debugKey: String,
        fakeKey: String
    ) : BaseVariable(fakeKey)

    open inner class BaseVariable(fakeKey: String) {
        open val key: String by lazy {
            getVariantKey().removeVariant()
        }
        val resourceKey: String by lazy {
            key.lowercase()
        }
        val value: String by lazy {
            project.getSecret(getVariantKey(), fakeKey)
        }

        private fun getVariantKey(): String = when (this) {
            is FlavoredVariable -> when (buildType) {
                BuildType.DEBUG -> debugKey
                BuildType.RELEASE -> releaseKey
                else -> ""
            }
            is UnFlavoredVariable -> key
            else -> ""
        }

        private fun String.removeVariant() = replace(
            oldValue = "_${BuildType.release}_",
            newValue = "_",
            ignoreCase = true
        ).replace(
            oldValue = "_${BuildType.debug}_",
            newValue = "_",
            ignoreCase = true
        )
    }
}
