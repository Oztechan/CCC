plugins {
    alias(libs.plugins.kotlinMultiplatform)
}
kotlin {
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    jvm()

    sourceSets {
        commonMain.dependencies {
            implementation(libs.common.kotlinXDateTime)
            implementation(project(Modules.Common.Core.model))
        }
        commonTest.dependencies {
            implementation(libs.common.test)
        }
    }
}
