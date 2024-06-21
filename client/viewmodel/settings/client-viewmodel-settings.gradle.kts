plugins {
    libs.plugins.apply {
        alias(kotlinMultiplatform)
        alias(androidLibrary)
        alias(mokkery)
    }
}
kotlin {
    androidTarget()

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

android {
    ProjectSettings.apply {
        namespace = Modules.Client.ViewModel.settings.packageName
        compileSdk = COMPILE_SDK_VERSION
        defaultConfig.minSdk = MIN_SDK_VERSION

        compileOptions {
            sourceCompatibility = JAVA_VERSION
            targetCompatibility = JAVA_VERSION
        }
    }
}
