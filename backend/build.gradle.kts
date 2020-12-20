/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

plugins {
    with(Plugins) {
        kotlin(platformJvm)
        kotlin(serializationPlugin)
    }
    application
}

dependencies {
    with(Dependencies.JVM) {
        implementation(ktorCore)
        implementation(ktorNetty)
        implementation(ktorWebSockets)
        implementation(ktorSerialization)
        implementation(logBack)
    }

    with(Dependencies.Common) {
        implementation(koinCore)
        implementation(kermit)
    }

    with(Modules) {
        implementation(project(common))
    }
}

application {
    @Suppress("UnstableApiUsage")
    mainClass.set("com.github.mustafaozhan.ccc.backend.BackendAppKt")
}
