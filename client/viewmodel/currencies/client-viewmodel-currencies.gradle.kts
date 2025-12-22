plugins {
    libs.plugins.apply {
        alias(kotlinMultiplatform)
        alias(androidLibrary)
        alias(mokkery)
    }
}
kotlin {
    @Suppress("Deprecation")
    androidTarget()

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
                implementation(project(analytics))
                implementation(project(shared))
            }
            Modules.Client.DataSource.apply {
                implementation(project(currency))
            }
            Modules.Client.Storage.apply {
                implementation(project(app))
                implementation(project(calculation))
            }
            Modules.Client.Repository.apply {
                implementation(project(adControl))
            }
            Modules.Common.Core.apply {
                implementation(project(model))
            }
            Submodules.apply {
                implementation(scopemob)
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
        namespace = Modules.Client.ViewModel.currencies.packageName
        compileSdk = COMPILE_SDK_VERSION
        defaultConfig.minSdk = MIN_SDK_VERSION

        compileOptions {
            sourceCompatibility = JAVA_VERSION
            targetCompatibility = JAVA_VERSION
            // needed for gitlive remoteconfig, we have it in app module though
            isCoreLibraryDesugaringEnabled = true
        }

        dependencies {
            coreLibraryDesugaring(libs.android.androidDesugaring)
        }
    }
}
