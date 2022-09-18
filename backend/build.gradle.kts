/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


plugins {
    with(Dependencies.Plugins) {
        application
        kotlin(MULTIPLATFORM)
        id(KSP) version (Versions.KSP)
    }
}

with(ProjectSettings) {
    application {
        mainClass.set("$PROJECT_ID.backend.ApplicationKt")
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
                with(Dependencies.JVM) {
                    implementation(KTOR_CORE)
                    implementation(KTOR_NETTY)
                    implementation(LOG_BACK)
                }

                with(Dependencies.Common) {
                    implementation(KOIN_CORE)
                    implementation(LOG_MOB)
                }

                with(Dependencies.Modules) {
                    implementation(project(COMMON))
                }
            }
        }

        val jvmTest by getting {
            dependencies {
                with(Dependencies.Common) {
                    implementation(MOCKATIVE)
                    implementation(COROUTINES_TEST)
                }
                implementation(project(Dependencies.Modules.TEST))
            }
        }
    }
}

dependencies {
    ksp(Dependencies.Processors.MOCKATIVE)
}

ksp {
    arg("mockative.stubsUnitByDefault", "true")
}


tasks.register<Jar>("fatJar") {
    archiveBaseName.set("${project.name}-fat")
    manifest {
        attributes["Implementation-Title"] = "Gradle Jar File Example"
        attributes["Implementation-Version"] = ProjectSettings.getVersionName(project)
        attributes["Main-Class"] = "${ProjectSettings.PROJECT_ID}.backend.ApplicationKt"
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
