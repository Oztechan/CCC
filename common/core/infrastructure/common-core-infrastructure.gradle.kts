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
            libs.common.apply {
                implementation(koinCore)
                implementation(coroutines)
            }
        }
    }
}
