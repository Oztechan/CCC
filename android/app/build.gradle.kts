/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
import com.google.firebase.perf.plugin.FirebasePerfExtension
import config.BuildType
import config.DeviceFlavour
import config.Keys

plugins {
    @Suppress("DSL_SCOPE_VIOLATION")
    libs.plugins.apply {
        id(androidApp.get().pluginId)
        id(crashlytics.get().pluginId)
        id(googleServices.get().pluginId)
        id(firebasePerPlugin.get().pluginId)
        id(android.get().pluginId)
    }
}

@Suppress("UnstableApiUsage")
android {
    ProjectSettings.apply {
        namespace = Modules.Android.app.packageName
        compileSdk = COMPILE_SDK_VERSION

        defaultConfig {
            minSdk = MIN_SDK_VERSION
            targetSdk = TARGET_SDK_VERSION

            versionCode = getVersionCode(project)
            versionName = getVersionName(project)
        }
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
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
    libs.android.apply {
        implementation(koinAndroid)
        implementation(firebasePer)
        coreLibraryDesugaring(desugaring)
        debugImplementation(leakCanary)
    }

    Modules.Android.Core.apply {
        implementation(project(billing))
        implementation(project(ad))
    }

    Modules.Common.Core.apply {
        implementation(project(database))
        implementation(project(network))
        implementation(project(infrastructure))
    }

    Modules.Common.DataSource.apply {
        implementation(project(conversion))
    }

    implementation(project(Modules.Client.self))

    Modules.Client.Core.apply {
        implementation(project(persistence))
        implementation(project(analytics))
        implementation(project(shared))
    }

    Modules.Client.Storage.apply {
        implementation(project(app))
        implementation(project(calculator))
    }

    Modules.Client.DataSource.apply {
        implementation(project(currency))
        implementation(project(watcher))
    }

    Modules.Client.Service.apply {
        implementation(project(backend))
    }

    Modules.Client.ConfigService.apply {
        implementation(project(ad))
        implementation(project(review))
        implementation(project(update))
    }

    Modules.Client.Repository.apply {
        implementation(project(adControl))
        implementation(project(appConfig))
    }

    Modules.Android.Feature.apply {
        implementation(project(mobile))
        implementation(project(widget))
    }

    Modules.Submodules.apply {
        implementation(project(logmob))
    }
}
