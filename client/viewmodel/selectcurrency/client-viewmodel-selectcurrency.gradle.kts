plugins {
    libs.plugins.apply {
        alias(kotlinMultiplatform)
        alias(androidKotlinMultiplatformLibrary)
        alias(mokkery)
    }
}
kotlin {
    android {
        namespace = Modules.Client.ViewModel.selectCurrency.packageName
        compileSdk = ProjectSettings.COMPILE_SDK_VERSION
        minSdk = ProjectSettings.MIN_SDK_VERSION
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
            }

            Modules.Client.DataSource.apply {
                implementation(project(currency))
                implementation(project(watcher))
            }
            Modules.Client.Storage.apply {
                implementation(project(calculation))
            }

            Modules.Common.Core.apply {
                implementation(project(model))
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
