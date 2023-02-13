plugins {
    @Suppress("DSL_SCOPE_VIOLATION")
    libs.plugins.apply {
        id(multiplatform.get().pluginId)
        id(androidLib.get().pluginId)
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
                    implementation(kermit)
                }

                Modules.Common.Core.apply {
                    implementation(project(model))
                }

                Modules.Client.Core.apply {
                    implementation(project(viewModel))
                    implementation(project(shared))
                }

                Modules.Client.DataSource.apply {
                    implementation(project(currency))
                    implementation(project(watcher))
                }

                Modules.Client.Repository.apply {
                    implementation(project(adControl))
                }
            }
        }
        val commonTest by getting {
            dependencies {
                libs.common.apply {
                    implementation(test)
                    implementation(mockative)
                    implementation(coroutinesTest)
                }
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(libs.android.lifecycleViewmodel)
            }
        }
        val androidUnitTest by getting

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
    configurations
        .filter { it.name.startsWith("ksp") && it.name.contains("Test") }
        .forEach {
            add(it.name, libs.processors.mockative)
        }
}

android {
    namespace = Modules.Client.ViewModel.watchers.packageName

    ProjectSettings.apply {
        compileSdk = COMPILE_SDK_VERSION
        defaultConfig.minSdk = MIN_SDK_VERSION
    }
}
