/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import com.codingfeline.buildkonfig.gradle.BuildKonfigExtension
import config.Keys
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    with(Dependencies.Plugins) {
        kotlin(MULTIPLATFORM)
        id(KOTLIN_X_SERIALIZATION)
        id(ANDROID_LIB)
        id(SQL_DELIGHT)
        id(BUILD_KONFIG)
        id(KSP) version (Versions.KSP)
    }
}

kotlin {

    android()

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    jvm()

    @Suppress("UNUSED_VARIABLE")
    sourceSets {

        val commonMain by getting {
            dependencies {
                with(Dependencies.Common) {
                    implementation(KOTLIN_X_DATE_TIME)
                    implementation(KOIN_CORE)
                    implementation(KTOR_LOGGING)
                    implementation(KTOR_JSON)
                    implementation(KTOR_CONTENT_NEGOTIATION)
                    implementation(SQL_DELIGHT_RUNTIME)
                    implementation(SQL_DELIGHT_COROUTINES_EXT)
                    implementation(COROUTINES)
                }
                implementation(project(Dependencies.Modules.LOGMOB))
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
                    implementation(SQL_DELIGHT)
                    implementation(KTOR)
                }
            }
        }
        val androidTest by getting

        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependencies {
                with(Dependencies.IOS) {
                    implementation(KTOR)
                    implementation(SQL_DELIGHT)
                }
            }
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

        val jvmMain by getting {
            dependencies {
                with(Dependencies.JVM) {
                    implementation(KTOR)
                    implementation(SQLLITE_DRIVER)
                }
            }
        }
        val jvmTest by getting
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

        @Suppress("UnstableApiUsage")
        defaultConfig {
            minSdk = MIN_SDK_VERSION
            targetSdk = TARGET_SDK_VERSION
        }

        sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    }
}

sqldelight {
    database("CurrencyConverterCalculatorDatabase") {
        packageName = "${ProjectSettings.PROJECT_ID}.common.db.sql"
        sourceFolders = listOf("kotlin")
    }
}

configure<BuildKonfigExtension> {
    packageName = "${ProjectSettings.PROJECT_ID}.common"

    defaultConfigs {
        with(Keys(project)) {
            buildConfigField(STRING, baseUrlBackend.key, baseUrlBackend.value, const = true)
            buildConfigField(STRING, baseUrlApi.key, baseUrlApi.value, const = true)
            buildConfigField(STRING, baseUrlApiPremium.key, baseUrlApiPremium.value, const = true)
            buildConfigField(STRING, apiKeyPremium.key, apiKeyPremium.value, const = true)
        }
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions { jvmTarget = JavaVersion.VERSION_1_8.toString() }
}
