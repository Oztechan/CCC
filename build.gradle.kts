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
    apply(plugin = "kover")
    repositories {
        mavenCentral()
        google()
        maven("https://dl.bintray.com/ekito/koin")
        maven("https://dl.bintray.com/icerockdev/moko")
        maven("https://kotlin.bintray.com/kotlinx/")
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

koverMerged {
    enable()
    filters {
        classes {
            excludes += buildTestPackagePaths()
        }
    }
}

fun buildTestPackagePaths() = mutableListOf<String>().apply {
    repeat(30) { depth ->
        var result = ""
        val prefix = "*."
        val postfix = "*Test"
        repeat(depth) {
            result = "$result$prefix"
        }
        add("$result$postfix")
    }
}
