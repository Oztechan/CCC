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
    libs.plugins.apply {
        application
        id(multiplatform.get().pluginId)
        id(buildKonfig.get().pluginId)
        alias(ksp)
    }
}

ProjectSettings.apply {
    application {
        mainClass.set("${Modules.backend.packageName}.ApplicationKt")
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
                libs.jvm.apply {
                    implementation(ktorCore)
                    implementation(ktorNetty)
                    implementation(koinKtor)
                }

                libs.common.apply {
                    implementation(ktorServerContentNegotiation)
                    implementation(ktorJson)
                }

                Modules.Common.Core.apply {
                    implementation(project(database))
                    implementation(project(network))
                    implementation(project(infrastructure))
                    implementation(project(model))
                }

                Modules.Common.Service.apply {
                    implementation(project(free))
                    implementation(project(premium))
                }

                Modules.Common.Datasource.apply {
                    implementation(project(conversion))
                }

                implementation(project(Modules.Submodules.logmob))
            }
        }

        val jvmTest by getting {
            dependencies {
                libs.common.apply {
                    implementation(mockative)
                    implementation(coroutinesTest)
                }
                implementation(project(Modules.test))
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
        attributes["Main-Class"] = "${Modules.backend.packageName}.ApplicationKt"
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
    packageName = Modules.backend.packageName

    defaultConfigs { } // none

    targetConfigs {
        create(KotlinPlatformType.jvm.name) {
            buildConfigField(INT, "versionCode", ProjectSettings.getVersionCode(project).toString(), const = true)
            buildConfigField(STRING, "versionName", ProjectSettings.getVersionName(project), const = true)
        }
    }
}
