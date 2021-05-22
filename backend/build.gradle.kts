/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

plugins {
    with(Plugins) {
        application
        kotlin(multiplatform)
        kotlin(serializationPlugin)
    }
}

with(ProjectSettings) {
    application {
        mainClass.set("$packageName.backend.BackendAppKt")
    }
    group = projectId
    version = getVersionName(project)
}

kotlin {
    jvm {
        withJava()
    }

    @Suppress("UNUSED_VARIABLE")
    sourceSets {
        val jvmMain by getting {
            dependencies {
                with(Dependencies.JVM) {
                    implementation(ktorCore)
                    implementation(ktorNetty)
                    implementation(ktorSerialization)
                    implementation(logBack)
                }

                with(Dependencies.Common) {
                    implementation(koinCore)
                }

                with(Modules) {
                    implementation(project(common))
                    implementation(project(logmob))
                }
            }
        }
    }
}
