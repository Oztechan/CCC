import config.DeviceFlavour
import config.DeviceFlavour.Companion.googleImplementation

plugins {
    @Suppress("DSL_SCOPE_VIOLATION")
    libs.plugins.apply {
        id(androidLib.get().pluginId)
        id(android.get().pluginId)
        id(safeArgs.get().pluginId) // todo can be removed once compose migration done
    }
}

@Suppress("UnstableApiUsage")
android {
    ProjectSettings.apply {
        namespace = Modules.Android.UI.mobile.packageName
        compileSdk = COMPILE_SDK_VERSION
        defaultConfig.minSdk = MIN_SDK_VERSION
    }

    buildFeatures {
        viewBinding = true
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }

    DeviceFlavour.apply {
        flavorDimensions.addAll(listOf(flavorDimension))

        productFlavors {
            create(google) {
                dimension = flavorDimension
            }

            create(huawei) {
                dimension = flavorDimension
            }
        }
    }
}

dependencies {
    libs.apply {
        common.apply {
            testImplementation(test)
            implementation(kermit)
        }

        android.apply {
            implementation(composeToolingPreview)
            debugImplementation(composeTooling)
            implementation(material3)
            implementation(androidMaterial)
            implementation(composeActivity)
            implementation(composeNavigation)
            implementation(constraintLayout)
            implementation(navigation)
            implementation(koinAndroid)
            implementation(koinCompose)
            implementation(lifecycleRuntime)
            implementation(splashScreen)
        }

        android.google.apply {
            @Suppress("UnstableApiUsage")
            googleImplementation(playCore)
        }
    }

    implementation(project(Modules.Common.Core.model))

    Modules.Android.Core.apply {
        implementation(project(billing))
        implementation(project(ad))
    }

    Modules.Client.Core.apply {
        implementation(project(shared))
        implementation(project(viewModel))
        implementation(project(res))
        implementation(project(analytics))
    }

    Modules.Android.ViewModel.apply {
        implementation(project(premium))
    }

    Modules.Client.ViewModel.apply {
        implementation(project(main))
        implementation(project(calculator))
        implementation(project(currencies))
        implementation(project(settings))
        implementation(project(selectCurrency))
        implementation(project(watchers))
    }

    Modules.Submodules.apply {
        implementation(project(scopemob))
        implementation(project(basemob))
    }
}
