/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

plugins {
    with(Plugins) {
        kotlin(js)
    }
}

dependencies {
    with(Dependencies.JS) {
        implementation(kotlinXHtml)
        implementation(kotlinReact)
        implementation(kotlinReactDom)
    }

    with(Dependencies.Common) {
        implementation(koinCore)
    }

    with(Modules) {
        implementation(project(client))
        implementation(project(logmob))
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
