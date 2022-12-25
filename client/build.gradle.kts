/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

import Modules.packageName
import Modules.path
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.INT
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import com.codingfeline.buildkonfig.gradle.BuildKonfigExtension
import config.DeviceFlavour
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    @Suppress("DSL_SCOPE_VIOLATION")
    with(libs.plugins) {
        id(multiplatform.get().pluginId)
        id(androidLib.get().pluginId)
        id(sqlDelight.get().pluginId)
        id(buildKonfig.get().pluginId)
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
                with(libs.common) {
                    implementation(kotlinXDateTime)
                    implementation(coroutines)
                    implementation(koinCore)
                    implementation(multiplatformSettings)
                }
                with(Modules) {
                    implementation(project(COMMON.path))
                    implementation(project(CONFIG.path))
                    implementation(project(LOGMOB.path))
                    implementation(project(SCOPEMOB.path))
                    implementation(project(PARSERMOB.path))
                    implementation(project(ANALYTICS.path))
                }
            }
        }
        val commonTest by getting {
            dependencies {
                with(libs.common) {
                    implementation(mockative)
                    implementation(coroutinesTest)
                }
                implementation(project(Modules.TEST.path))
            }
        }

        val androidMain by getting {
            dependencies {
                with(libs.android) {
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
    with(ProjectSettings) {
        namespace = Modules.CLIENT.packageName
        compileSdk = COMPILE_SDK_VERSION

        defaultConfig {
            minSdk = MIN_SDK_VERSION
            targetSdk = TARGET_SDK_VERSION
        }
    }

    with(DeviceFlavour) {
        flavorDimensions.addAll(listOf(flavorDimension))

        productFlavors {
            create(google) {
                dimension = flavorDimension
            }

            create(huawei) {
                dimension = flavorDimension
            }
        }
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}

configure<BuildKonfigExtension> {
    packageName = Modules.CLIENT.packageName

    defaultConfigs {
        buildConfigField(INT, "versionCode", ProjectSettings.getVersionCode(project).toString(), const = true)
        buildConfigField(STRING, "versionName", ProjectSettings.getVersionName(project), const = true)
    }
}
