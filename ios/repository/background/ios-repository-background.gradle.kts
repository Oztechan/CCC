plugins {
    libs.plugins.apply {
        alias(kotlinMultiplatform)
        alias(mokkery)
    }
}

kotlin {
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
            implementation(project(Modules.Client.DataSource.watcher))
            implementation(project(Modules.Client.Service.backend))
            implementation(project(Modules.Client.Core.shared))
            implementation(project(Modules.Common.Core.model))
        }
        commonTest.dependencies {
            libs.common.apply {
                implementation(test)
                implementation(coroutinesTest)
            }
        }
    }
}
