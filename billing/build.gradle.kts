import config.DeviceFlavour

plugins {
    @Suppress("DSL_SCOPE_VIOLATION")
    with(libs.plugins) {
        id(androidLib.get().pluginId)
        id(android.get().pluginId)
    }
}

@Suppress("UnstableApiUsage")
android {
    with(ProjectSettings) {
        namespace = Modules.BILLING.packageName
        compileSdk = COMPILE_SDK_VERSION

        defaultConfig {
            minSdk = MIN_SDK_VERSION
            targetSdk = TARGET_SDK_VERSION
        }
    }

    with(DeviceFlavour) {
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
    with(libs) {
        with(common) {
            implementation(koinCore)
        }

        with(android) {
            implementation(lifecycleRuntime)

            with(google) {
                @Suppress("UnstableApiUsage")
                DeviceFlavour.googleApi(billing)
            }
        }
    }

    with(Modules) {
        implementation(project(LOGMOB))
        implementation(project(SCOPEMOB))
    }
}
