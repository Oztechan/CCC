/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
import Modules.packageName
import com.google.firebase.perf.plugin.FirebasePerfExtension
import config.BuildType
import config.DeviceFlavour
import config.DeviceFlavour.Companion.googleImplementation
import config.Keys

plugins {
    @Suppress("DSL_SCOPE_VIOLATION")
    with(libs.plugins) {
        id(androidApp.get().pluginId)
        id(crashlytics.get().pluginId)
        id(googleServices.get().pluginId)
        id(firebasePerPlugin.get().pluginId)
        id(safeArgs.get().pluginId)
        id(android.get().pluginId)
    }
}

@Suppress("UnstableApiUsage")
android {
    with(ProjectSettings) {
        namespace = Modules.ANDROID.packageName
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
    with(libs) {
        with(common) {
            implementation(kotlinXDateTime)
        }

        with(android) {
            implementation(androidMaterial)
            implementation(constraintLayout)
            implementation(navigation)
            implementation(koinAndroid)
            implementation(lifecycleRuntime)
            implementation(workRuntime) // android 12 crash fix
            implementation(splashScreen)
            implementation(firebasePer)
            coreLibraryDesugaring(desugaring)
            debugImplementation(leakCanary)
        }

        with(android.google) {
            @Suppress("UnstableApiUsage")
            googleImplementation(playCore)
        }
    }

    with(Modules) {
        implementation(project(CLIENT.path))
        implementation(project(RES.path))
        implementation(project(BILLING.path))
        implementation(project(AD.path))
        implementation(project(LOGMOB.path))
        implementation(project(SCOPEMOB.path))
        implementation(project(BASEMOB.path))
        implementation(project(ANALYTICS.path))

        testImplementation(project(TEST.path))
    }
}
