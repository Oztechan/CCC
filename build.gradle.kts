/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

plugins {
    id(Plugins.dependencyUpdates) version Versions.dependencyUpdates
    id(Plugins.buildHealth) version Versions.buildHealth
}

buildscript {
    repositories {
        gradlePluginPortal()
        google()
        maven(url = "https://dl.bintray.com/icerockdev/plugins")
    }
    dependencies {
        with(ClassPaths) {
            classpath(androidBuildTools)
            classpath(kotlinGradlePlugin)
            classpath(gsmGoogle)
            classpath(crashlytics)
            classpath(navigation)
            classpath(kotlinSerialization)
            classpath(sqldelight)
            classpath(mokoResoruces)
        }
    }
}

group = ProjectSettings.projectId
version = ProjectSettings.getVersionName(project)

allprojects {
    repositories {
        mavenCentral()
        google()
        jcenter()
        maven(url = "https://dl.bintray.com/ekito/koin")
        // todo soon will be just jcenter() https://github.com/Kotlin/kotlinx-datetime/issues/40
        maven(url = "https://kotlin.bintray.com/kotlinx/")
        maven(url = "https://dl.bintray.com/icerockdev/moko")
    }
}
