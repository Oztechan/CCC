/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
import io.gitlab.arturbosch.detekt.Detekt
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    libs.plugins.apply {
        alias(kotlinMultiplatform).apply(false)
        alias(jetbrainsCompose).apply(false)
        alias(kotlinJvm).apply(false)
        alias(kotlinAndroid).apply(false)
        alias(androidApplication).apply(false)
        alias(androidLibrary).apply(false)
        alias(buildKonfig).apply(false)
        alias(sqlDelight).apply(false)
        alias(kover)
        alias(detekt)
    }
}

group = ProjectSettings.PROJECT_ID
version = ProjectSettings.getVersionName(project)

allprojects {
    project.dependencyLocking.lockAllConfigurations()

    apply(plugin = rootProject.libs.plugins.kover.get().pluginId).also {
        rootProject.dependencies.add("kover", project(path))
        kover.reports.filters.excludes.annotatedBy(
            "com.oztechan.ccc.android.ui.compose.annotations.ThemedPreviews",
            "androidx.compose.ui.tooling.preview.Preview",
            "androidx.compose.runtime.Composable"
        )
    }

    apply(plugin = rootProject.libs.plugins.detekt.get().pluginId).also {
        detekt {
            buildUponDefaultConfig = true
            allRules = true
            parallel = true
            config.from(rootProject.layout.projectDirectory.file("detekt.yml"))
        }

        tasks.withType<Detekt> {
            // Use providers to avoid direct project references
            val projectDirectory = layout.projectDirectory.asFile
            val buildDirectory = layout.buildDirectory.asFile

            setSource(projectDirectory)
            exclude("**/build/**")
            exclude {
                val relativePath = it.file.relativeTo(projectDirectory)
                relativePath.startsWith(buildDirectory.get().relativeTo(projectDirectory))
            }
        }

        tasks.register("detektAll") {
            dependsOn(tasks.withType<Detekt>())
        }

        dependencies {
            detektPlugins(rootProject.libs.common.detektFormatting)
        }
    }
    tasks.withType<KotlinCompile> {
        compilerOptions {
            // todo remove when not needed anymore
            freeCompilerArgs.add("-Xexpect-actual-classes")
            allWarningsAsErrors = true
        }
    }
}

tasks.findByName("dependencies")?.let {
    allprojects.forEach { prj ->
        if (prj != rootProject) it.dependsOn("${prj.path}:dependencies")
    }
}
