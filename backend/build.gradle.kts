/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


plugins {
    with(Plugins) {
        application
        kotlin(MULTIPLATFORM)
    }
}

with(ProjectSettings) {
    application {
        mainClass.set("$PACKAGE_NAME.backend.BackendAppKt")
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
                    implementation(KTOR_SERIALIZATIONM)
                    implementation(LOG_BACK)
                }

                with(Dependencies.Common) {
                    implementation(KOIN_CORE)
                }

                with(Modules) {
                    implementation(project(COMMON))
                    implementation(project(LOG_MOB))
                }
            }
        }
    }
}

tasks.register<Jar>("fatJar") {
    archiveBaseName.set("${project.name}-fat")
    manifest {
        attributes["Implementation-Title"] = "Gradle Jar File Example"
        attributes["Implementation-Version"] = ProjectSettings.getVersionName(project)
        attributes["Main-Class"] = "${ProjectSettings.PACKAGE_NAME}.backend.BackendAppKt"
    }
    from(configurations.runtimeClasspath.get().map { file: File ->
        if (file.isDirectory) {
            file
        } else {
            zipTree(file)
        }
    })
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
