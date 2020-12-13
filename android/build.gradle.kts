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

        kotlin {
            sourceSets {
                all {
                    languageSettings.apply {
                        useExperimentalAnnotation("kotlinx.coroutines.FlowPreview")
                        useExperimentalAnnotation("kotlinx.coroutines.ExperimentalCoroutinesApi")
                    }
                }
            }
        }
    }
}

dependencies {
    with(Dependencies.Android) {
        implementation(multiDex)
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

    implementation(Dependencies.Common.dateTime)

    with(Modules) {
        implementation(project(client))
        implementation(project(common))

        implementation(project(calculator))

        implementation(project(basemob))
        implementation(project(scopemob))
        implementation(project(logmob))
    }
}
