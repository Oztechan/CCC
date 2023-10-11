/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
import io.gitlab.arturbosch.detekt.Detekt
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    libs.plugins.apply {
        alias(kover)
        alias(detekt)
    }
}

buildscript {
    dependencies {
        libs.classpaths.apply {
            classpath(androidGradlePlugin)
            classpath(kotlinGradlePlugin)
            classpath(gsm)
            classpath(firebasePerPlugin)
            classpath(crashlytics)
            classpath(navigation) // todo can be removed once compose migration done
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
        rootProject.dependencies.add("kover", project(path))
        koverReport {
            if (pluginManager.hasPlugin(rootProject.libs.plugins.androidLib.get().pluginId)) {
                defaults {
                    // adds the contents of the reports of `release` Android build variant to default reports
                    mergeWith("*")
                }
            }
            filters {
                excludes {
                    annotatedBy("com.oztechan.ccc.android.ui.compose.annotations.ThemedPreviews")
                    annotatedBy("androidx.compose.ui.tooling.preview.Preview")
                    annotatedBy("androidx.compose.runtime.Composable")
                }
            }
        }
    }

    apply(plugin = rootProject.libs.plugins.detekt.get().pluginId).also {
        detekt {
            buildUponDefaultConfig = true
            allRules = true
            parallel = true
            config.from("${rootProject.projectDir}/detekt.yml")
        }
        tasks.withType<Detekt> {
            setSource(files(project.projectDir))
            exclude("**/build/**")
            exclude {
                it.file.relativeTo(projectDir).startsWith(
                    project.layout.buildDirectory.asFile.get().relativeTo(projectDir)
                )
            }
        }.onEach { detekt ->
            // skip detekt tasks unless a it is specifically called
            detekt.onlyIf {
                gradle.startParameter.taskNames.any { it.contains("detekt") }
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
        kotlinOptions {
            allWarningsAsErrors = true
        }
    }
}
