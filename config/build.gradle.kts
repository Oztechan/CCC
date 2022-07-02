plugins {
    with(Dependencies.Plugins) {
        id(ANDROID_LIB)
        kotlin(MULTIPLATFORM)
        id(KOTLIN_X_SERIALIZATION)
    }
}

kotlin {
    android()

    // todo Revert to just ios() when gradle plugin can properly resolve it
    if (System.getenv("SDK_NAME")?.startsWith("iphoneos") == true) {
        iosArm64("ios")
    } else {
        iosX64("ios")
    }

    @Suppress("UNUSED_VARIABLE")
    sourceSets {

        val commonMain by getting {
            dependencies {
                with(Dependencies.Common) {
                    implementation(KTOR_JSON)
                    implementation(LOG_MOB)
                }
            }
        }
        val commonTest by getting

        with(Dependencies.Android) {
            val androidMain by getting {
                dependencies {
                    implementation(FIREBASE_REMOTE_CONFIG)
                }
            }
            val androidTest by getting
        }

        val iosMain by getting
        val iosTest by getting
    }
}

android {
    with(ProjectSettings) {
        compileSdk = COMPILE_SDK_VERSION

        defaultConfig {
            minSdk = MIN_SDK_VERSION
            targetSdk = TARGET_SDK_VERSION
        }

        sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    }
}
