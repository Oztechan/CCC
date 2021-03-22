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
        compileSdkVersion(projectCompileSdkVersion)

        defaultConfig {
            minSdkVersion(projectMinSdkVersion)
            targetSdkVersion(projectTargetSdkVersion)

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
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            resValue(
                Type.string.toResource(),
                Keys.admobAppId.toResource(),
                Values.Release.admobAppId
            )
            resValue(
                Type.string.toResource(),
                Keys.bannerAdUnitIdCalculator.toResource(),
                Values.Release.bannerAdUnitIdCalculator
            )
            resValue(
                Type.string.toResource(),
                Keys.bannerAdUnitIdSettings.toResource(),
                Values.Release.bannerAdUnitIdSettings
            )
            resValue(
                Type.string.toResource(),
                Keys.bannerAdUnitIdCurrencies.toResource(),
                Values.Release.bannerAdUnitIdCurrencies
            )
            resValue(
                Type.string.toResource(),
                Keys.interstitialAdId.toResource(),
                Values.Release.interstitialAdId
            )
            resValue(
                Type.string.toResource(),
                Keys.rewardedAdUnitId.toResource(),
                Values.Release.rewardedAdUnitId
            )
        }
        getByName("debug") {
            resValue(
                Type.string.toResource(),
                Keys.admobAppId.toResource(),
                Values.Debug.admobAppId
            )
            resValue(
                Type.string.toResource(),
                Keys.bannerAdUnitIdCalculator.toResource(),
                Values.Debug.bannerAdUnitIdCalculator
            )
            resValue(
                Type.string.toResource(),
                Keys.bannerAdUnitIdSettings.toResource(),
                Values.Debug.bannerAdUnitIdSettings
            )
            resValue(
                Type.string.toResource(),
                Keys.bannerAdUnitIdCurrencies.toResource(),
                Values.Debug.bannerAdUnitIdCurrencies
            )
            resValue(
                Type.string.toResource(),
                Keys.interstitialAdId.toResource(),
                Values.Debug.interstitialAdId
            )
            resValue(
                Type.string.toResource(),
                Keys.rewardedAdUnitId.toResource(),
                Values.Debug.rewardedAdUnitId
            )
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
        coreLibraryDesugaring(desugaring)
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
