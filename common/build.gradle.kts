/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import com.codingfeline.buildkonfig.gradle.BuildKonfigExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    with(Dependencies.Plugins) {
        kotlin(MULTIPLATFORM)
        id(KOTLIN_X_SERIALIZATION)
        id(ANDROID_LIB)
        id(SQL_DELIGHT)
        id(BUILD_KONFIG)
    }
}

kotlin {

    android()

    // todo Revert to just ios() when gradle plugin can properly resolve it
    if (System.getenv("SDK_NAME")?.startsWith("iphoneos") == true) {
        iosArm64("ios")
    } else {
        iosX64("ios")
    }

    jvm()

    @Suppress("UNUSED_VARIABLE")
    sourceSets {

        with(Dependencies.Common) {
            val commonMain by getting {
                dependencies {
                    implementation(project(Dependencies.Modules.LOG_MOB))

                    implementation(MULTIPLATFORM_SETTINGS)
                    implementation(KOTLIN_X_DATE_TIME)
                    implementation(KOIN_CORE)
                    implementation(KTOR_LOGGING)
                    implementation(KTOR_SETIALIZATION)
                    implementation(SQL_DELIGHT_RUNTIME)
                    implementation(SQL_DELIGHT_COROUTINES_EXT)
                }
            }
            val commonTest by getting {
                dependencies {
                    implementation(kotlin(TEST))
                    implementation(kotlin(TEST_ANNOTATIONS))
                }
            }
        }

        with(Dependencies.Android) {
            val androidMain by getting {
                dependencies {
                    implementation(SQL_DELIGHT)
                    implementation(KTOR)
                }
            }
            val androidTest by getting {
                dependencies {
                    implementation(kotlin(Dependencies.JVM.TEST_J_UNIT))
                }
            }
        }

        with(Dependencies.IOS) {
            val iosMain by getting {
                dependencies {
                    implementation(KTOR)
                    implementation(SQL_DELIGHT)
                }
            }
            val iosTest by getting
        }

        with(Dependencies.JVM) {
            val jvmMain by getting {
                dependencies {
                    implementation(KTOR)
                    implementation(SQLLITE_DRIVER)
                }
            }
            val jvmTest by getting {
                dependencies {
                    implementation(kotlin(TEST_J_UNIT))
                }
            }
        }
    }
}

android {
    with(ProjectSettings) {
        compileSdk = COMPILE_SDK_VERSION

        defaultConfig {
            minSdk = MIN_SDK_VERSION
            targetSdk = TARGET_SDK_VERSION
        }

        sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    }
}

sqldelight {
    database("CurrencyConverterCalculatorDatabase") {
        packageName = "${ProjectSettings.PACKAGE_NAME}.common.db.sql"
        sourceFolders = listOf("kotlin")
    }
}

configure<BuildKonfigExtension> {
    packageName = "${ProjectSettings.PACKAGE_NAME}.common"

    defaultConfigs {
        buildConfigField(
            STRING,
            Keys.BASE_URL_BACKEND,
            getSecret(Keys.BASE_URL_BACKEND, Fakes.PRIVATE_URL)
        )
        buildConfigField(STRING, Keys.BASE_URL_API, getSecret(Keys.BASE_URL_API, Fakes.PRIVATE_URL))
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions { jvmTarget = JavaVersion.VERSION_1_8.toString() }
}
