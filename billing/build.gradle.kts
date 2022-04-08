import config.DeviceFlavour

plugins {
    with(Dependencies.Plugins) {
        id(ANDROID_LIB)
        kotlin(ANDROID)
    }
}

android {
    with(ProjectSettings) {
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

    with(DeviceFlavour) {
        googleApi(Dependencies.Android.GOOGLE.BILLING)
    }

    with(Dependencies.Android) {
        implementation(LIFECYCLE_RUNTIME)
    }

    with(Dependencies.Common) {
        implementation(SCOPE_MOB)
    }

    with(Dependencies.Modules) {
        implementation(project(LOG_MOB))
    }
}
