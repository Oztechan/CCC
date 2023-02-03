/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
import io.gitlab.arturbosch.detekt.Detekt
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    @Suppress("DSL_SCOPE_VIOLATION")
    libs.plugins.apply {
        alias(kover)
        alias(detekt)
        id("com.autonomousapps.dependency-analysis") version "1.13.1"
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
        koverMerged {
            filters {
                annotations {
                    excludes += listOf(
                        "com.oztechan.ccc.android.ui.compose.annotations.ThemedPreviews",
                        "androidx.compose.ui.tooling.preview.Preview",
                        "androidx.compose.runtime.Composable"
                    )
                }
            }
            enable()
        }
    }

    apply(plugin = rootProject.libs.plugins.detekt.get().pluginId).also {
        detekt {
            buildUponDefaultConfig = true
            allRules = true
            parallel = true
            config = files("${rootProject.projectDir}/detekt.yml")
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

        dependencies {
            detektPlugins(rootProject.libs.common.detektFormatting)
        }
    }

    apply(plugin = "com.autonomousapps.dependency-analysis")

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            allWarningsAsErrors = true
        }
    }
}
