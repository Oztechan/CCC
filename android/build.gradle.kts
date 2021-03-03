/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
plugins {
    with(Plugins) {
        id(androidApplication)
        id(crashlytics)
        id(googleServices)
        id(safeargs)
        kotlin(android)
    }
}

android {
    with(ProjectSettings) {
        compileSdkVersion(projectCompileSdkVersion)

        defaultConfig {
            minSdkVersion(projectMinSdkVersion)
            targetSdkVersion(projectTargetSdkVersion)

            applicationId = projectId

            versionCode = getVersionCode(project)
            versionName = getVersionName(project)
        }

        compileOptions {
            isCoreLibraryDesugaringEnabled = true
            sourceCompatibility = JavaVersion.VERSION_1_8
            targetCompatibility = JavaVersion.VERSION_1_8
        }

        kotlinOptions {
            jvmTarget = JavaVersion.VERSION_1_8.toString()
        }

        buildFeatures {
            viewBinding = true
        }
    }
}

dependencies {
    with(Dependencies.Android) {
        implementation(androidMaterial)
        implementation(constraintLayout)
        implementation(admob)
        implementation(navigation)
        implementation(playCore)
        implementation(koinAndroid)
        coreLibraryDesugaring(desugaring)
    }

    with(Dependencies.Common) {
        implementation(dateTime)
    }

    with(Modules) {
        implementation(project(client))

        implementation(project(basemob))
        implementation(project(scopemob))
        implementation(project(logmob))
    }
}
