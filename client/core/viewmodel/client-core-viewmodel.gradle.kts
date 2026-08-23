plugins {
    libs.plugins.apply {
        alias(kotlinMultiplatform)
        alias(androidKotlinMultiplatformLibrary)
    }
}
kotlin {
    android {
        namespace = Modules.Client.Core.viewModel.packageName
        compileSdk = ProjectSettings.COMPILE_SDK_VERSION
        minSdk = ProjectSettings.MIN_SDK_VERSION
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
                implementation(kermit)
            }
        }
        androidMain.dependencies {
            libs.android.apply {
                implementation(koinAndroid)
                implementation(lifecycleViewmodel)
            }
        }
    }
}
