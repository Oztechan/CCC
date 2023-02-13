plugins {
    @Suppress("DSL_SCOPE_VIOLATION")
    libs.plugins.apply {
        id(androidLib.get().pluginId)
        id(android.get().pluginId)
        alias(ksp)
    }
}

android {
    namespace = Modules.Android.ViewModel.widget.packageName
    compileSdk = ProjectSettings.COMPILE_SDK_VERSION
}

dependencies {
    libs.common.apply {
        implementation(koinCore)
    }

    libs.common.apply {
        testImplementation(test)
        testImplementation(mockative)
        testImplementation(coroutinesTest)
    }

    kspTest(libs.processors.mockative)

    Modules.Common.Core.apply {
        implementation(project(model))
    }

    Modules.Client.Core.apply {
        implementation(project(viewModel))
        implementation(project(shared))
    }

    Modules.Client.Storage.apply {
        implementation(project(app))
        implementation(project(calculation))
    }

    Modules.Client.DataSource.apply {
        implementation(project(currency))
    }

    Modules.Client.Service.apply {
        implementation(project(backend))
    }
}
