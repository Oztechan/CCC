plugins {
    alias(libs.plugins.kotlinJvm)
}

dependencies {
    libs.common.apply {
        implementation(kermit)
        implementation(coroutines)
        implementation(koinCore)
    }

    Modules.Backend.Service.apply {
        implementation(project(premium))
    }

    Modules.Common.DataSource.apply {
        implementation(project(conversion))
    }
    Modules.Common.Core.apply {
        implementation(project(model))
    }
}
