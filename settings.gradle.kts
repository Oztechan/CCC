/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
rootProject.name = "androidCCC"
include(
    ":app",
    ":ui",
    ":data",
    ":scopemob", ":logmob"
)

project(":scopemob").projectDir = file("scopemob/submob")
project(":logmob").projectDir = file("logmob/submob")
