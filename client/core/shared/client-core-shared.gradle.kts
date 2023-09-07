plugins {
    id(libs.plugins.multiplatform.get().pluginId)
}
kotlin {
    @Suppress("OPT_IN_USAGE")
    targetHierarchy.default()

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    jvm()

    @Suppress("UNUSED_VARIABLE")
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.common.kotlinXDateTime)
                implementation(project(Modules.Common.Core.model))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(libs.common.test)
            }
        }
    }
}
