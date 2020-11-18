/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
plugins {
    with(Plugins) {
        id(androidApplication)
        id(crashlytics)
        id(googleServices)
        id(safeargs)
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

        buildFeatures {
            viewBinding = true
            dataBinding = true
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    with(Dependencies.Android) {
        implementation(multiDex)
        implementation(androidMaterial)
        implementation(constraintLayout)
        implementation(dagger)
        implementation(admob)
        implementation(navigation)
        implementation(playCore)
        implementation(roomRuntime)

        testImplementation(jUnit)
        testImplementation(mockK)
        testImplementation(archTesting)
        testImplementation(coroutinesTest)
    }
    with(Annotations) {
        kapt(daggerCompiler)
        kapt(daggerProcessor)
    }

    with(Modules) {
        implementation(project(client))
        implementation(project(common))

        implementation(project(data))

        implementation(project(logmob))
        implementation(project(scopemob))
    }

    implementation(files(Libs.mxParser))
}
