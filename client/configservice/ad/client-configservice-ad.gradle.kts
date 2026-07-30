plugins {
    libs.plugins.apply {
        alias(androidKotlinMultiplatformLibrary)
        alias(kotlinMultiplatform)
    }
}

kotlin {
    android {
        namespace = Modules.Client.ConfigService.ad.packageName
        compileSdk = ProjectSettings.COMPILE_SDK_VERSION
        minSdk = ProjectSettings.MIN_SDK_VERSION
        enableCoreLibraryDesugaring = true
        withHostTest {}
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {
            libs.common.apply {
                implementation(koinCore)
                implementation(coroutines)
            }

            implementation(project(Modules.Client.Core.remoteConfig))
        }

        commonTest.dependencies {
            implementation(libs.common.test)
        }
    }
}
dependencies {
    coreLibraryDesugaring(libs.android.androidDesugaring)
}

// GitLive Firebase (via :client:core:remoteconfig) injects `-framework FirebaseCore` into the iOS
// test binaries, but that framework only exists inside an Xcode/SwiftPM build - so a standalone
// `./gradlew build` fails to link it (ld: framework 'FirebaseCore' not found). These are pure-logic
// tests already covered by the Android host tests, so skip the iOS unit-test binaries.
listOf("IosX64", "IosSimulatorArm64").forEach { target ->
    tasks.named("linkDebugTest$target") { enabled = false }
    tasks.named("${target.replaceFirstChar(Char::lowercaseChar)}Test") { enabled = false }
}
