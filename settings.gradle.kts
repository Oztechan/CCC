/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

// FE
include(
    ":android",
    // ":web", todo enable when implementation start
    // ":desktop", todo enable when implementation start
)

// BE
include(
    ":backend"
)

// Shared modules
include(
    ":client", // All FE targets
    ":common", // All FE & BE targets
)

// SubMob
include(
    ":basemob",
    ":scopemob",
    ":logmob",
    ":parsermob"
)

include(":billing")

project(":basemob").projectDir = file("basemob/basemob")
project(":scopemob").projectDir = file("scopemob/scopemob")
project(":logmob").projectDir = file("logmob/logmob")
project(":parsermob").projectDir = file("parsermob/parsermob")
