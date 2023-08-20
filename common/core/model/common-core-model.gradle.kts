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
        val commonTest by getting {
            dependencies {
                libs.common.apply {
                    implementation(test)
                }
            }
        }
    }
}
