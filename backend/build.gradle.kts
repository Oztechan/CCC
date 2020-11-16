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
    implementation(project(Modules.common))

    with(Dependencies.JVM) {
        implementation(ktorCore)
        implementation(ktorNetty)
        implementation(ktorWebSockets)
        implementation(ktorSerialization)
        implementation(logBack)
    }
}

application {
    @Suppress("UnstableApiUsage")
    mainClass.set("${ProjectSettings.projectId}.backend.BackendAppKt")
}
