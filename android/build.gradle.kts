/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
import config.BuildType
import config.DeviceFlavour
import config.DeviceFlavour.Companion.googleImplementation
import config.Keys

plugins {
    with(Dependencies.Plugins) {
        id(ANDROID_APP)
        id(CRASHLYTICS)
        id(GOOGLE_SERVICES)
        id(SAFE_ARGS)
        kotlin(ANDROID)
    }
}

android {
    with(ProjectSettings) {
        compileSdk = COMPILE_SDK_VERSION

        defaultConfig {
            minSdk = MIN_SDK_VERSION
            targetSdk = TARGET_SDK_VERSION

            versionCode = getVersionCode(project)
            versionName = getVersionName(project)
        }

        compileOptions {
            isCoreLibraryDesugaringEnabled = true
            sourceCompatibility = JavaVersion.VERSION_1_8
            targetCompatibility = JavaVersion.VERSION_1_8
        }

        kotlinOptions {
            jvmTarget = JavaVersion.VERSION_1_8.toString()
        }

        buildFeatures {
            viewBinding = true
        }
    }

    signingConfigs {
        create(BuildType.release) {
            with(Keys(project)) {
                storeFile = file(androidKeyStorePath.value)
                storePassword = androidStorePassword.value
                keyAlias = androidKeyAlias.value
                keyPassword = androidKeyPassword.value
            }
        }
    }

    with(DeviceFlavour) {
        flavorDimensions.addAll(listOf(flavorDimension))

        productFlavors {
            create(google) {
                dimension = flavorDimension
                applicationId = ProjectSettings.ANDROID_APP_ID
            }

            create(huawei) {
                dimension = flavorDimension
                applicationId = ProjectSettings.HUAWEI_APP_ID
            }
        }
    }

    buildTypes {
        getByName(BuildType.release) {
            signingConfig = signingConfigs.getByName(BuildType.release)
            isMinifyEnabled = false

            with(Keys(project, BuildType.RELEASE)) {
                resValue(typeString, admobAppId.resourceKey, admobAppId.value)
                resValue(typeString, bannerAdIdCalculator.resourceKey, bannerAdIdCalculator.value)
                resValue(typeString, bannerAdIdSettings.resourceKey, bannerAdIdSettings.value)
                resValue(typeString, bannerAdIdCurrencies.resourceKey, bannerAdIdCurrencies.value)
                resValue(typeString, interstitialAdId.resourceKey, interstitialAdId.value)
                resValue(typeString, rewardedAdId.resourceKey, rewardedAdId.value)
            }
        }

        getByName(BuildType.debug) {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
            with(Keys(project, BuildType.DEBUG)) {
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
    with(Dependencies.Android) {
        implementation(ANDROID_MATERIAL)
        implementation(CONSTRAINT_LAYOUT)
        implementation(NAVIGATION)
        implementation(KOIN_ANDROID)
        implementation(LIFECYCLE_RUNTIME)
        implementation(WORK_RUNTIME) // android 12 crash fix
        implementation(SPLASH_SCREEN)
        implementation(BASE_MOB)
        coreLibraryDesugaring(DESUGARING)
        debugImplementation(LEAK_CANARY)
    }

    googleImplementation(Dependencies.Android.GOOGLE.PLAY_CORE)

    with(Dependencies.Common) {
        implementation(KOTLIN_X_DATE_TIME)
        implementation(SCOPE_MOB)
        implementation(LOG_MOB)
    }

    with(Dependencies.Modules) {
        implementation(project(CLIENT))
        implementation(project(RES))
        implementation(project(BILLING))
        implementation(project(AD))
        implementation(project(ANALYTICS))
    }
}
