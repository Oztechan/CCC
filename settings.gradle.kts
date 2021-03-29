/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

include(
    ":android",
    ":backend",
    ":web",
//    todo desktop is removed until 1.5.0 stable release, compose desktop is not capable with Kotlin M releases
//    ":desktop",
    ":client",
    ":common",
    ":basemob", ":scopemob", ":logmob", ":parsermob"
)

project(":basemob").projectDir = file("basemob/basemob")
project(":scopemob").projectDir = file("scopemob/scopemob")
project(":logmob").projectDir = file("logmob/logmob")
project(":parsermob").projectDir = file("parsermob/parsermob")
