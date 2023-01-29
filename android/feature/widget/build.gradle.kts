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
        namespace = Modules.Android.Feature.widget.packageName
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
    libs.android.apply {
        implementation(glance)
        implementation(koinAndroid)
    }

    implementation(project(Modules.Client.ViewModel.widget))

    implementation(project(Modules.Common.Core.model))

    implementation(project(Modules.Client.self))

    implementation(project(Modules.Client.Core.res))

    implementation(project(Modules.Submodules.logmob))

    testImplementation(libs.common.test)
}
