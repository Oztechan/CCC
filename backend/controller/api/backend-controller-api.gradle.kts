plugins {
    @Suppress("DSL_SCOPE_VIOLATION")
    libs.plugins.apply {
        id(jvm.get().pluginId)
        alias(ksp)
    }
}

dependencies {
    libs.common.apply {
        implementation(kermit)
        implementation(koinCore)

        testImplementation(mockative)
        testImplementation(coroutinesTest)
        testImplementation(test)
    }

    kspTest(libs.processors.mockative)

    Modules.Common.Core.apply {
        implementation(project(network))
        implementation(project(model))
    }

    Modules.Common.DataSource.apply {
        implementation(project(conversion))
    }
}
