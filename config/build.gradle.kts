import Modules.packageName
import Modules.path

plugins {
    with(Dependencies.Plugins) {
        id(ANDROID_LIB)
        kotlin(MULTIPLATFORM)
        id(KOTLIN_X_SERIALIZATION)
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
                with(Dependencies.Common) {
                    implementation(KTOR_JSON)
                    implementation(KOIN_CORE)
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
                implementation(Dependencies.Android.FIREBASE_REMOTE_CONFIG)
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
