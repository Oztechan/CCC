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

// submodules
include(
    ":log",
    ":scope",
    ":base",
    ":parser"
)

project(":log").projectDir = file("log/logmob")
project(":scope").projectDir = file("scope/scopemob")
project(":base").projectDir = file("base/basemob")
project(":parser").projectDir = file("parser/parsermob")
