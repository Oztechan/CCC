/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.INT
import com.codingfeline.buildkonfig.gradle.BuildKonfigExtension
import config.DeviceFlavour
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet as SourceSet
import org.jetbrains.kotlin.gradle.plugin.cocoapods.CocoapodsExtension as Cocoapods

plugins {
    with(Dependencies.Plugins) {
        kotlin(MULTIPLATFORM)
        kotlin(COCOAPODS)
        id(ANDROID_LIB)
        id(SQL_DELIGHT)
        id(MOKO_RESOURCES)
        id(BUILD_KONFIG)
        id(KSP) version (Versions.KSP)
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

    cocoapods { addConfig() }

    @Suppress("UNUSED_VARIABLE")
    sourceSets {

        all { addLanguageSettings() }

        val commonMain by getting { setCommonMainDependencies() }
        val commonTest by getting { setCommonTestDependencies() }

        val mobileMain by creating { setMobileMainDependencies(commonMain) }

        val androidMain by getting { setAndroidMainDependencies(mobileMain) }
        val androidTest by getting

        val iosMain by getting { dependencies { dependsOn(mobileMain) } }
        val iosTest by getting
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

        // todo needed for android coroutine testing
        testOptions {
            unitTests.isReturnDefaultValues = true
        }

        sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    }

    with(DeviceFlavour) {
        flavorDimensions.addAll(listOf(flavorDimension))

        productFlavors {
            create(google) {
                dimension = flavorDimension
            }

            create(huawei) {
                dimension = flavorDimension
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

configure<BuildKonfigExtension> {
    packageName = "${ProjectSettings.PACKAGE_NAME}.client"

    defaultConfigs {
        buildConfigField(INT, "versionCode", ProjectSettings.getVersionCode(project).toString())
    }
}

fun setCommonMainDependencies() {
    with(Dependencies.Common) {
        dependencies {
            implementation(KOTLIN_X_DATE_TIME)
            implementation(COROUTINES)
            implementation(KOIN_CORE)

            with(Dependencies.Modules) {
                implementation(project(COMMON))
                implementation(project(PARSER_MOB))
                implementation(project(SCOPE_MOB))
                implementation(project(LOG_MOB))
                implementation(project(CONFIG))
            }
        }
    }
}

fun setCommonTestDependencies() {
    with(Dependencies.Common) {
        dependencies {
            implementation(kotlin(TEST))
            implementation(kotlin(TEST_ANNOTATIONS))
            implementation(MOCKATIVE)
        }
    }
}

fun SourceSet.setMobileMainDependencies(commonMain: SourceSet) {
    dependencies {
        dependsOn(commonMain)
        implementation(Dependencies.Common.MOKO_RESOURCES)
    }
}

fun SourceSet.addLanguageSettings() {
    languageSettings.apply {
        optIn("kotlinx.coroutines.FlowPreview")
        optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
    }
}

fun SourceSet.setAndroidMainDependencies(mobileMain: SourceSet) {
    with(Dependencies.Android) {
        dependencies {
            dependsOn(mobileMain)
            implementation(ANDROID_MATERIAL)
            implementation(KOIN_ANDROID)
            implementation(LIFECYCLE_VIEWMODEL)
        }
    }
}

fun Cocoapods.addConfig() {
    summary = "CCC"
    homepage = "https://github.com/CurrencyConverterCalculator/CCC"
    ios.deploymentTarget = "14.0"
    framework {
        baseName = "Client"
    }
}
