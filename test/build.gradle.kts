plugins {
    with(Dependencies.Plugins) {
        kotlin(MULTIPLATFORM)
        id(ANDROID_LIB)
    }
}

kotlin {

    android()

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    jvm()

    @Suppress("UNUSED_VARIABLE")
    sourceSets {
        val commonMain by getting {
            dependencies {
                with(Dependencies.Common) {
                    api(kotlin(TEST))
                    api(kotlin(TEST_ANNOTATIONS))
                    implementation(COROUTINES_TEST)
                }
                implementation(project(Modules.LOGMOB.path))
            }
        }
        val commonTest by getting

        val androidMain by getting {
            dependencies {
                api(kotlin(Dependencies.JVM.TEST_JUNIT))
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

        val jvmMain by getting {
            dependencies {
                api(kotlin(Dependencies.JVM.TEST_JUNIT))
            }
        }
        val jvmTest by getting
    }
}

android {
    with(ProjectSettings) {
        compileSdk = COMPILE_SDK_VERSION

        @Suppress("UnstableApiUsage")
        defaultConfig {
            minSdk = MIN_SDK_VERSION
            targetSdk = TARGET_SDK_VERSION
        }

        sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    }
}
