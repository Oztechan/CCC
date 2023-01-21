/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    @Suppress("DSL_SCOPE_VIOLATION")
    libs.plugins.apply {
        id(multiplatform.get().pluginId)
        id(kotlinXSerialization.get().pluginId)
        id(androidLib.get().pluginId)
        alias(ksp)
    }
}

kotlin {

    android()

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    jvm()

    @Suppress("UNUSED_VARIABLE")
    sourceSets {

        val commonMain by getting {
            dependencies {
                libs.common.apply {
                    implementation(koinCore)
                    implementation(ktorLogging)
                    implementation(ktorJson)
                }
                Modules.Common.Core.apply {
                    implementation(project(database))
                    implementation(project(network))
                    implementation(project(infrastructure))
                    implementation(project(model))
                }
                implementation(project(Modules.Submodules.logmob))
            }
        }
        val commonTest by getting {
            dependencies {
                libs.common.apply {
                    implementation(mockative)
                    implementation(coroutinesTest)
                }
                implementation(project(Modules.test))
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

        val jvmMain by getting
        val jvmTest by getting
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
        namespace = Modules.common.packageName
        compileSdk = COMPILE_SDK_VERSION

        @Suppress("UnstableApiUsage")
        defaultConfig {
            minSdk = MIN_SDK_VERSION
            targetSdk = TARGET_SDK_VERSION
        }
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions { jvmTarget = JavaVersion.VERSION_1_8.toString() }
}
