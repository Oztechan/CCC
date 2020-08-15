/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

plugins {
    id("com.android.application")
    id("com.google.firebase.crashlytics")
    id("com.google.gms.google-services")
    kotlin("android")
    kotlin("kapt")
}

android {
    compileSdkVersion(Configuration.compileSdkVersion)

    defaultConfig {
        minSdkVersion(Configuration.minSdkVersion)
        targetSdkVersion(Configuration.targetSdkVersion)

        multiDexEnabled = true
        applicationId = Configuration.applicationId

        versionCode = Configuration.getVersionCode(project)
        versionName = Configuration.getVersionName(project)
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
}

dependencies {
    implementation(Dependencies.kotlin)
    implementation(Dependencies.dagger)
    implementation(Dependencies.multiDex)
    implementation(Dependencies.roomRuntime)

    kapt(Annotations.daggerCompiler)

    implementation(project(Modules.ui))

    implementation(project(Modules.data))

    implementation(project(Modules.basemob))
    implementation(project(Modules.logmob))
}
