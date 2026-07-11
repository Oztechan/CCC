/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositories {
        google()
        mavenCentral()
        maven("https://developer.huawei.com/repo/")
        // SubMob shared libraries publish -SNAPSHOT builds here; releases resolve via mavenCentral().
        // Scoped to SubMob snapshots so Gradle never queries it for anything else.
        maven("https://central.sonatype.com/repository/maven-snapshots/") {
            mavenContent { snapshotsOnly() }
            content { includeGroup("com.github.submob") }
        }
    }
}

include(
    // region Android only modules
    ":android:app",
    // Core modules
    ":android:core:billing",
    ":android:core:ad",
    // UI modules
    ":android:ui:mobile",
    ":android:ui:widget",
    // ViewModel modules
    ":android:viewmodel:widget",
    // endregion

    // region iOS only modules
    ":ios:provider",
    // Repository modules
    ":ios:repository:background",
    // endregion

    // region Backend only modules
    ":backend:app",
    // Service modules
    ":backend:service:premium",
    // Controller modules
    ":backend:controller:sync",
    ":backend:controller:api",
    // endregion

    // region Client only modules Android+iOS
    // Core modules
    ":client:core:viewmodel",
    ":client:core:shared",
    ":client:core:res",
    ":client:core:analytics",
    ":client:core:persistence",
    ":client:core:remoteconfig",
    // Storage modules
    ":client:storage:app",
    ":client:storage:calculation",
    // DataSource modules
    ":client:datasource:currency",
    ":client:datasource:watcher",
    // Service modules
    ":client:service:backend",
    // ConfigService modules
    ":client:configservice:ad",
    ":client:configservice:review",
    ":client:configservice:update",
    // Repository modules
    ":client:repository:adcontrol",
    ":client:repository:appconfig",
    // ViewModel modules
    ":client:viewmodel:main",
    ":client:viewmodel:calculator",
    ":client:viewmodel:currencies",
    ":client:viewmodel:settings",
    ":client:viewmodel:selectcurrency",
    ":client:viewmodel:watchers",
    ":client:viewmodel:premium",
    // endregion

    // region Common only modules Android+iOS+Backend
    // Core modules
    ":common:core:database",
    ":common:core:network",
    ":common:core:infrastructure",
    ":common:core:model",
    // DataSource modules
    ":common:datasource:conversion",
    // endregion
    ":test",
)

// region SubMob shared libraries
// Co-develop from the sibling Oztechan/SubMob checkouts when present (edits show up instantly via a
// Gradle composite build); otherwise — CI, or a clone without the siblings — resolve the published
// versions declared in gradle/libs.versions.toml. Substitution is explicit because vanniktech sets
// the module group late, which the automatic composite substitution can miss.
listOf(
    "LogMob",
    "ScopeMob",
    "BaseMob",
    "ParserMob",
).forEach { dirName ->
    val artifact = dirName.lowercase()
    val dir = file("../../SubMob/$dirName")
    if (dir.isDirectory) {
        includeBuild(dir) {
            dependencySubstitution {
                substitute(module("com.github.submob:$artifact")).using(project(":$artifact"))
            }
        }
    }
}
// endregion

rootProject.name = "CCC"
rootProject.updateBuildFileNames()

fun ProjectDescriptor.updateBuildFileNames() {
    buildFileName = path
        .drop(1)
        .replace(":", "-")
        .dropLastWhile { it != '-' }
        .plus(name)
        .plus(".gradle.kts")

    if (children.isNotEmpty()) {
        children.forEach { it.updateBuildFileNames() }
    }
}
