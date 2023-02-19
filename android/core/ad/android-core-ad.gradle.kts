import config.BuildType
import config.DeviceFlavour
import config.Keys

plugins {
    @Suppress("DSL_SCOPE_VIOLATION")
    libs.plugins.apply {
        id(androidLib.get().pluginId)
        id(android.get().pluginId)
    }
}

@Suppress("UnstableApiUsage")
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
            Keys(project, BuildType.RELEASE).apply {
                resValue(typeString, admobAppId.resourceKey, admobAppId.value)
                resValue(typeString, bannerAdIdCalculator.resourceKey, bannerAdIdCalculator.value)
                resValue(typeString, bannerAdIdSettings.resourceKey, bannerAdIdSettings.value)
                resValue(typeString, bannerAdIdCurrencies.resourceKey, bannerAdIdCurrencies.value)
                resValue(typeString, interstitialAdId.resourceKey, interstitialAdId.value)
                resValue(typeString, rewardedAdId.resourceKey, rewardedAdId.value)
            }
        }

        getByName(BuildType.debug) {
            Keys(project, BuildType.DEBUG).apply {
                resValue(typeString, admobAppId.resourceKey, admobAppId.value)
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

    @Suppress("UnstableApiUsage")
    DeviceFlavour.googleImplementation(libs.android.google.admob)
}
