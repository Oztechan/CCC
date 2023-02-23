/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        maven("https://dl.bintray.com/icerockdev/plugins")
    }
}
plugins {
    id("com.gradle.enterprise") version ("3.12.3")
}

gradleEnterprise {
    buildScan {
        termsOfServiceUrl = "https://gradle.com/terms-of-service"
        termsOfServiceAgree = "yes"
        publishAlways()

        obfuscation {
            username { null }
            hostname { null }
            ipAddresses { null }
        }
    }
}

dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositories {
        mavenCentral()
        google()
        maven("https://dl.bintray.com/ekito/koin")
        maven("https://dl.bintray.com/icerockdev/moko")
        maven("https://kotlin.bintray.com/kotlinx/")
        maven("https://developer.huawei.com/repo/")
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
    ":android:viewmodel:premium",
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
    ":backend:service:free",
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

    // region Git Submodules independent modules and project hosted in different repository
    ":submodule:logmob", // KMP, logger library
    ":submodule:scopemob", // KMP, hand scope functions
    ":submodule:basemob", // Android only base classes
    ":submodule:parsermob" // KMP, parsing library
    // endregion
)

project(":submodule:logmob").projectDir = file("submodule/logmob/logmob")
project(":submodule:scopemob").projectDir = file("submodule/scopemob/scopemob")
project(":submodule:basemob").projectDir = file("submodule/basemob/basemob")
project(":submodule:parsermob").projectDir = file("submodule/parsermob/parsermob")

rootProject.name = "CCC"
rootProject.updateBuildFileNames()

fun ProjectDescriptor.updateBuildFileNames() {
    if (name.startsWith("submodule")) return

    buildFileName = if (this == rootProject) {
        rootProject.name
    } else {
        path.drop(1).replace(":", "-")
    }.let {
        "$it.gradle.kts"
    }

    if (children.isNotEmpty()) {
        children.forEach { it.updateBuildFileNames() }
    }
}
