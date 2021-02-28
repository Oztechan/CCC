/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

enableFeaturePreview("GRADLE_METADATA")

include(
    ":android",
    ":backend",
    ":web",
    ":desktop",
    ":client",
    ":common",
    ":calculator",
    ":basemob", ":scopemob", ":logmob"
)

project(":basemob").projectDir = file("basemob/basemob")
project(":scopemob").projectDir = file("scopemob/scopemob")
project(":logmob").projectDir = file("logmob/logmob")
