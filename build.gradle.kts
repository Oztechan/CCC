/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import io.gitlab.arturbosch.detekt.Detekt
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    @Suppress("DSL_SCOPE_VIOLATION")
    libs.plugins.apply {
        alias(dependencyUpdates)
        alias(kover)
        alias(detekt)
        alias(sonarqube)
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
                classes {
                    excludes.addAll(buildTestPackagePaths())
                }
                annotations {
                    excludes.addAll(
                        listOf(
                            "com.oztechan.ccc.android.ui.compose.annotations.ThemedPreviews",
                            "androidx.compose.ui.tooling.preview.Preview",
                            "androidx.compose.runtime.Composable"
                        )
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

    apply(plugin = rootProject.libs.plugins.sonarqube.get().pluginId).also {
        sonar {
            properties {
                property("sonar.sources", "src,$rootDir/ios")
                property("sonar.projectKey", "Oztechan_CCC")
                property("sonar.organization", "oztechan")
                property("sonar.host.url", "https://sonarcloud.io")
            }
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
