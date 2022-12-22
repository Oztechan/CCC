/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
import com.google.firebase.perf.plugin.FirebasePerfExtension
import config.BuildType
import config.DeviceFlavour
import config.DeviceFlavour.Companion.googleImplementation
import config.Keys

plugins {
    @Suppress("DSL_SCOPE_VIOLATION")
    libs.plugins.apply {
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
    ProjectSettings.apply {
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
            compose = true
        }
        composeOptions {
            kotlinCompilerExtensionVersion = libs.versions.compose.get()
        }
    }

    signingConfigs {
        create(BuildType.release) {
            Keys(project).apply {
                storeFile = file(androidKeyStorePath.value)
                storePassword = androidStorePassword.value
                keyAlias = androidKeyAlias.value
                keyPassword = androidKeyPassword.value
            }
        }
    }

    DeviceFlavour.apply {
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
    libs.apply {
        common.apply {
            implementation(kotlinXDateTime)
        }

        android.apply {
            implementation(composeToolingPreview)
            debugImplementation(composeTooling)
            implementation(material3)
            implementation(androidMaterial)
            implementation(composeActivity)
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

        android.google.apply {
            @Suppress("UnstableApiUsage")
            googleImplementation(playCore)
        }
    }

    Modules.apply {
        implementation(project(CLIENT))
        implementation(project(RES))
        implementation(project(BILLING))
        implementation(project(AD))
        implementation(project(LOGMOB))
        implementation(project(SCOPEMOB))
        implementation(project(BASEMOB))
        implementation(project(ANALYTICS))

        testImplementation(project(TEST))
    }
}
