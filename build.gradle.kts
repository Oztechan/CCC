/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    with(Dependencies.Plugins) {
        id(DEPENDENCY_UPDATES) version Versions.DEPENDENCY_UPDATES
        id(KOVER) version Versions.KOVER
    }
}

buildscript {
    dependencies {
        with(Dependencies.ClassPaths) {
            classpath(ANDROID_GRADLE_PLUGIN)
            classpath(KOTLIN_GRADLE_PLUGIN)
            classpath(GSM)
            classpath(FIREBASE_PER_PLUGIN)
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
    apply(plugin = "kover").also {
        koverMerged.enable()
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            allWarningsAsErrors = true
        }
    }
}

tasks.withType<DependencyUpdatesTask> {
    gradleReleaseChannel = "current"
    rejectVersionIf { candidate.version.isNonStable() }
}
