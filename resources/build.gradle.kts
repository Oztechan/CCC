plugins {
    with(Dependencies.Plugins) {
        kotlin(MULTIPLATFORM)
        kotlin(COCOAPODS)
        id(ANDROID_LIB)
        id(MOKO_RESOURCES)
    }
}

version = ProjectSettings.getVersionName(project)

kotlin {
    android()

    // todo Revert to just ios() when gradle plugin can properly resolve it
    // todo it is necessary for xcodebuild, find workaround
    if (System.getenv("SDK_NAME")?.startsWith("iphoneos") == true) {
        iosArm64("ios")
    } else {
        iosX64("ios")
    }

    cocoapods {
        summary = "CCC"
        homepage = "https://github.com/CurrencyConverterCalculator/CCC"
        ios.deploymentTarget = "14.0"
        framework {
            baseName = "Resources"
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

multiplatformResources {
    multiplatformResourcesPackage = "${ProjectSettings.PROJECT_ID}.resources"
    disableStaticFrameworkWarning = true
}
