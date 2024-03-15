plugins {
    libs.plugins.apply {
        alias(kotlinMultiplatform)
        alias(androidLibrary)
        alias(ksp)
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
                implementation(kermit)
            }

            Modules.Client.Core.apply {
                implementation(project(viewModel))
                implementation(project(shared))
                implementation(project(analytics))
            }
            Modules.Client.Storage.apply {
                implementation(project(app))
            }
            Modules.Client.Repository.apply {
                implementation(project(appConfig))
                implementation(project(adControl))
            }
            Modules.Client.ConfigService.apply {
                implementation(project(ad))
                implementation(project(review))
            }
        }
        commonTest.dependencies {
            libs.common.apply {
                implementation(test)
                implementation(mockative)
                implementation(coroutinesTest)
            }
        }
        androidMain.dependencies {
            implementation(libs.android.lifecycleViewmodel)
        }
    }
}

dependencies {
    configurations
        .filter { it.name.startsWith("ksp") && it.name.contains("Test") }
        .forEach {
            add(it.name, libs.processors.mockative)
        }
}

android {
    ProjectSettings.apply {
        namespace = Modules.Client.ViewModel.main.packageName
        compileSdk = COMPILE_SDK_VERSION
        defaultConfig.minSdk = MIN_SDK_VERSION

        compileOptions {
            sourceCompatibility = JAVA_VERSION
            targetCompatibility = JAVA_VERSION
        }
    }
}
