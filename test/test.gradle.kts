plugins {
    id(libs.plugins.jvm.get().pluginId)
}

dependencies {
    libs.common.apply {
        testImplementation(test)
        testImplementation(konsist)
    }
}
