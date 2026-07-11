plugins {
    libs.plugins.apply {
        alias(kotlinMultiplatform)
        alias(androidKotlinMultiplatformLibrary)
        alias(mokkery)
    }
}
kotlin {
    android {
        namespace = Modules.Client.ViewModel.premium.packageName
        compileSdk = ProjectSettings.COMPILE_SDK_VERSION
        minSdk = ProjectSettings.MIN_SDK_VERSION
        withHostTest {}
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {
            libs.submob.apply {
                implementation(scopemob)
            }

            libs.common.apply {
                implementation(koinCore)
                implementation(kermit)
                implementation(coroutines)
                implementation(kotlinXDateTime)
            }

            Modules.Client.Core.apply {
                implementation(project(viewModel))
                implementation(project(shared))
            }

            Modules.Client.Storage.apply {
                implementation(project(app))
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
