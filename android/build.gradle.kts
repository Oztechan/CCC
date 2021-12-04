/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
import config.BuildType
import config.DeviceFlavour

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

            applicationId = PROJECT_ID

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
            with(BuildValues.Signing) {
                storeFile = file(getSecret(ANDROID_KEY_STORE_PATH))
                storePassword = getSecret(ANDROID_STORE_PASSWORD)
                keyAlias = getSecret(ANDROID_KEY_ALIAS)
                keyPassword = getSecret(ANDROID_KEY_PASSWORD)
            }
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
            signingConfig = signingConfigs.getByName(BuildType.release)
            isMinifyEnabled = false

            with(BuildValues.Release) {
                resValue(
                    BuildValues.Type.STRING,
                    ADMOB_APP_ID.toResourceName(),
                    getSecret(ADMOB_APP_ID, BuildValues.Fakes.ADMOB_APP_ID)
                )
                resValue(
                    BuildValues.Type.STRING,
                    BANNER_AD_UNIT_ID_CALCULATOR.toResourceName(),
                    getSecret(BANNER_AD_UNIT_ID_CALCULATOR, BuildValues.Fakes.BANNER_AD_UNIT_ID)
                )
                resValue(
                    BuildValues.Type.STRING,
                    BANNER_AD_UNIT_ID_SETTINGS.toResourceName(),
                    getSecret(BANNER_AD_UNIT_ID_SETTINGS, BuildValues.Fakes.BANNER_AD_UNIT_ID)
                )
                resValue(
                    BuildValues.Type.STRING,
                    BANNER_AD_UNIT_ID_CURRENCIES.toResourceName(),
                    getSecret(BANNER_AD_UNIT_ID_CURRENCIES, BuildValues.Fakes.BANNER_AD_UNIT_ID)
                )
                resValue(
                    BuildValues.Type.STRING,
                    INTERSTITIAL_AD_ID.toResourceName(),
                    getSecret(INTERSTITIAL_AD_ID, BuildValues.Fakes.INTERSTITIAL_AD_ID)
                )
                resValue(
                    BuildValues.Type.STRING,
                    REWARDED_AD_UNIT_ID.toResourceName(),
                    getSecret(REWARDED_AD_UNIT_ID, BuildValues.Fakes.REWARDED_AD_UNIT_ID)
                )
            }
        }

        getByName(BuildType.debug) {
            with(BuildValues.Debug) {
                resValue(
                    BuildValues.Type.STRING,
                    ADMOB_APP_ID.toResourceName(),
                    getSecret(ADMOB_APP_ID, BuildValues.Fakes.ADMOB_APP_ID)
                )
                resValue(
                    BuildValues.Type.STRING,
                    BANNER_AD_UNIT_ID_CALCULATOR.toResourceName(),
                    getSecret(BANNER_AD_UNIT_ID_CALCULATOR, BuildValues.Fakes.BANNER_AD_UNIT_ID)
                )
                resValue(
                    BuildValues.Type.STRING,
                    BANNER_AD_UNIT_ID_SETTINGS.toResourceName(),
                    getSecret(BANNER_AD_UNIT_ID_SETTINGS, BuildValues.Fakes.BANNER_AD_UNIT_ID)
                )
                resValue(
                    BuildValues.Type.STRING,
                    BANNER_AD_UNIT_ID_CURRENCIES.toResourceName(),
                    getSecret(BANNER_AD_UNIT_ID_CURRENCIES, BuildValues.Fakes.BANNER_AD_UNIT_ID)
                )
                resValue(
                    BuildValues.Type.STRING,
                    INTERSTITIAL_AD_ID.toResourceName(),
                    getSecret(INTERSTITIAL_AD_ID, BuildValues.Fakes.INTERSTITIAL_AD_ID)
                )
                resValue(
                    BuildValues.Type.STRING,
                    REWARDED_AD_UNIT_ID.toResourceName(),
                    getSecret(REWARDED_AD_UNIT_ID, BuildValues.Fakes.REWARDED_AD_UNIT_ID)
                )
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
        coreLibraryDesugaring(DESUGARING)
        debugImplementation(LEAK_CANARY)
    }

    implementation(Dependencies.Android.GOOGLE.PLAY_CORE)

    with(Dependencies.Common) {
        implementation(KOTLIN_X_DATE_TIME)
    }

    with(Dependencies.Modules) {
        implementation(project(CLIENT))

        implementation(project(BASE_MOB))
        implementation(project(SCOPE_MOB))
        implementation(project(LOG_MOB))

        implementation(project(BILLING))
        implementation(project(AD))
        implementation(project(ANALYTICS))
    }
}
