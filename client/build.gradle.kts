/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    with(Plugins) {
        kotlin(multiplatform)
        id(androidLibrary)
        id(sqldelight)
    }
}

kotlin {
    android()

    ios {
        binaries {
            framework {
                baseName = "client"
            }
        }
    }

    js {
        browser {
            binaries.executable()
            testTask {
                enabled = false
            }
        }
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
                    implementation(multiplatformSettings)
                    implementation(dateTime)
                    implementation(coroutines)
                    implementation(koinCore)
                    implementation(kermit)

                    with(Modules) {
                        implementation(project(common))
                        implementation(project(calculator))
                        implementation(project(scopemob))
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

        with(Dependencies.Android) {
            val androidMain by getting {
                dependencies {
                    implementation(androidMaterial)
                    implementation(koinAndroidViewModel)
                    implementation(viewModelExt)
                }
            }
            val androidTest by getting {
                dependencies {
                    implementation(kotlin(Dependencies.JVM.testJUnit))
                }
            }
        }

        val iosMain by getting
        val iosTest by getting

        with(Dependencies.JS) {
            val jsMain by getting
            val jsTest by getting {
                dependencies {
                    implementation(kotlin(test))
                }
            }
        }
    }
}

android {
    with(ProjectSettings) {
        compileSdkVersion(projectCompileSdkVersion)

        defaultConfig {
            minSdkVersion(projectMinSdkVersion)
            targetSdkVersion(projectTargetSdkVersion)
            versionCode = getVersionCode(project)
            versionName = getVersionName(project)
        }

        sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    }
}

val packForXcode by tasks.creating(Sync::class) {
    group = "build"
    val mode = System.getenv("CONFIGURATION") ?: "DEBUG"
    val sdkName = System.getenv("SDK_NAME") ?: "iphonesimulator"
    val targetName = "ios" + if (sdkName.startsWith("iphoneos")) "Arm64" else "X64"
    val framework =
        kotlin.targets.getByName<KotlinNativeTarget>(targetName).binaries.getFramework(mode)
    inputs.property("mode", mode)
    dependsOn(framework.linkTask)
    val targetDir = File(buildDir, "xcode-frameworks")
    from({ framework.outputDirectory })
    into(targetDir)
}

tasks.getByName("build").dependsOn(packForXcode)
