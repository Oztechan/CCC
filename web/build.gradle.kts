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
    implementation(project(Modules.client))
}

kotlin {
    // todo need to revert when Koin supports IR
    // https://github.com/InsertKoinIO/koin/issues/929
    js {
        useCommonJs()
        browser {
            binaries.executable()
        }
    }
}
