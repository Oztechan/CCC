plugins {
    libs.plugins.apply {
        alias(androidLibrary)
        alias(kotlinAndroid)
        alias(mokkery)
    }
}

android {
    ProjectSettings.apply {
        namespace = Modules.Android.ViewModel.widget.packageName
        compileSdk = COMPILE_SDK_VERSION
        defaultConfig.minSdk = MIN_SDK_VERSION

        compileOptions {
            sourceCompatibility = JAVA_VERSION
            targetCompatibility = JAVA_VERSION
        }
    }
}

dependencies {
    libs.apply {
        android.apply {
            implementation(lifecycleViewmodel)
        }
        common.apply {
            implementation(koinCore)
            implementation(coroutines)
            implementation(kermit)

            testImplementation(test)
            testImplementation(coroutinesTest)
        }
    }

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
