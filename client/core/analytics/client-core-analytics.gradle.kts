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
            implementation(libs.common.koinCore)
        }
        commonTest.dependencies {
            implementation(libs.common.test)
        }
        androidMain.dependencies {
            implementation(libs.android.firebaseAnalytics)
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
