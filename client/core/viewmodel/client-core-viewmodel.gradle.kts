plugins {
    libs.plugins.apply {
        alias(kotlinMultiplatform)
        alias(androidLibrary)
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

android {
    ProjectSettings.apply {
        namespace = Modules.Client.Core.viewModel.packageName
        compileSdk = COMPILE_SDK_VERSION
        defaultConfig.minSdk = MIN_SDK_VERSION

        compileOptions {
            sourceCompatibility = JAVA_VERSION
            targetCompatibility = JAVA_VERSION
        }
    }
}
