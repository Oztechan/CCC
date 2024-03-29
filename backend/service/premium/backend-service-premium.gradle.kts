plugins {
    libs.plugins.apply {
        alias(kotlinJvm)
        alias(ksp)
    }
}

dependencies {
    libs.common.apply {
        implementation(koinCore)
        implementation(coroutines)
        implementation(kermit)

        testImplementation(mockative)
        testImplementation(coroutinesTest)
        testImplementation(test)
    }

    kspTest(libs.processors.mockative)

    Modules.Common.Core.apply {
        implementation(project(network))
        implementation(project(model))
        implementation(project(infrastructure))
    }
}
