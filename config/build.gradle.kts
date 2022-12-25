import Modules.packageName
import Modules.path

plugins {
    @Suppress("DSL_SCOPE_VIOLATION")
    with(libs.plugins) {
        id(androidLib.get().pluginId)
        id(multiplatform.get().pluginId)
        id(kotlinXSerialization.get().pluginId)
    }
}

kotlin {
    android()

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    @Suppress("UNUSED_VARIABLE")
    sourceSets {

        val commonMain by getting {
            dependencies {
                with(libs.common) {
                    implementation(ktorJson)
                    implementation(koinCore)
                }
                implementation(project(Modules.LOGMOB.path))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(project(Modules.TEST.path))
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(libs.android.firebaseRemoteConfig)
            }
        }
        val androidTest by getting

        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }
    }
}

android {
    with(ProjectSettings) {
        namespace = Modules.CONFIG.packageName
        compileSdk = COMPILE_SDK_VERSION

        @Suppress("UnstableApiUsage")
        defaultConfig {
            minSdk = MIN_SDK_VERSION
            targetSdk = TARGET_SDK_VERSION
        }
    }
}
