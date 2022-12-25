/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.INT
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import com.codingfeline.buildkonfig.gradle.BuildKonfigExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


plugins {
    @Suppress("DSL_SCOPE_VIOLATION")
    with(libs.plugins) {
        application
        id(multiplatform.get().pluginId)
        id(buildKonfig.get().pluginId)
        alias(ksp)
    }
}

with(ProjectSettings) {
    application {
        mainClass.set("${Modules.BACKEND.packageName}.ApplicationKt")
    }
    group = PROJECT_ID
    version = getVersionName(project)
}

kotlin {
    jvm {
        withJava()
    }

    @Suppress("UNUSED_VARIABLE")
    sourceSets {
        val jvmMain by getting {
            dependencies {
                with(libs.jvm) {
                    implementation(ktorCore)
                    implementation(ktorNetty)
                    implementation(koinKtor)
                }

                with(Modules) {
                    implementation(project(COMMON))
                    implementation(project(LOGMOB))
                }
            }
        }

        val jvmTest by getting {
            dependencies {
                with(libs.common) {
                    implementation(mockative)
                    implementation(coroutinesTest)
                }
                implementation(project(Modules.TEST))
            }
        }
    }
}

dependencies {
    ksp(libs.processors.mockative)
}

ksp {
    arg("mockative.stubsUnitByDefault", "true")
}

tasks.register<Jar>("fatJar") {
    archiveBaseName.set("${project.name}-fat")
    manifest {
        attributes["Implementation-Title"] = "Gradle Jar File Example"
        attributes["Implementation-Version"] = ProjectSettings.getVersionName(project)
        attributes["Main-Class"] = "${Modules.BACKEND.packageName}.ApplicationKt"
    }
    from(
        configurations.runtimeClasspath.get().map {
            it.takeIf { it.isDirectory } ?: zipTree(it)
        }
    )
    with(tasks.jar.get() as CopySpec)
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}

@Suppress("UnstableApiUsage")
tasks.named<ProcessResources>("jvmProcessResources") {
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}

configure<BuildKonfigExtension> {
    packageName = Modules.BACKEND.packageName

    defaultConfigs { } // none

    targetConfigs {
        create(KotlinPlatformType.jvm.name) {
            buildConfigField(INT, "versionCode", ProjectSettings.getVersionCode(project).toString(), const = true)
            buildConfigField(STRING, "versionName", ProjectSettings.getVersionName(project), const = true)
        }
    }
}
