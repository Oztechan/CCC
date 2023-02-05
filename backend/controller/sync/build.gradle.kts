plugins {
    @Suppress("DSL_SCOPE_VIOLATION")
    id(libs.plugins.jvm.get().pluginId)
}

dependencies {
    libs.common.apply {
        implementation(kermit)
        implementation(coroutines)
        implementation(koinCore)
    }

    Modules.Backend.Service.apply {
        implementation(project(free))
        implementation(project(premium))
    }

    Modules.Common.DataSource.apply {
        implementation(project(conversion))
    }
    Modules.Common.Core.apply {
        implementation(project(model))
    }
}
