import config.BuildType
import config.DeviceFlavour
import config.Keys
import config.key.TypedKey
import config.key.resId
import config.key.secret
import config.key.string

plugins {
    @Suppress("DSL_SCOPE_VIOLATION")
    libs.plugins.apply {
        id(androidLib.get().pluginId)
        id(android.get().pluginId)
    }
}

android {
    ProjectSettings.apply {
        namespace = Modules.Android.Core.ad.packageName
        compileSdk = COMPILE_SDK_VERSION
        defaultConfig.minSdk = MIN_SDK_VERSION

        compileOptions {
            sourceCompatibility = JAVA_VERSION
            targetCompatibility = JAVA_VERSION
        }
    }

    DeviceFlavour.apply {
        @Suppress("UnstableApiUsage")
        flavorDimensions.addAll(listOf(flavorDimension))

        productFlavors {
            create(google) {
                dimension = flavorDimension
            }

            create(huawei) {
                dimension = flavorDimension
            }
        }
    }

    buildTypes {
        getByName(BuildType.release) {
            TypedKey.values().forEach {
                resValue(string, it.name.resId, secret(it, BuildType.RELEASE))
            }
            Keys(project, BuildType.RELEASE).apply {
                resValue(typeString, bannerAdIdCalculator.resourceKey, bannerAdIdCalculator.value)
                resValue(typeString, bannerAdIdSettings.resourceKey, bannerAdIdSettings.value)
                resValue(typeString, bannerAdIdCurrencies.resourceKey, bannerAdIdCurrencies.value)
                resValue(typeString, interstitialAdId.resourceKey, interstitialAdId.value)
                resValue(typeString, rewardedAdId.resourceKey, rewardedAdId.value)
            }
        }

        getByName(BuildType.debug) {
            TypedKey.values().forEach {
                resValue(string, it.name.resId, secret(it, BuildType.DEBUG))
            }
            Keys(project, BuildType.DEBUG).apply {
                resValue(typeString, bannerAdIdCalculator.resourceKey, bannerAdIdCalculator.value)
                resValue(typeString, bannerAdIdSettings.resourceKey, bannerAdIdSettings.value)
                resValue(typeString, bannerAdIdCurrencies.resourceKey, bannerAdIdCurrencies.value)
                resValue(typeString, interstitialAdId.resourceKey, interstitialAdId.value)
                resValue(typeString, rewardedAdId.resourceKey, rewardedAdId.value)
            }
        }
    }
}

dependencies {
    libs.common.apply {
        implementation(koinCore)
        implementation(kermit)
    }

    DeviceFlavour.googleImplementation(libs.android.google.admob)
}
