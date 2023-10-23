plugins {
    libs.plugins.apply {
        alias(kotlinMultiplatform)
        id(androidLib.get().pluginId)
        alias(ksp)
    }
}
kotlin {
    @Suppress("OPT_IN_USAGE")
    targetHierarchy.default()

    androidTarget()

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
                    implementation(kotlinXDateTime)
                    implementation(kermit)
                }

                Modules.Client.Core.apply {
                    implementation(project(viewModel))
                    implementation(project(shared))
                    implementation(project(analytics))
                }

                Modules.Client.Storage.apply {
                    implementation(project(app))
                    implementation(project(calculation))
                }

                Modules.Client.DataSource.apply {
                    implementation(project(currency))
                    implementation(project(watcher))
                }
                Modules.Common.DataSource.apply {
                    implementation(project(conversion))
                }
                Modules.Common.Core.apply {
                    implementation(project(model))
                }
                Modules.Client.Service.apply {
                    implementation(project(backend))
                }
                Modules.Client.Repository.apply {
                    implementation(project(adControl))
                    implementation(project(appConfig))
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
        namespace = Modules.Client.ViewModel.settings.packageName
        compileSdk = COMPILE_SDK_VERSION
        defaultConfig.minSdk = MIN_SDK_VERSION

        compileOptions {
            sourceCompatibility = JAVA_VERSION
            targetCompatibility = JAVA_VERSION
        }
    }
}
