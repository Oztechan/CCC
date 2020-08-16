/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
rootProject.name = "androidCCC"
include(
    ":app",
    ":ui",
    ":data",
    ":scopemob", ":basemob", ":logmob"
)

project(":basemob").projectDir = file("basemob/submob")
project(":scopemob").projectDir = file("scopemob/submob")
project(":logmob").projectDir = file("logmob/submob")
