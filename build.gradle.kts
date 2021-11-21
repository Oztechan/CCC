/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

plugins {
    with(Dependencies.Plugins) {
        id(DEPENDENCY_UPDATES) version Versions.DEPENDENCY_UPDATES
        id(BUILD_HEALTH) version Versions.BUILD_HEALTH
        id(KOVER) version Versions.KOVER
    }
}

buildscript {
    repositories {
        gradlePluginPortal()
        google()
        maven("https://dl.bintray.com/icerockdev/plugins")
    }
    dependencies {
        with(Dependencies.ClassPaths) {
            classpath(ANDROID_GRADLE_PLUGIN)
            classpath(KOTLIN_GRADLE_PLUGIN)
            classpath(GSM)
            classpath(CRASHLYTICS)
            classpath(NAVIGATION)
            classpath(KOTLIN_SERIALIZATION)
            classpath(SQL_DELIGHT)
            classpath(MOKO_RESOURCES)
            classpath(BUILD_KONFIG)
            classpath(KOVER)
        }
    }
}

group = ProjectSettings.PROJECT_ID
version = ProjectSettings.getVersionName(project)

allprojects {
    repositories {
        mavenCentral()
        google()
        maven("https://dl.bintray.com/ekito/koin")
        maven("https://dl.bintray.com/icerockdev/moko")
        maven("https://kotlin.bintray.com/kotlinx/")
    }
}

tasks.withType<DependencyUpdatesTask> {
    gradleReleaseChannel = "current"
    rejectVersionIf { candidate.version.isNonStable() }
}
