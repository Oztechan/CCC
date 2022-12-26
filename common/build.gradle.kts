/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
import Modules.packageName
import Modules.path
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import com.codingfeline.buildkonfig.gradle.BuildKonfigExtension
import config.Keys
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    @Suppress("DSL_SCOPE_VIOLATION")
    with(libs.plugins) {
        id(multiplatform.get().pluginId)
        id(kotlinXSerialization.get().pluginId)
        id(androidLib.get().pluginId)
        id(sqlDelight.get().pluginId)
        id(buildKonfig.get().pluginId)
        alias(ksp)
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
                with(libs.common) {
                    implementation(kotlinXDateTime)
                    implementation(koinCore)
                    implementation(ktorLogging)
                    implementation(ktorJson)
                    implementation(ktorContentNegotiation)
                    implementation(sqlDelightRuntime)
                    implementation(sqlDelightCoroutinesExt)
                    implementation(coroutines)
                }
                implementation(project(Modules.LOGMOB.path))
            }
        }
        val commonTest by getting {
            dependencies {
                with(libs.common) {
                    implementation(mockative)
                    implementation(coroutinesTest)
                }
                implementation(project(Modules.TEST.path))
            }
        }

        val androidMain by getting {
            dependencies {
                with(libs.android) {
                    implementation(sqlDelight)
                    implementation(ktor)
                }
            }
        }
        val androidTest by getting

        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependencies {
                with(libs.ios) {
                    implementation(ktor)
                    implementation(sqlDelight)
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
                with(libs.jvm) {
                    implementation(ktor)
                    implementation(sqlliteDriver)
                }
            }
        }
        val jvmTest by getting
    }
}

dependencies {
    ksp(libs.processors.mockative)
}

ksp {
    arg("mockative.stubsUnitByDefault", "true")
}

android {
    with(ProjectSettings) {
        namespace = Modules.COMMON.packageName
        compileSdk = COMPILE_SDK_VERSION

        @Suppress("UnstableApiUsage")
        defaultConfig {
            minSdk = MIN_SDK_VERSION
            targetSdk = TARGET_SDK_VERSION
        }
    }
}

sqldelight {
    database("CurrencyConverterCalculatorDatabase") {
        packageName = "${Modules.COMMON.packageName}.database.sql"
        sourceFolders = listOf("sql")
        dialect = "sqlite:3.25"
    }
}

configure<BuildKonfigExtension> {
    packageName = Modules.COMMON.packageName

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
