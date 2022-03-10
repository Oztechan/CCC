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

    // todo Revert to just ios() when gradle plugin can properly resolve it
    if (System.getenv("SDK_NAME")?.startsWith("iphoneos") == true) {
        iosArm64("ios")
    } else {
        iosX64("ios")
    }

    jvm()

    @Suppress("UNUSED_VARIABLE")
    sourceSets {
        val commonMain by getting { setCommonMainDependencies() }
        val commonTest by getting { setCommonTestDependencies() }

        val androidMain by getting { setAndroidMainDependencies() }
        val androidTest by getting

        val iosMain by getting { setIosMainDependencies() }
        val iosTest by getting

        val jvmMain by getting { setJvmMainDependencies() }
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
        with(Keys(project)) {
            buildConfigField(STRING, baseUrlBackend.key, baseUrlBackend.value)
            buildConfigField(STRING, baseUrlApi.key, baseUrlApi.value)
            buildConfigField(STRING, baseUrlApiPremium.key, baseUrlApiPremium.value)
            buildConfigField(STRING, apiKeyPremium.key, apiKeyPremium.value)
        }
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions { jvmTarget = JavaVersion.VERSION_1_8.toString() }
}

fun setCommonMainDependencies() {
    dependencies {
        implementation(project(Dependencies.Modules.LOG_MOB))

        with(Dependencies.Common) {
            implementation(MULTIPLATFORM_SETTINGS)
            implementation(KOTLIN_X_DATE_TIME)
            implementation(KOIN_CORE)
            implementation(KTOR_LOGGING)
            implementation(KTOR_SETIALIZATION)
            implementation(SQL_DELIGHT_RUNTIME)
            implementation(SQL_DELIGHT_COROUTINES_EXT)
            implementation(COROUTINES) {
                version {
                    strictly(Versions.COROUTINES)
                }
            }
        }
    }
}

fun setCommonTestDependencies() {
    dependencies {
        with(Dependencies.Common) {
            implementation(kotlin(TEST))
            implementation(kotlin(TEST_ANNOTATIONS))
            implementation(MOCKATIVE)
        }
    }
}

fun setAndroidMainDependencies() {
    dependencies {
        with(Dependencies.Android) {
            implementation(SQL_DELIGHT)
            implementation(KTOR)
        }
    }
}

fun setIosMainDependencies() {
    with(Dependencies.IOS) {
        dependencies {
            implementation(KTOR)
            implementation(SQL_DELIGHT)
        }
    }
}

fun setJvmMainDependencies() {
    with(Dependencies.JVM) {
        dependencies {
            implementation(KTOR)
            implementation(SQLLITE_DRIVER)
        }
    }
}