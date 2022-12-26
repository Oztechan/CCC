/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import io.gitlab.arturbosch.detekt.Detekt
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    @Suppress("DSL_SCOPE_VIOLATION")
    with(libs.plugins) {
        alias(dependencyUpdates)
        alias(kover)
        alias(detekt)
    }
}

buildscript {
    dependencies {
        with(libs.classpaths) {
            classpath(androidGradlePlugin)
            classpath(kotlinGradlePlugin)
            classpath(gsm)
            classpath(firebasePerPlugin)
            classpath(crashlytics)
            classpath(navigation)
            classpath(kotlinSerialization)
            classpath(sqlDelight)
            classpath(mokoResources)
            classpath(buildKonfig)
            classpath(kover)
        }
    }
}

group = ProjectSettings.PROJECT_ID
version = ProjectSettings.getVersionName(project)

apply(plugin = libs.plugins.kover.get().pluginId).also {
    koverMerged {
        allprojects {
            enable()
        }
    }
}

apply(plugin = libs.plugins.detekt.get().pluginId).also {
    detekt {
        buildUponDefaultConfig = true
        allRules = true

        allprojects {
            tasks.withType<Detekt> {
                setSource(files(project.projectDir))
                exclude("**/build/**")
            }
            tasks.register("detektAll") {
                dependsOn(tasks.withType<Detekt>())
            }
        }
    }
}

allprojects {
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
