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
}

dependencies {
    with(Dependencies.Android) {
        implementation(FIREBASE_REMOTE_CONFIG)
    }

    with(Dependencies.Common) {
        implementation(KTOR_SETIALIZATION)
    }

    with(Dependencies.Modules) {
        implementation(project(LOG_MOB))
    }
}
