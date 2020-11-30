/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
plugins {
    with(Plugins) {
        id(library)
        id(kotlinXSerialization)
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

            versionCode = getVersionCode(project)
            versionName = getVersionName(project)

            android {
                javaCompileOptions {
                    annotationProcessorOptions {
                        argument("room.schemaLocation", "$projectDir/schemas")
                    }
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

    with(Dependencies.Android) {
        implementation(retrofit)
        implementation(roomKtx)

        testImplementation(jUnit)
    }

    implementation(Dependencies.Common.serialization)
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.1")
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0")

    kapt(Annotations.room)

    with(Modules) {
        implementation(project(common))
        implementation(project(scopemob))
    }
}
