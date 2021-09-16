/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

plugins {
    id(Plugins.DEPENDENCY_UPDATES) version Versions.DEPENDENCY_UPDATES
    id(Plugins.BUILD_HEALTH) version Versions.BUILD_HEALTH
}

buildscript {
    repositories {
        gradlePluginPortal()
        google()
        maven("https://dl.bintray.com/icerockdev/plugins")
    }
    dependencies {
        with(ClassPaths) {
            classpath(ANDROID_GRADLE_PLUGIN)
            classpath(KOTLIN_GRADLE_PLUGIN)
            classpath(GSM)
            classpath(CRASHLYTICS)
            classpath(NAVIGATION)
            classpath(KOTLIN_SERIALIZATION)
            classpath(SQL_DELIGHT)
            classpath(MOKO_RESOURCES)
            classpath(BUILD_KONFIG)
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
    // show only stable version of gradle
    gradleReleaseChannel = "current"
    rejectVersionIf {
        // show only stable versions
        isNonStable(candidate.version) &&
            // hide warnings for current unstable versions
            !isNonStable(currentVersion)
    }
}

fun isNonStable(version: String): Boolean {
    val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.toUpperCase().contains(it) }
    val regex = "^[0-9,.v-]+(-r)?$".toRegex()
    val isStable = stableKeyword || regex.matches(version)
    return isStable.not()
}
