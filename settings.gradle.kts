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
    ":android", // android app
    ":backend", // backend app
    // ios -> not a gradle module

    // KMP modules
    ":common", // Shared with all FE & BE targets
    ":client", // Shared with all FE targets

    ":res", // Shared with all FE targets for resources

    ":config", // Shared with all FE targets for Firebase Remote Config
    ":analytics", // Shared with all FE targets for Google Analytics

    ":test", // common test classes

    ":provider", // umbrella framework for iOS libraries

    ":billing", // android only billing module
    ":ad", // android only ad module

    // submodules
    ":logmob", // KMP, logger library
    ":scopemob", // KMP, hand scope functions
    ":basemob", // android only
    ":parsermob" // KMP, parsing library
)

project(":logmob").projectDir = file("logmob/logmob")
project(":scopemob").projectDir = file("scopemob/scopemob")
project(":basemob").projectDir = file("basemob/basemob")
project(":parsermob").projectDir = file("parsermob/parsermob")
