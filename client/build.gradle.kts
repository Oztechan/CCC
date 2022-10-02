/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.INT
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import com.codingfeline.buildkonfig.gradle.BuildKonfigExtension
import config.DeviceFlavour
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    with(Dependencies.Plugins) {
        kotlin(MULTIPLATFORM)
        kotlin(COCOAPODS)
        id(ANDROID_LIB)
        id(SQL_DELIGHT)
        id(BUILD_KONFIG)
        id(KSP) version (Versions.KSP)
    }
}

version = ProjectSettings.getVersionName(project)

kotlin {
    android()

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        summary = "CCC"
        homepage = "https://github.com/CurrencyConverterCalculator/CCC"
        ios.deploymentTarget = "14.0"
        framework {
            baseName = "Client"
            export(project(Dependencies.Modules.ANALYTICS))
        }
    }

    @Suppress("UNUSED_VARIABLE")
    sourceSets {
        val commonMain by getting {
            dependencies {
                with(Dependencies.Common) {
                    implementation(KOTLIN_X_DATE_TIME)
                    implementation(COROUTINES)
                    implementation(KOIN_CORE)
                    implementation(PARSER_MOB)
                }
                with(Dependencies.Modules) {
                    implementation(project(COMMON))
                    implementation(project(CONFIG))
                    implementation(project(LOG))
                    implementation(project(SCOPE))
                    api(project(ANALYTICS))
                }
            }
        }
        val commonTest by getting {
            dependencies {
                with(Dependencies.Common) {
                    implementation(MOCKATIVE)
                    implementation(COROUTINES_TEST)
                }
                implementation(project(Dependencies.Modules.TEST))
            }
        }

        val androidMain by getting {
            dependencies {
                with(Dependencies.Android) {
                    implementation(ANDROID_MATERIAL)
                    implementation(KOIN_ANDROID)
                    implementation(LIFECYCLE_VIEWMODEL)
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
    ksp(Dependencies.Processors.MOCKATIVE)
}

ksp {
    arg("mockative.stubsUnitByDefault", "true")
}

@Suppress("UnstableApiUsage")
android {
    with(ProjectSettings) {
        compileSdk = COMPILE_SDK_VERSION

        defaultConfig {
            minSdk = MIN_SDK_VERSION
            targetSdk = TARGET_SDK_VERSION
        }

        sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
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
    packageName = "${ProjectSettings.PROJECT_ID}.client"

    defaultConfigs {
        buildConfigField(INT, "versionCode", ProjectSettings.getVersionCode(project).toString(), const = true)
        buildConfigField(STRING, "versionName", ProjectSettings.getVersionName(project), const = true)
    }
}
