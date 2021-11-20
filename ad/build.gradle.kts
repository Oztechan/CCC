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

    with(Build.Flavor) {
        flavorDimensions.addAll(listOf(getFlavorName()))

        productFlavors {
            create(GOOGLE) {
                dimension = getFlavorName()
            }

            create(HUAWEI) {
                dimension = getFlavorName()
            }
        }
    }
}

dependencies {
    with(Build.Flavor.Implementation) {
        googleImplementation(Dependencies.Android.GOOGLE.ADMOB)
    }

    with(Dependencies.Modules) {
        implementation(project(LOG_MOB))
    }
}
