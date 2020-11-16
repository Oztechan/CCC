/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
plugins {
    with(Plugins) {
        id(library)
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
}

dependencies {

    with(Dependencies) {
        implementation(kotlin)
        implementation(dagger)
        implementation(moshi)
        implementation(moshiConverter)
        implementation(timber)
        implementation(retrofit)
        implementation(roomKtx)
    }

    testImplementation(TestDependencies.jUnit)

    with(Annotations) {
        kapt(daggerCompiler)
        kapt(moshi)
        kapt(room)
    }

    with(Modules) {
        implementation(project(scopemob))
    }
}
