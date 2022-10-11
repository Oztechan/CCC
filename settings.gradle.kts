/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

include(
    ":android",
    ":billing",
    ":ad",
    ":analytics"
)

include(":backend")

// KMP
include(
    ":client", // Shared with all FE targets
    ":res", // Shared with all FE targets
    ":config", // Shared with all FE targets
    ":common", // Shared with all FE & BE targets
    ":test"
)

// native
include(":provider") // used for combining iOS libraries and injecting as pod

// submodules
include(
    ":logmob",
    ":scopemob",
    ":basemob",
    ":parsermob"
)

project(":logmob").projectDir = file("logmob/logmob")
project(":scopemob").projectDir = file("scopemob/scopemob")
project(":basemob").projectDir = file("basemob/basemob")
project(":parsermob").projectDir = file("parsermob/parsermob")
