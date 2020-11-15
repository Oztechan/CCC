/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

plugins {
    with(Plugins) {
        id(application)
        id(crashlytics)
        id(googleServices)
        id(versions) version Versions.versionsVersion
        kotlin(android)
        kotlin(kapt)
    }
}

android {
    with(ProjectSettings) {
        compileSdkVersion(projectCompileSdkVersion)

        defaultConfig {
            minSdkVersion(projectMinSdkVersion)
            targetSdkVersion(projectTargetSdkVersion)

            multiDexEnabled = true
            applicationId = applicationId

            versionCode = getVersionCode(project)
            versionName = getVersionName(project)
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
}

dependencies {
    with(Dependencies) {
        implementation(kotlin)
        implementation(dagger)
        implementation(multiDex)
        implementation(roomRuntime)
    }
    kapt(Annotations.daggerCompiler)

    with(Modules) {
        implementation(project(ui))

        implementation(project(data))

        implementation(project(logmob))
    }
}
