/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

plugins {
    with(Plugins) {
        kotlin(JS)
    }
}

dependencies {
    with(Dependencies.JS) {
        implementation(KOTLIN_X_HTML)
        implementation(KOTLIN_REACT)
        implementation(KOTLIN_REACT_DOM)
    }

    with(Dependencies.Common) {
        implementation(KOIN_CORE)
    }

    with(Modules) {
        implementation(project(CLIENT))
        implementation(project(LOG_MOB))
    }
}

kotlin {
    js {
        useCommonJs()
        browser {
            binaries.executable()
        }
    }
}
