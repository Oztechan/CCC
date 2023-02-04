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
    }
}

include(
    // Targets
    ":android:app", // android app
    ":android:core:billing", // android only billing module
    ":android:core:ad", // android only ad module
    ":android:feature:mobile", // android mobile app
    ":android:feature:widget", // android widget

    ":ios:provider",

    ":backend", // backend app
    ":backend:service:free",
    ":backend:service:premium",

    // KMP modules
    // Shared with all FE & BE targets
    ":common:core:database",
    ":common:core:network",
    ":common:core:infrastructure",
    ":common:core:model",
    ":common:datasource:conversion",

    ":client:core:viewmodel",
    ":client:core:shared",
    ":client:core:res",
    ":client:core:analytics",
    ":client:core:persistence",
    ":client:core:remoteconfig",
    ":client:storage:app",
    ":client:storage:calculation",
    ":client:datasource:currency",
    ":client:datasource:watcher",
    ":client:service:backend",
    ":client:configservice:ad",
    ":client:configservice:review",
    ":client:configservice:update",
    ":client:repository:adcontrol",
    ":client:repository:background",
    ":client:repository:appconfig",
    ":client:viewmodel:main",
    ":client:viewmodel:calculator",
    ":client:viewmodel:currencies",
    ":client:viewmodel:settings",
    ":client:viewmodel:selectcurrency",
    ":client:viewmodel:premium",
    ":client:viewmodel:watchers",
    ":client:viewmodel:widget",

    // submodules
    ":submodule:logmob", // KMP, logger library
    ":submodule:scopemob", // KMP, hand scope functions
    ":submodule:basemob", // android only
    ":submodule:parsermob" // KMP, parsing library
)

project(":submodule:logmob").projectDir = file("submodule/logmob/logmob")
project(":submodule:scopemob").projectDir = file("submodule/scopemob/scopemob")
project(":submodule:basemob").projectDir = file("submodule/basemob/basemob")
project(":submodule:parsermob").projectDir = file("submodule/parsermob/parsermob")
