plugins {
    @Suppress("DSL_SCOPE_VIOLATION")
    libs.plugins.apply {
        id(androidLib.get().pluginId)
        id(android.get().pluginId)
        alias(ksp)
    }
}

android {
    ProjectSettings.apply {
        namespace = Modules.Android.ViewModel.premium.packageName
        compileSdk = COMPILE_SDK_VERSION
        defaultConfig.minSdk = MIN_SDK_VERSION
    }
}

dependencies {
    libs.common.apply {
        implementation(koinCore)
        implementation(coroutines)
        implementation(kermit)
        implementation(libs.android.lifecycleViewmodel)

        testImplementation(test)
        testImplementation(mockative)
        testImplementation(coroutinesTest)
    }

    kspTest(libs.processors.mockative)

    Modules.Client.Core.apply {
        implementation(project(viewModel))
        implementation(project(shared))
    }

    Modules.Client.Storage.apply {
        implementation(project(app))
    }

    Modules.Submodules.apply {
        implementation(project(scopemob))
    }
}
