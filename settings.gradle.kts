/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
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
    ":data",
    ":basemob", ":scopemob", ":logmob"
)

project(":basemob").projectDir = file("basemob/submob")
project(":scopemob").projectDir = file("scopemob/submob")
project(":logmob").projectDir = file("logmob/submob")
