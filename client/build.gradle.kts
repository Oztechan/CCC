/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    with(Dependencies.Plugins) {
        kotlin(MULTIPLATFORM)
        kotlin(COCOAPODS)
        id(ANDROID_LIB)
        id(SQL_DELIGHT)
        id(MOKO_RESOURCES)
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

                    with(Dependencies.Modules) {
                        implementation(project(COMMON))
                        implementation(project(PARSER_MOB))
                        implementation(project(SCOPE_MOB))
                        implementation(project(LOG_MOB))
                    }
                }
            }
            val commonTest by getting {
                dependencies {
                    implementation(kotlin(TEST))
                    implementation(kotlin(TEST_ANNOTATIONS))
                }
            }
        }

        val mobileMain by creating {
            dependencies {
                dependsOn(commonMain.get())
                implementation(Dependencies.Common.MOKO_RESOURCES)
            }
        }

        with(Dependencies.Android) {
            val androidMain by getting {
                dependencies {
                    dependsOn(mobileMain)
                    implementation(ANDROID_MATERIAL)
                    implementation(KOIN_ANDROID)
                    implementation(LIFECYCLE_VIEWMODEL)
                }
            }
            val androidTest by getting {
                dependencies {
                    implementation(kotlin(Dependencies.JVM.TEST_J_UNIT))
                }
            }
        }

        val iosMain by getting {
            dependencies {
                dependsOn(mobileMain)
            }
        }
        val iosTest by getting
    }
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

    with(Build.Flavor) {
        flavorDimensions.addAll(listOf(getFlavorName()))

        productFlavors {
            create(GOOGLE) {
                dimension = getFlavorName()
            }

            create(HUAWEI) {
                dimension = getFlavorName()
            }
        }
    }
}

multiplatformResources {
    multiplatformResourcesPackage = "${ProjectSettings.PACKAGE_NAME}.client"
    multiplatformResourcesSourceSet = "mobileMain"
    disableStaticFrameworkWarning = true
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}
