plugins {
    libs.plugins.apply {
        alias(androidKotlinMultiplatformLibrary)
        alias(kotlinMultiplatform)
    }
}

kotlin {
    androidLibrary {
        namespace = Modules.Client.ConfigService.review.packageName
        compileSdk = ProjectSettings.COMPILE_SDK_VERSION
        minSdk = ProjectSettings.MIN_SDK_VERSION
        enableCoreLibraryDesugaring = true
        withHostTest {}
    }

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
dependencies {
    coreLibraryDesugaring(libs.android.androidDesugaring)
}
