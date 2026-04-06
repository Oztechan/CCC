plugins {
    libs.plugins.apply {
        alias(kotlinMultiplatform)
        alias(androidKotlinMultiplatformLibrary)
        alias(serialization)
    }
}

kotlin {
    androidLibrary {
        namespace = Modules.Client.Core.remoteConfig.packageName
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
                implementation(ktorJson)
                implementation(kermit)
                implementation(gitliveRemoteConfig)
            }
        }
        commonTest.dependencies {
            implementation(libs.common.test)
        }
        androidMain.dependencies {
            libs.android.apply {
                // Bom is needed for gitlive
                implementation(project.dependencies.platform(firebaseBom))
            }
        }
    }
}
dependencies {
    coreLibraryDesugaring(libs.android.androidDesugaring)
}
