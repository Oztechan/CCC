plugins {
    alias(libs.plugins.kotlinJvm)
}

dependencies {
    libs.common.apply {
        testImplementation(konsist)
    }
    libs.jvm.apply {
        testImplementation(test)
    }
}
