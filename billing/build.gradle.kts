plugins {
    with(Plugins) {
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

    flavorDimensions.addAll(listOf(DeviceType::class.simpleName.toString()))

    productFlavors {
        create(DeviceType.GOOGLE) {
            dimension = DeviceType::class.simpleName.toString()
        }

        create(DeviceType.HUAWEI) {
            dimension = DeviceType::class.simpleName.toString()
        }
    }
}

dependencies {

    DeviceType.googleApi(Dependencies.Android.GOOGLE.BILLING)

    implementation(Dependencies.Android.LIFECYCLE_RUNTIME)

    with(Modules) {
        implementation(project(SCOPE_MOB))
        implementation(project(LOG_MOB))
    }
}
