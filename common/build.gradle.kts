/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

import com.codingfeline.buildkonfig.compiler.FieldSpec.Type
import com.codingfeline.buildkonfig.gradle.BuildKonfigExtension
import org.jetbrains.kotlin.konan.properties.Properties

plugins {
    with(Plugins) {
        kotlin(multiplatform)
        id(kotlinXSerialization)
        id(androidLibrary)
        id(sqldelight)
        id(buildKonfig)
    }
}

kotlin {

    android()

    ios()

    js {
        browser {
            binaries.executable()
            testTask {
                enabled = false
            }
        }
    }

    jvm()

    @Suppress("UNUSED_VARIABLE")
    sourceSets {

        with(Dependencies.Common) {
            val commonMain by getting {
                dependencies {
                    implementation(project(Modules.logmob))

                    implementation(multiplatformSettings)
                    implementation(dateTime)

                    implementation(koinCore)
                    implementation(kermit)

                    implementation(ktorLogging)
                    implementation(ktorSerialization)

                    implementation(sqldelightRuntime)
                    implementation(sqldelightCoroutineExtensions)
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
                    implementation(sqlliteDriver)
                    implementation(ktor)
                }
            }
            val androidTest by getting {
                dependencies {
                    implementation(kotlin(Dependencies.JVM.testJUnit))
                }
            }
        }

        with(Dependencies.IOS) {
            val iosMain by getting {
                dependencies {
                    implementation(ktor)
                    implementation(sqlliteDriver)
                }
            }
            val iosTest by getting
        }

        with(Dependencies.JS) {
            val jsMain by getting {
                dependencies {
                    implementation(ktor)
                }
            }
            val jsTest by getting {
                dependencies {
                    implementation(kotlin(test))
                }
            }
        }

        with(Dependencies.JVM) {
            val jvmMain by getting {
                dependencies {
                    implementation(ktor)
                    implementation(sqlliteDriver)
                }
            }
            val jvmTest by getting {
                dependencies {
                    implementation(kotlin(testJUnit))
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

        // todo remove after https://github.com/touchlab/Kermit/issues/67
        testOptions {
            unitTests.isReturnDefaultValues = true
        }

        // todo https://youtrack.jetbrains.com/issue/KT-43944
        configurations {
            create("testApi") {}
            create("testDebugApi") {}
            create("testReleaseApi") {}
        }

        sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    }
}

sqldelight {
    database(Database.name) {
        packageName = Database.packageName
        sourceFolders = listOf(Database.sourceFolders)
    }
}

@Suppress("TooGenericExceptionCaught")
configure<BuildKonfigExtension> {
    packageName = "${ProjectSettings.packageName}.common"

    val props = Properties()

    try {
        props.load(file("key.properties").inputStream())
    } catch (e: Exception) {
        // keys are private and can not be committed to git
    }

    defaultConfigs {
        buildConfigField(
            Type.STRING,
            "BASE_URL_BACKEND",
            props["base_url_backend"]?.toString() ?: "http://private.backend.url"
        )
        buildConfigField(
            Type.STRING,
            "BASE_URL_API",
            props["base_url_api"]?.toString() ?: "http://private.api.url"
        )
    }
}
