/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.INT
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import com.codingfeline.buildkonfig.gradle.BuildKonfigExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


plugins {
    with(Dependencies.Plugins) {
        application
        kotlin(MULTIPLATFORM)
        id(BUILD_KONFIG)
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
        val commonMain by getting
        val commonTest by getting

        val jvmMain by getting {
            dependsOn(commonMain)

            dependencies {
                with(Dependencies.JVM) {
                    implementation(KTOR_CORE)
                    implementation(KTOR_NETTY)
                    implementation(LOG_BACK)
                }

                with(Dependencies.Common) {
                    implementation(KOIN_CORE)
                }

                with(Dependencies.Modules) {
                    implementation(project(COMMON))
                    implementation(project(LOGMOB))
                }
            }
        }

        val jvmTest by getting {
            dependsOn(commonTest)

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

configure<BuildKonfigExtension> {
    packageName = "${ProjectSettings.PROJECT_ID}.backend"

    defaultConfigs {
        buildConfigField(INT, "versionCode", ProjectSettings.getVersionCode(project).toString(), const = true)
        buildConfigField(STRING, "versionName", ProjectSettings.getVersionName(project), const = true)
    }
}
