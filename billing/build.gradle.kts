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
        googleApi(Dependencies.Android.GOOGLE.BILLING)
    }

    with(Dependencies.Android) {
        implementation(LIFECYCLE_RUNTIME)
    }

    with(Dependencies.Modules) {
        implementation(project(SCOPE_MOB))
        implementation(project(LOG_MOB))
    }
}
