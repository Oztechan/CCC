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
    ":util",
    ":base"
)

project(":log").projectDir = file("log/logmob")
project(":util").projectDir = file("util/scopemob")
project(":base").projectDir = file("base/basemob")
