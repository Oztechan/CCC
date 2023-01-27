/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

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
                    implementation(kotlinXDateTime)
                    implementation(coroutines)
                    implementation(koinCore)
                }
                Modules.apply {
                    implementation(project(analytics))
                }
                Modules.Submodules.apply {
                    implementation(project(logmob))
                    implementation(project(scopemob))
                    implementation(project(parsermob))
                }
                Modules.Common.Core.apply {
                    implementation(project(infrastructure))
                    implementation(project(model))
                }
                Modules.Common.DataSource.apply {
                    implementation(project(conversion))
                }
                Modules.Client.Core.apply {
                    implementation(project(persistence))
                    implementation(project(shared))
                }
                Modules.Client.DataSource.apply {
                    implementation(project(currency))
                    implementation(project(watcher))
                }
                Modules.Client.Service.apply {
                    implementation(project(backend))
                }
                Modules.Client.ConfigService.apply {
                    implementation(project(ad))
                    implementation(project(review))
                    implementation(project(update))
                }
                Modules.Client.Storage.apply {
                    implementation(project(app))
                    implementation(project(calculator))
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
                libs.android.apply {
                    implementation(androidMaterial)
                    implementation(koinAndroid)
                    implementation(lifecycleViewmodel)
                }
            }
        }
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

@Suppress("UnstableApiUsage")
android {
    ProjectSettings.apply {
        namespace = Modules.Client.self.packageName
        compileSdk = COMPILE_SDK_VERSION

        defaultConfig {
            minSdk = MIN_SDK_VERSION
            targetSdk = TARGET_SDK_VERSION
        }
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}
