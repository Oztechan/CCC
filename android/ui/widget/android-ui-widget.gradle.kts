plugins {
    libs.plugins.apply {
        alias(androidLibrary)
        alias(kotlinAndroid)
        alias(kotlinPluginCompose)
    }
}

android {
    ProjectSettings.apply {
        namespace = Modules.Android.UI.widget.packageName
        compileSdk = COMPILE_SDK_VERSION
        defaultConfig.minSdk = MIN_SDK_VERSION

        compileOptions {
            sourceCompatibility = JAVA_VERSION
            targetCompatibility = JAVA_VERSION
        }
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    libs.apply {
        android.apply {
            implementation(glance)
            implementation(lifecycleViewmodel)
        }

        common.apply {
            implementation(koinCore)
            implementation(kermit)
        }
    }

    implementation(project(Modules.Android.ViewModel.widget))

    implementation(project(Modules.Common.Core.model))

    Modules.Client.Core.apply {
        implementation(project(viewModel))
        implementation(project(res))
        implementation(project(analytics))
    }
}
