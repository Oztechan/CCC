plugins {
    libs.plugins.apply {
        alias(kotlinMultiplatform)
        alias(androidKotlinMultiplatformLibrary)
        alias(mokkery)
    }
}
kotlin {
    android {
        namespace = Modules.Client.ViewModel.calculator.packageName
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

            Modules.Client.Core.apply {
                implementation(project(viewModel))
                implementation(project(shared))
                implementation(project(analytics))
            }
            Modules.Client.DataSource.apply {
                implementation(project(currency))
            }
            Modules.Client.Storage.apply {
                implementation(project(calculation))
            }
            Modules.Client.Repository.apply {
                implementation(project(adControl))
            }
            Modules.Client.Service.apply {
                implementation(project(backend))
            }
            Modules.Common.Core.apply {
                implementation(project(model))
            }
            Modules.Common.DataSource.apply {
                implementation(project(conversion))
            }
            Submodules.apply {
                implementation(scopemob)
                implementation(parsermob)
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
