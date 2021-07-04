/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    with(Plugins) {
        kotlin(multiplatform)
        kotlin(cocoapods)
        id(androidLibrary)
        id(sqldelight)
        id(mokoResources)
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

    // todo enable when implementation start
    // jvm()

    // todo enable when implementation start
    // js { browser { binaries.executable()testTask { enabled = false } } }


    cocoapods {
        summary = "CCC"
        homepage = "https://github.com/CurrencyConverterCalculator/CCC"
        frameworkName = "Client"
        ios.deploymentTarget = "14.0"
    }

    @Suppress("UNUSED_VARIABLE")
    sourceSets {

        all {
            languageSettings.apply {
                useExperimentalAnnotation("kotlinx.coroutines.FlowPreview")
                useExperimentalAnnotation("kotlinx.coroutines.ExperimentalCoroutinesApi")
            }
        }

        with(Dependencies.Common) {
            val commonMain by getting {
                dependencies {
                    implementation(dateTime)
                    implementation(coroutines)
                    implementation(koinCore)

                    with(Modules) {
                        implementation(project(common))
                        implementation(project(parsermob))
                        implementation(project(scopemob))
                        implementation(project(logmob))
                    }
                }
            }
            val commonTest by getting {
                dependencies {
                    implementation(kotlin(test))
                    implementation(kotlin(testAnnotations))
                }
            }
        }

        val mobileMain by creating {
            dependencies {
                dependsOn(commonMain.get())
                implementation(Dependencies.Common.mokoResources)
            }
        }

        with(Dependencies.Android) {
            val androidMain by getting {
                dependencies {
                    dependsOn(mobileMain)
                    implementation(androidMaterial)
                    implementation(koinAndroid)
                    implementation(lifecycleViewModel)
                }
            }
            val androidTest by getting {
                dependencies {
                    implementation(kotlin(Dependencies.JVM.testJUnit))
                }
            }
        }

        val iosMain by getting {
            dependencies {
                dependsOn(mobileMain)
            }
        }
        val iosTest by getting

        // todo enable when implementation start
        // with(Dependencies.JVM) { val jvmMain by getting val jvmTest by getting { dependencies { implementation(kotlin(testJUnit)) } } }

        // todo enable when implementation start
        // with(Dependencies.JS) { val jsMain by getting val jsTest by getting { dependencies { implementation(kotlin(test)) } } }
    }
}

android {
    with(ProjectSettings) {
        compileSdk = compileSdkVersion

        defaultConfig {
            minSdk = minSdkVersion
            targetSdk = targetSdkVersion
        }

        // todo needed for android coroutine testing
        testOptions {
            unitTests.isReturnDefaultValues = true
        }

        sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    }
}

multiplatformResources {
    multiplatformResourcesPackage = "${ProjectSettings.packageName}.client"
    multiplatformResourcesSourceSet = "mobileMain"
    disableStaticFrameworkWarning = true
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}
