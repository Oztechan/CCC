/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    with(Plugins) {
        kotlin(multiplatform)
        id(androidLibrary)
    }
}

group = ProjectSettings.projectId
version = ProjectSettings.getVersionName(project)

repositories {
    gradlePluginPortal()
    google()
}

kotlin {

    jvm()

    android()

    ios {
        binaries {
            framework {
                baseName = "common"
            }
        }
    }

    // todo need to revert when Koin supports IR
    // https://github.com/InsertKoinIO/koin/issues/929
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

        with(Dependencies.Common) {
            val commonMain by getting {
                dependencies {
                    implementation(project(Modules.logmob))
                    api(koinCore)
                    api(kermit)
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
            val androidMain by getting
            val androidTest by getting {
                dependencies {
                    implementation(jUnit)

                    // to run tests in common
                    implementation(kotlin(Dependencies.JVM.testJUnit))
                }
            }
        }

        val iosMain by getting
        val iosTest by getting

        with(Dependencies.JVM) {
            val jvmMain by getting
            val jvmTest by getting {
                dependencies {
                    implementation(kotlin(testJUnit))
                }
            }
        }

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
