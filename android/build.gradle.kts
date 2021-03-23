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
        val props = try {
            Properties().apply { load(project.file("secret.properties").inputStream()) }
        } catch (e: IOException) {
            // keys are private and can not be committed to git
            null
        }

        getByName("release") {
            isMinifyEnabled = false
            resValue(
                Type.string.toResource(),
                Keys.admobAppId.toResource(),
                props.get(Keys.admobAppId.toRelease(), Fakes.admobAppId)
            )
            resValue(
                Type.string.toResource(),
                Keys.bannerAdUnitIdCalculator.toResource(),
                props.get(Keys.bannerAdUnitIdCalculator.toRelease(), Fakes.bannerAdUnitId)
            )
            resValue(
                Type.string.toResource(),
                Keys.bannerAdUnitIdSettings.toResource(),
                props.get(Keys.bannerAdUnitIdSettings.toRelease(), Fakes.bannerAdUnitId)
            )
            resValue(
                Type.string.toResource(),
                Keys.bannerAdUnitIdCurrencies.toResource(),
                props.get(Keys.bannerAdUnitIdCurrencies.toRelease(), Fakes.bannerAdUnitId)
            )
            resValue(
                Type.string.toResource(),
                Keys.interstitialAdId.toResource(),
                props.get(Keys.interstitialAdId.toRelease(), Fakes.interstitialAdId)
            )
            resValue(
                Type.string.toResource(),
                Keys.rewardedAdUnitId.toResource(),
                props.get(Keys.rewardedAdUnitId.toRelease(), Fakes.rewardedAdUnitId)
            )
        }
        getByName("debug") {
            resValue(
                Type.string.toResource(),
                Keys.admobAppId.toResource(),
                props.get(Keys.admobAppId.toDebug(), Fakes.admobAppId)
            )
            resValue(
                Type.string.toResource(),
                Keys.bannerAdUnitIdCalculator.toResource(),
                props.get(Keys.bannerAdUnitIdCalculator.toDebug(), Fakes.bannerAdUnitId)
            )
            resValue(
                Type.string.toResource(),
                Keys.bannerAdUnitIdSettings.toResource(),
                props.get(Keys.bannerAdUnitIdSettings.toDebug(), Fakes.bannerAdUnitId)
            )
            resValue(
                Type.string.toResource(),
                Keys.bannerAdUnitIdCurrencies.toResource(),
                props.get(Keys.bannerAdUnitIdCurrencies.toDebug(), Fakes.bannerAdUnitId)
            )
            resValue(
                Type.string.toResource(),
                Keys.interstitialAdId.toResource(),
                props.get(Keys.interstitialAdId.toDebug(), Fakes.interstitialAdId)
            )
            resValue(
                Type.string.toResource(),
                Keys.rewardedAdUnitId.toResource(),
                props.get(Keys.rewardedAdUnitId.toDebug(), Fakes.rewardedAdUnitId)
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
