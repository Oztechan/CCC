plugins {
    libs.plugins.apply {
        alias(kotlinMultiplatform)
        alias(androidLibrary)
        alias(serialization)
    }
}

kotlin {
    androidTarget()

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {
            libs.common.apply {
                implementation(ktorJson)
                implementation(kermit)
            }
        }
        commonTest.dependencies {
            implementation(libs.common.test)
        }
        androidMain.dependencies {
            implementation(libs.android.firebaseRemoteConfig)
        }
    }
}

android {
    ProjectSettings.apply {
        namespace = Modules.Client.Core.remoteConfig.packageName
        compileSdk = COMPILE_SDK_VERSION
        defaultConfig.minSdk = MIN_SDK_VERSION

        compileOptions {
            sourceCompatibility = JAVA_VERSION
            targetCompatibility = JAVA_VERSION
            // needed for gitlive remoteconfig, we have it in app module though
            isCoreLibraryDesugaringEnabled = true
        }

        dependencies {
            coreLibraryDesugaring(libs.android.androidDesugaring)
        }
    }
}
