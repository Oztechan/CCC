plugins {
    libs.plugins.apply {
        alias(androidLibrary)
        alias(kotlinMultiplatform)
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
                implementation(koinCore)
                implementation(coroutines)
            }

            implementation(project(Modules.Client.Core.remoteConfig))
        }
        commonTest.dependencies {
            implementation(libs.common.test)
        }
    }
}

android {
    ProjectSettings.apply {
        namespace = Modules.Client.ConfigService.update.packageName
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
