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
    val props = project.getSecretProperties()

    signingConfigs {
        create("release") {
            storeFile = file(props.get(Keys.keyStorePath))
            storePassword = props.get(Keys.storePassword)
            keyAlias = props.get(Keys.keyAlias)
            keyPassword = props.get(Keys.keyPassword)
        }
    }

    buildTypes {

        getByName("release") {
            signingConfig = signingConfigs.getByName("release")

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
