plugins {
    libs.plugins.apply {
        alias(kotlinMultiplatform)
        alias(androidKotlinMultiplatformLibrary)
        alias(mokkery)
    }
}
kotlin {
    androidLibrary {
        namespace = Modules.Client.ViewModel.settings.packageName
        compileSdk = ProjectSettings.COMPILE_SDK_VERSION
        minSdk = ProjectSettings.MIN_SDK_VERSION
        enableCoreLibraryDesugaring = true
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {
            libs.common.apply {
                implementation(koinCore)
                implementation(coroutines)
                implementation(kotlinXDateTime)
                implementation(kermit)
            }

            Modules.Client.Core.apply {
                implementation(project(viewModel))
                implementation(project(shared))
                implementation(project(analytics))
            }

            Modules.Client.Storage.apply {
                implementation(project(app))
                implementation(project(calculation))
            }

            Modules.Client.DataSource.apply {
                implementation(project(currency))
                implementation(project(watcher))
            }
            Modules.Common.DataSource.apply {
                implementation(project(conversion))
            }
            Modules.Common.Core.apply {
                implementation(project(model))
            }
            Modules.Client.Service.apply {
                implementation(project(backend))
            }
            Modules.Client.Repository.apply {
                implementation(project(adControl))
                implementation(project(appConfig))
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
