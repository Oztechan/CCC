import config.DeviceFlavour
import config.DeviceFlavour.Companion.implementation

plugins {
    libs.plugins.apply {
        alias(androidLibrary)
        alias(kotlinAndroid)
    }
}

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
                DeviceFlavour.GOOGLE.implementation(billing)
            }
        }
    }

    Submodules.apply {
        implementation(scopemob)
    }
}
