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

allprojects {
    apply(plugin = rootProject.libs.plugins.kover.get().pluginId).also {
        koverMerged.enable()
    }

    apply(plugin = rootProject.libs.plugins.detekt.get().pluginId).also {
        detekt {
            buildUponDefaultConfig = true
            allRules = true
            parallel = true
        }
        tasks.withType<Detekt> {
            setSource(files(project.projectDir))
            exclude("**/build/**")
            exclude {
                it.file.relativeTo(projectDir).startsWith(project.buildDir.relativeTo(projectDir))
            }
        }
        tasks.register("detektAll") {
            dependsOn(tasks.withType<Detekt>())
        }
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
