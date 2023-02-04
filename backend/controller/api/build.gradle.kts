plugins {
    @Suppress("DSL_SCOPE_VIOLATION")
    id(libs.plugins.jvm.get().pluginId)
}

dependencies {
    libs.common.apply {
        implementation(kermit)
        implementation(koinCore)

        testImplementation(mockative)
        testImplementation(coroutinesTest)
        testImplementation(test)
    }

    Modules.Common.Core.apply {
        implementation(project(network))
        implementation(project(model))
    }

    Modules.Common.DataSource.apply {
        implementation(project(conversion))
    }
}
