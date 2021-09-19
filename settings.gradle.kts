/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

// FE
include(
    ":android",
    ":backend",
    ":client", // Shared with all FE targets
    ":common", // Shared with all FE & BE targets
    ":billing" // android billing
)

// SubMob
include(
    ":basemob",
    ":scopemob",
    ":logmob",
    ":parsermob"
)


project(":basemob").projectDir = file("basemob/basemob")
project(":scopemob").projectDir = file("scopemob/scopemob")
project(":logmob").projectDir = file("logmob/logmob")
project(":parsermob").projectDir = file("parsermob/parsermob")
