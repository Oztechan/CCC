plugins {
    libs.plugins.apply {
        alias(androidKotlinMultiplatformLibrary)
        alias(kotlinMultiplatform)
        alias(mokkery)
    }
}

kotlin {
    androidLibrary {
        namespace = Modules.Client.Repository.adControl.packageName
        compileSdk = ProjectSettings.COMPILE_SDK_VERSION
        minSdk = ProjectSettings.MIN_SDK_VERSION
        enableCoreLibraryDesugaring = true
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {
            implementation(libs.common.koinCore)
            implementation(project(Modules.Client.Storage.app))
            implementation(project(Modules.Client.ConfigService.ad))
            implementation(project(Modules.Client.Core.shared))
        }
        commonTest.dependencies {
            implementation(libs.common.test)
        }
    }
}
dependencies {
    coreLibraryDesugaring(libs.android.androidDesugaring)
}
