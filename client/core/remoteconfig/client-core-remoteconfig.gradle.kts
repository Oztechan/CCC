plugins {
    libs.plugins.apply {
        id(androidLib.get().pluginId)
        id(multiplatform.get().pluginId)
        alias(serialization)
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
                    implementation(ktorJson)
                    implementation(kermit)
                }
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(libs.common.test)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(libs.android.firebaseRemoteConfig)
            }
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
        }
    }
}
