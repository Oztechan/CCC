/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

enableFeaturePreview("GRADLE_METADATA")

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        jcenter()
        mavenCentral()
    }
}

include(
    ":android",
    ":backend",
    ":web",
    ":client",
    ":common",
    ":calculator",
    ":basemob", ":scopemob", ":logmob"
)

project(":basemob").projectDir = file("basemob/basemob")
project(":scopemob").projectDir = file("scopemob/scopemob")
project(":logmob").projectDir = file("logmob/logmob")
