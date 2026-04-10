plugins {
    libs.plugins.apply {
        alias(kotlinMultiplatform)
        alias(androidKotlinMultiplatformLibrary)
    }
}

kotlin {
    android {
        namespace = Modules.Client.Core.analytics.packageName
        compileSdk = ProjectSettings.COMPILE_SDK_VERSION
        minSdk = ProjectSettings.MIN_SDK_VERSION
        withHostTest {}
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {
            implementation(libs.common.koinCore)
            implementation(libs.common.gitliveAnalytics)
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
