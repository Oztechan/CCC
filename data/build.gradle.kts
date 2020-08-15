/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
}

android {
    compileSdkVersion(Configuration.compileSdkVersion)

    defaultConfig {
        minSdkVersion(Configuration.minSdkVersion)
        targetSdkVersion(Configuration.targetSdkVersion)

        versionCode = Configuration.getVersionCode(project)
        versionName = Configuration.getVersionName(project)

        android {
            javaCompileOptions {
                annotationProcessorOptions {
                    arguments = mapOf("room.schemaLocation" to "$projectDir/schemas")
                }
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation(Dependencies.kotlin)
    implementation(Dependencies.dagger)
    implementation(Dependencies.moshi)
    implementation(Dependencies.timber)
    implementation(Dependencies.retrofit)
    implementation(Dependencies.roomKtx)

    testImplementation(Tests.jUnit)

    kapt(Annotations.daggerCompiler)
    kapt(Annotations.moshi)
    kapt(Annotations.room)

    implementation(project(Modules.scopemob))
    implementation(project(Modules.basemob))
}
