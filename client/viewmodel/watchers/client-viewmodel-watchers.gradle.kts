plugins {
    libs.plugins.apply {
        alias(kotlinMultiplatform)
        alias(androidKotlinMultiplatformLibrary)
        alias(mokkery)
    }
}
kotlin {
    androidLibrary {
        namespace = Modules.Client.ViewModel.watchers.packageName
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
                implementation(kermit)
            }

            Modules.Common.Core.apply {
                implementation(project(model))
            }

            Modules.Client.Core.apply {
                implementation(project(analytics))
                implementation(project(viewModel))
                implementation(project(shared))
            }

            Modules.Client.DataSource.apply {
                implementation(project(currency))
                implementation(project(watcher))
            }

            Modules.Client.Repository.apply {
                implementation(project(adControl))
            }
        }
        commonTest.dependencies {
            libs.common.apply {
                implementation(test)
                implementation(coroutinesTest)
            }
        }
        androidMain.dependencies {
            implementation(libs.android.lifecycleViewmodel)
        }
    }
}
dependencies {
    coreLibraryDesugaring(libs.android.androidDesugaring)
}
