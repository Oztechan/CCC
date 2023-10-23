plugins {
    libs.plugins.apply {
        alias(kotlinMultiplatform)
        id(androidLib.get().pluginId)
    }
}
kotlin {
    @Suppress("OPT_IN_USAGE")
    targetHierarchy.default()

    androidTarget()

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    @Suppress("UNUSED_VARIABLE")
    sourceSets {
        val commonMain by getting {
            dependencies {
                libs.common.apply {
                    implementation(koinCore)
                    implementation(coroutines)
                    implementation(kermit)
                }
            }
        }
        val androidMain by getting {
            dependencies {
                libs.android.apply {
                    implementation(koinAndroid)
                    implementation(lifecycleViewmodel)
                }
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
