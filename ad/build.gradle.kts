import config.BuildType
import config.DeviceFlavour

plugins {
    with(Dependencies.Plugins) {
        id(ANDROID_LIB)
        kotlin(ANDROID)
    }
}

@Suppress("UnstableApiUsage")
android {
    with(ProjectSettings) {
        compileSdk = COMPILE_SDK_VERSION

        defaultConfig {
            minSdk = MIN_SDK_VERSION
            targetSdk = TARGET_SDK_VERSION
        }
    }

    with(DeviceFlavour) {
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
            with(config.Keys(project, BuildType.RELEASE)) {
                resValue(typeString, admobAppId.resourceKey, admobAppId.value)
                resValue(typeString, bannerAdIdCalculator.resourceKey, bannerAdIdCalculator.value)
                resValue(typeString, bannerAdIdSettings.resourceKey, bannerAdIdSettings.value)
                resValue(typeString, bannerAdIdCurrencies.resourceKey, bannerAdIdCurrencies.value)
                resValue(typeString, interstitialAdId.resourceKey, interstitialAdId.value)
                resValue(typeString, rewardedAdId.resourceKey, rewardedAdId.value)
            }
        }

        getByName(BuildType.debug) {
            with(config.Keys(project, BuildType.DEBUG)) {
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
    with(DeviceFlavour) {
        googleImplementation(Dependencies.Android.GOOGLE.ADMOB)
    }

    implementation(project(Dependencies.Modules.LOGMOB))
}
