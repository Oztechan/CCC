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
    ":android:feature:mobile", // android mobile app
    ":android:feature:widget", // android widget
    ":backend", // backend app
    // ios -> not a gradle module

    // KMP modules
    ":common", // Shared with all FE & BE targets
    ":common:core:database",
    ":common:core:network",
    ":common:core:infrastructure",
    ":common:core:model",
    ":common:data:service:free",
    ":common:data:datasource:currency",
    ":common:data:service:premium",
    ":common:data:service:backend",

    ":client", // Shared with all FE targets

    ":res", // Shared with all FE targets for resources

    ":config", // Shared with all FE targets for Firebase Remote Config
    ":analytics", // Shared with all FE targets for Google Analytics

    ":test", // common test classes

    ":provider", // umbrella framework for iOS libraries

    ":billing", // android only billing module
    ":ad", // android only ad module

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
