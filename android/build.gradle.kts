/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
plugins {
    with(Plugins) {
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
        create(BuildType.RELEASE) {
            with(Keys.Signing) {
                storeFile = file(getSecret(ANDROID_KEY_STORE_PATH))
                storePassword = getSecret(ANDROID_STORE_PASSWORD)
                keyAlias = getSecret(ANDROID_KEY_ALIAS)
                keyPassword = getSecret(ANDROID_KEY_PASSWORD)
            }
        }
    }

    buildTypes {

        getByName(BuildType.RELEASE) {
            signingConfig = signingConfigs.getByName(BuildType.RELEASE)
            isMinifyEnabled = false

            with(Keys.Release) {
                resValue(
                    Type.STRING.toLowerCase(),
                    ADMOB_APP_ID.removeVariant().toLowerCase(),
                    getSecret(ADMOB_APP_ID, Fakes.ADMOB_APP_ID)
                )
                resValue(
                    Type.STRING.toLowerCase(),
                    BANNER_AD_UNIT_ID_CALCULATOR.removeVariant().toLowerCase(),
                    getSecret(BANNER_AD_UNIT_ID_CALCULATOR, Fakes.BANNER_AD_UNIT_ID)
                )
                resValue(
                    Type.STRING.toLowerCase(),
                    BANNER_AD_UNIT_ID_SETTINGS.removeVariant().toLowerCase(),
                    getSecret(BANNER_AD_UNIT_ID_SETTINGS, Fakes.BANNER_AD_UNIT_ID)
                )
                resValue(
                    Type.STRING.toLowerCase(),
                    BANNER_AD_UNIT_ID_CURRENCIES.removeVariant().toLowerCase(),
                    getSecret(BANNER_AD_UNIT_ID_CURRENCIES, Fakes.BANNER_AD_UNIT_ID)
                )
                resValue(
                    Type.STRING.toLowerCase(),
                    INTERSTITIAL_AD_ID.removeVariant().toLowerCase(),
                    getSecret(INTERSTITIAL_AD_ID, Fakes.INTERSTITIAL_AD_ID)
                )
                resValue(
                    Type.STRING.toLowerCase(),
                    REWARDED_AD_UNIT_ID.removeVariant().toLowerCase(),
                    getSecret(REWARDED_AD_UNIT_ID, Fakes.REWARDED_AD_UNIT_ID)
                )
            }
        }
        getByName(BuildType.DEBUG) {
            with(Keys.Debug) {
                resValue(
                    Type.STRING.toLowerCase(),
                    ADMOB_APP_ID.removeVariant().toLowerCase(),
                    getSecret(ADMOB_APP_ID, Fakes.ADMOB_APP_ID)
                )
                resValue(
                    Type.STRING.toLowerCase(),
                    BANNER_AD_UNIT_ID_CALCULATOR.removeVariant().toLowerCase(),
                    getSecret(BANNER_AD_UNIT_ID_CALCULATOR, Fakes.BANNER_AD_UNIT_ID)
                )
                resValue(
                    Type.STRING.toLowerCase(),
                    BANNER_AD_UNIT_ID_SETTINGS.removeVariant().toLowerCase(),
                    getSecret(BANNER_AD_UNIT_ID_SETTINGS, Fakes.BANNER_AD_UNIT_ID)
                )
                resValue(
                    Type.STRING.toLowerCase(),
                    BANNER_AD_UNIT_ID_CURRENCIES.removeVariant().toLowerCase(),
                    getSecret(BANNER_AD_UNIT_ID_CURRENCIES, Fakes.BANNER_AD_UNIT_ID)
                )
                resValue(
                    Type.STRING.toLowerCase(),
                    INTERSTITIAL_AD_ID.removeVariant().toLowerCase(),
                    getSecret(INTERSTITIAL_AD_ID, Fakes.INTERSTITIAL_AD_ID)
                )
                resValue(
                    Type.STRING.toLowerCase(),
                    REWARDED_AD_UNIT_ID.removeVariant().toLowerCase(),
                    getSecret(REWARDED_AD_UNIT_ID, Fakes.REWARDED_AD_UNIT_ID)
                )
            }
        }
    }
}

dependencies {
    with(Dependencies.Android) {
        implementation(ANDROID_MATERIAL)
        implementation(CONSTRAINT_LAYOUT)
        implementation(ADMOB)
        implementation(NAVIGATION)
        implementation(PLAY_CORE)
        implementation(KOIN_ANDROID)
        implementation(LIFECYCLE_RUNTIME)
        coreLibraryDesugaring(DESUGARING)
        debugImplementation(LEAK_CANARY)
    }

    with(Dependencies.Common) {
        implementation(KOTLIN_X_DATE_TIME)
    }

    with(Modules) {
        implementation(project(CLIENT))

        implementation(project(BASE_MOB))
        implementation(project(SCOPE_MOB))
        implementation(project(LOG_MOB))

        implementation(project(BILLING))
    }
}
