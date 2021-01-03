/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

plugins {
    with(Plugins) {
        kotlin(multiplatform)
        id(kotlinXSerialization)
        id(androidLibrary)
        id(sqldelight)
    }
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

        // todo remove after androidPlugin = "7.0.0-alpha03" fixed
        configurations {
            create("testApi") {}
            create("testDebugApi") {}
            create("testReleaseApi") {}
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_1_8
            targetCompatibility = JavaVersion.VERSION_1_8
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
