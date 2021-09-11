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
}

dependencies {
    with(Dependencies.Android) {
        api(BILLING)
        implementation(KOIN_ANDROID)
        implementation(LIFECYCLE_RUNTIME)
    }

    with(Modules) {
        implementation(project(SCOPE_MOB))
        implementation(project(LOG_MOB))
    }
}