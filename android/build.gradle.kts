/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
import com.google.firebase.perf.plugin.FirebasePerfExtension
import config.BuildType
import config.DeviceFlavour
import config.DeviceFlavour.Companion.googleImplementation
import config.Keys

plugins {
    with(Dependencies.Plugins) {
        id(ANDROID_APP)
        id(CRASHLYTICS)
        id(GOOGLE_SERVICES)
        id(FIREBASE_PER_PLUGIN)
        id(SAFE_ARGS)
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
        }

        getByName(BuildType.debug) {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"

            extensions.getByName<FirebasePerfExtension>("FirebasePerformance").setInstrumentationEnabled(false)
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
        implementation(FIREBASE_PER)
        coreLibraryDesugaring(DESUGARING)
        debugImplementation(LEAK_CANARY)
    }

    googleImplementation(Dependencies.Android.GOOGLE.PLAY_CORE)

    with(Dependencies.Common) {
        implementation(KOTLIN_X_DATE_TIME)
    }

    with(Dependencies.Modules) {
        implementation(project(CLIENT))
        implementation(project(RES))
        implementation(project(BILLING))
        implementation(project(AD))
        implementation(project(LOG))
        implementation(project(UTIL))
        implementation(project(BASE))
    }

    testImplementation(project(Dependencies.Modules.TEST))
}
