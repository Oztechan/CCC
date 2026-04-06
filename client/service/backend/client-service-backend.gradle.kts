plugins {
    libs.plugins.apply {
        alias(kotlinMultiplatform)
        alias(androidKotlinMultiplatformLibrary)
        alias(mokkery)
    }
}

kotlin {
    androidLibrary {
        namespace = Modules.Client.Service.backend.packageName
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
            Modules.Common.Core.apply {
                implementation(project(network))
                implementation(project(model))
                implementation(project(infrastructure))
            }
        }
        commonTest.dependencies {
            libs.common.apply {
                implementation(test)
                implementation(coroutinesTest)
            }
        }
    }
}
