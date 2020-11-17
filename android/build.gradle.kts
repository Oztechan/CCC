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
        implementation(multiDex)
        implementation(androidMaterial)
        implementation(constraintLayout)
        implementation(dagger)
        implementation(timber)
        implementation(admob)
        implementation(navigation)
        implementation(playCore)
        implementation(roomRuntime)
    }
    with(Annotations) {
        kapt(daggerCompiler)
        kapt(daggerProcessor)
    }

    kapt(Annotations.daggerCompiler)

    with(TestDependencies) {
        testImplementation(jUnit)
        testImplementation(mockK)
        testImplementation(archTesting)
        testImplementation(coroutinesTest)
    }

    with(Modules) {
        implementation(project(client))
        implementation(project(common))

        implementation(project(data))

        implementation(project(scopemob))

        implementation(project(logmob))
    }

    implementation(files(Libs.mxParser))
}
