import config.DeviceFlavour
import config.DeviceFlavour.Companion.implementation

plugins {
    libs.plugins.apply {
        alias(androidLibrary)
        alias(kotlinAndroid)
        alias(safeArgs) // todo can be removed once compose migration done
        alias(jetbrainsCompose)
    }
}

android {
    ProjectSettings.apply {
        namespace = Modules.Android.UI.mobile.packageName
        compileSdk = COMPILE_SDK_VERSION
        defaultConfig.minSdk = MIN_SDK_VERSION

        compileOptions {
            sourceCompatibility = JAVA_VERSION
            targetCompatibility = JAVA_VERSION
        }
    }

    buildFeatures {
        viewBinding = true
        compose = true
        buildConfig = true
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
        compose.apply {
            implementation(material3)
            debugImplementation(uiTooling)
            implementation(preview)
        }
        android.apply {
            implementation(activityCompose)
            implementation(composeNavigation)
            implementation(androidMaterial)
            implementation(constraintLayout)
            implementation(navigation)
            implementation(koinAndroid)
            implementation(koinCompose)
            implementation(lifecycleRuntime)
            implementation(splashScreen)

            // todo can be removed when SearchView is removed: https://github.com/Oztechan/CCC/issues/3272
            implementation(appCompat)
            implementation(appCompatResources)
        }

        android.google.apply {
            DeviceFlavour.GOOGLE.implementation(playCore)
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

    Modules.Client.ViewModel.apply {
        implementation(project(main))
        implementation(project(calculator))
        implementation(project(currencies))
        implementation(project(settings))
        implementation(project(selectCurrency))
        implementation(project(watchers))
        implementation(project(premium))
    }

    Submodules.apply {
        implementation(scopemob)
        implementation(basemob)
    }
}
