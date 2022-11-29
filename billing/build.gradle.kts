import config.DeviceFlavour

plugins {
    with(Dependencies.Plugins) {
        id(ANDROID_LIB)
        kotlin(ANDROID)
    }
}

@Suppress("UnstableApiUsage")
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

    DeviceFlavour.googleApi(Dependencies.Android.GOOGLE.BILLING)

    implementation(Dependencies.Common.KOIN_CORE)

    with(Dependencies.Android) {
        implementation(LIFECYCLE_RUNTIME)
    }

    with(Dependencies.Modules) {
        implementation(project(LOGMOB.path))
        implementation(project(SCOPEMOB.path))
    }
}
