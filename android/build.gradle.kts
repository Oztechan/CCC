/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
plugins {
    with(Plugins) {
        id(androidApplication)
        id(crashlytics)
        id(googleServices)
        id(safeargs)
        kotlin(android)
    }
}

android {
    with(ProjectSettings) {
        compileSdk = compileSdkVersion

        defaultConfig {
            minSdk = minSdkVersion
            targetSdk = targetSdkVersion

            applicationId = projectId

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
        // todo remove after MissingSuperCall bug fixed
        lint {
            isAbortOnError = false
        }
    }

    signingConfigs {
        create(BuildType.release) {
            with(Keys.Signing) {
                storeFile = file(getSecret(androidKeyStorePath))
                storePassword = getSecret(androidStorePassword)
                keyAlias = getSecret(androidKeyAlias)
                keyPassword = getSecret(androidKeyPassword)
            }
        }
    }

    buildTypes {

        getByName(BuildType.release) {
            signingConfig = signingConfigs.getByName(BuildType.release)
            isMinifyEnabled = false

            with(Keys.Release) {
                resValue(
                    Type.string.toLowerCase(),
                    admobAppId.removeVariant().toLowerCase(),
                    getSecret(admobAppId, Fakes.admobAppId)
                )
                resValue(
                    Type.string.toLowerCase(),
                    bannerAdUnitIdCalculator.removeVariant().toLowerCase(),
                    getSecret(bannerAdUnitIdCalculator, Fakes.bannerAdUnitId)
                )
                resValue(
                    Type.string.toLowerCase(),
                    bannerAdUnitIdSettings.removeVariant().toLowerCase(),
                    getSecret(bannerAdUnitIdSettings, Fakes.bannerAdUnitId)
                )
                resValue(
                    Type.string.toLowerCase(),
                    bannerAdUnitIdCurrencies.removeVariant().toLowerCase(),
                    getSecret(bannerAdUnitIdCurrencies, Fakes.bannerAdUnitId)
                )
                resValue(
                    Type.string.toLowerCase(),
                    interstitialAdId.removeVariant().toLowerCase(),
                    getSecret(interstitialAdId, Fakes.interstitialAdId)
                )
                resValue(
                    Type.string.toLowerCase(),
                    rewardedAdUnitId.removeVariant().toLowerCase(),
                    getSecret(rewardedAdUnitId, Fakes.rewardedAdUnitId)
                )
            }
        }
        getByName(BuildType.debug) {
            with(Keys.Debug) {
                resValue(
                    Type.string.toLowerCase(),
                    admobAppId.removeVariant().toLowerCase(),
                    getSecret(admobAppId, Fakes.admobAppId)
                )
                resValue(
                    Type.string.toLowerCase(),
                    bannerAdUnitIdCalculator.removeVariant().toLowerCase(),
                    getSecret(bannerAdUnitIdCalculator, Fakes.bannerAdUnitId)
                )
                resValue(
                    Type.string.toLowerCase(),
                    bannerAdUnitIdSettings.removeVariant().toLowerCase(),
                    getSecret(bannerAdUnitIdSettings, Fakes.bannerAdUnitId)
                )
                resValue(
                    Type.string.toLowerCase(),
                    bannerAdUnitIdCurrencies.removeVariant().toLowerCase(),
                    getSecret(bannerAdUnitIdCurrencies, Fakes.bannerAdUnitId)
                )
                resValue(
                    Type.string.toLowerCase(),
                    interstitialAdId.removeVariant().toLowerCase(),
                    getSecret(interstitialAdId, Fakes.interstitialAdId)
                )
                resValue(
                    Type.string.toLowerCase(),
                    rewardedAdUnitId.removeVariant().toLowerCase(),
                    getSecret(rewardedAdUnitId, Fakes.rewardedAdUnitId)
                )
            }
        }
    }
}

dependencies {
    with(Dependencies.Android) {
        implementation(androidMaterial)
        implementation(constraintLayout)
        implementation(admob)
        implementation(navigation)
        implementation(playCore)
        implementation(koinAndroid)
        implementation(billing)
        implementation(lifecycleRuntime)
        coreLibraryDesugaring(desugaring)
        debugImplementation(leakCanary)
    }

    with(Dependencies.Common) {
        implementation(dateTime)
    }

    with(Modules) {
        implementation(project(client))

        implementation(project(basemob))
        implementation(project(scopemob))
        implementation(project(logmob))
    }
}
