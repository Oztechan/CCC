/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
plugins {
    id("com.android.library")
    id("androidx.navigation.safeargs")
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
    implementation(Dependencies.androidMaterial)
    implementation(Dependencies.constraintLayout)
    implementation(Dependencies.dagger)
    implementation(Dependencies.timber)
    implementation(Dependencies.admob)
    implementation(Dependencies.navigation)

    kapt(Annotations.daggerCompiler)
    kapt(Annotations.daggerProcessor)

    testImplementation(Tests.jUnit)
    testImplementation(Tests.mockK)
    testImplementation(Tests.archTesting)
    testImplementation(Tests.coroutinesTest)

    implementation(project(Modules.data))

    implementation(project(Modules.scopemob))
    implementation(project(Modules.basemob))

    implementation(files(Libs.mxParser))
}
