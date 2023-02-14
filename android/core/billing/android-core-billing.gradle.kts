import config.DeviceFlavour

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
        namespace = Modules.Android.Core.billing.packageName
        compileSdk = COMPILE_SDK_VERSION
        defaultConfig.minSdk = MIN_SDK_VERSION

        compileOptions {
            sourceCompatibility = JAVA_VERSION
            targetCompatibility = JAVA_VERSION
        }
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
            implementation(koinCore)
            implementation(kermit)
        }

        android.apply {
            implementation(lifecycleRuntime)

            google.apply {
                @Suppress("UnstableApiUsage")
                DeviceFlavour.googleImplementation(billing)
            }
        }
    }

    Modules.Submodules.apply {
        implementation(project(scopemob))
    }
}
