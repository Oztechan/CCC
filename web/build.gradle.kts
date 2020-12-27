/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

plugins {
    with(Plugins) {
        kotlin(js)
        kotlin(serializationPlugin)
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
        implementation(kermit)
    }

    with(Modules) {
        implementation(project(client))
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
