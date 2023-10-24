plugins {
    alias(libs.plugins.kotlinJvm)
}

dependencies {
    libs.common.apply {
        testImplementation(test)
        testImplementation(konsist)
    }
}
