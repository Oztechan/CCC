/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

plugins {
    id(Plugins.versionChecker) version Versions.versionChecker
}

buildscript {
    repositories {
        gradlePluginPortal()
        google()
    }
    dependencies {
        with(Classpaths) {
            classpath(androidBuildTools)
            classpath(kotlinGradlePlugin)
            classpath(gsmGoogle)
            classpath(crashlytics)
            classpath(navigation)
            classpath(kotlinSerialization)
            classpath(sqldelight)
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
        maven(url = "https://kotlin.bintray.com/kotlinx/") // soon will be just jcenter()
    }
}
