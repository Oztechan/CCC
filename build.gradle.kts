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
        maven("https://dl.bintray.com/icerockdev/plugins")
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
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
            classpath(buildKonfig)
            classpath(compose)
        }
    }
}

group = ProjectSettings.projectId
version = ProjectSettings.getVersionName(project)

allprojects {
    repositories {
        mavenCentral()
        google()
        maven("https://dl.bintray.com/ekito/koin")
        maven("https://dl.bintray.com/icerockdev/moko")
        maven("https://kotlin.bintray.com/kotlin-js-wrappers")
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven("https://kotlin.bintray.com/kotlinx/")
    }
}
