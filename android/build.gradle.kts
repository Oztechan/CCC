/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
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
        implementation(koinAndroidViewModel)

        testImplementation(jUnit) // todo remove when viewModels moved
        testImplementation(mockK)
        testImplementation(archTesting)
        testImplementation(coroutinesTest)
    }

    implementation(Dependencies.Common.dateTime) // todo remove after test removed

    with(Modules) {
        implementation(project(client))
        implementation(project(common))

        implementation(project(basemob))
        implementation(project(scopemob))
        implementation(project(logmob))
    }
}
