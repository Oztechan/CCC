plugins {
    with(Dependencies.Plugins) {
        kotlin(MULTIPLATFORM)
        id(ANDROID_LIB)
        id(MOKO_RESOURCES)
    }
}

version = ProjectSettings.getVersionName(project)

kotlin {
    android()

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "Res"
            isStatic = true
        }
    }

    @Suppress("UNUSED_VARIABLE")
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(Dependencies.Common.MOKO_RESOURCES)
            }
        }
        val commonTest by getting
        val androidMain by getting
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
        compileSdk = COMPILE_SDK_VERSION

        defaultConfig {
            minSdk = MIN_SDK_VERSION
            targetSdk = TARGET_SDK_VERSION
        }

        sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    }
}

multiplatformResources {
    multiplatformResourcesPackage = "${ProjectSettings.PROJECT_ID}.res"
    disableStaticFrameworkWarning = true
}
