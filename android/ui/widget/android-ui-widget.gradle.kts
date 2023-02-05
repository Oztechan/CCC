plugins {
    @Suppress("DSL_SCOPE_VIOLATION")
    libs.plugins.apply {
        id(androidLib.get().pluginId)
        id(android.get().pluginId)
    }
}

@Suppress("UnstableApiUsage")
android {
    ProjectSettings.apply {
        namespace = Modules.Android.UI.widget.packageName
        compileSdk = COMPILE_SDK_VERSION

        defaultConfig {
            minSdk = MIN_SDK_VERSION
            targetSdk = TARGET_SDK_VERSION
        }
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.get()
    }
}

dependencies {
    libs.apply {
        android.apply {
            implementation(glance)
        }

        common.apply {
            implementation(koinCore)
            testImplementation(test)
        }
    }

    implementation(project(Modules.Android.ViewModel.widget))

    implementation(project(Modules.Common.Core.model))

    implementation(project(Modules.Client.Core.res))
}
