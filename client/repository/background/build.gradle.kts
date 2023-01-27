plugins {
    @Suppress("DSL_SCOPE_VIOLATION")
    libs.plugins.apply {
        id(androidLib.get().pluginId)
        id(multiplatform.get().pluginId)
        alias(ksp)
    }
}

kotlin {
    android()

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    @Suppress("UNUSED_VARIABLE")
    sourceSets {

        val commonMain by getting {
            dependencies {
                libs.common.apply {
                    implementation(koinCore)
                    implementation(coroutines)
                }
                implementation(project(Modules.Client.DataSource.watcher))
                implementation(project(Modules.Client.Service.backend))
                implementation(project(Modules.Client.Core.shared))
                implementation(project(Modules.Common.Core.model))
                implementation(project(Modules.Submodules.logmob))
            }
        }
        val commonTest by getting {
            dependencies {
                libs.common.apply {
                    implementation(test)
                    implementation(coroutinesTest)
                    implementation(mockative)
                }
            }
        }

        val androidMain by getting
        val androidTest by getting

        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }
    }
}

dependencies {
    ksp(libs.processors.mockative)
}

ksp {
    arg("mockative.stubsUnitByDefault", "true")
}

android {
    ProjectSettings.apply {
        namespace = Modules.Client.Repository.background.packageName
        compileSdk = COMPILE_SDK_VERSION

        @Suppress("UnstableApiUsage")
        defaultConfig {
            minSdk = MIN_SDK_VERSION
            targetSdk = TARGET_SDK_VERSION
        }
    }
}
