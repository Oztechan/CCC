plugins {
    libs.plugins.apply {
        id(multiplatform.get().pluginId)
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
                implementation(libs.common.koinCore)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(libs.common.test)
            }
        }
        val androidMain by getting {
            dependencies {
                libs.android.apply {
                    implementation(firebaseAnalytics)
                    implementation(rootBeer)
                }
            }
        }
    }
}

android {
    ProjectSettings.apply {
        namespace = Modules.Client.Core.analytics.packageName
        compileSdk = COMPILE_SDK_VERSION
        defaultConfig.minSdk = MIN_SDK_VERSION

        compileOptions {
            sourceCompatibility = JAVA_VERSION
            targetCompatibility = JAVA_VERSION
        }
    }

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
}
