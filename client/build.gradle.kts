/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.INT
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

    // todo Revert to just ios() when gradle plugin can properly resolve it
    // todo it is necessary for xcodebuild, find workaround
    if (System.getenv("SDK_NAME")?.startsWith("iphoneos") == true) {
        iosArm64("ios")
    } else {
        iosX64("ios")
    }

    cocoapods {
        summary = "CCC"
        homepage = "https://github.com/CurrencyConverterCalculator/CCC"
        ios.deploymentTarget = "14.0"
        framework {
            baseName = "Client"
        }
    }

    @Suppress("UNUSED_VARIABLE")
    sourceSets {

        all {
            languageSettings.apply {
                optIn("kotlinx.coroutines.FlowPreview")
                optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
            }
        }

        with(Dependencies.Common) {
            val commonMain by getting {
                dependencies {
                    implementation(KOTLIN_X_DATE_TIME)
                    implementation(COROUTINES)
                    implementation(KOIN_CORE)
                    implementation(SCOPE_MOB)
                    implementation(PARSER_MOB)
                    implementation(LOG_MOB)

                    with(Dependencies.Modules) {
                        implementation(project(COMMON))
                        implementation(project(CONFIG))
                    }
                }
            }
            val commonTest by getting {
                dependencies {
                    implementation(kotlin(TEST))
                    implementation(kotlin(TEST_ANNOTATIONS))
                    implementation(MOCKATIVE)
                }
            }
        }

        with(Dependencies.Android) {
            val androidMain by getting {
                dependencies {
                    implementation(ANDROID_MATERIAL)
                    implementation(KOIN_ANDROID)
                    implementation(LIFECYCLE_VIEWMODEL)
                }
            }
            val androidTest by getting
        }

        val iosMain by getting
        val iosTest by getting
    }
}

dependencies {
    ksp(Dependencies.Processors.MOCKATIVE)
}

ksp {
    arg("mockative.stubsUnitByDefault", "true")
}

android {
    with(ProjectSettings) {
        compileSdk = COMPILE_SDK_VERSION

        defaultConfig {
            minSdk = MIN_SDK_VERSION
            targetSdk = TARGET_SDK_VERSION
        }

        // todo needed for android coroutine testing
        testOptions {
            unitTests.isReturnDefaultValues = true
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
        buildConfigField(INT, "versionCode", ProjectSettings.getVersionCode(project).toString())
    }
}
