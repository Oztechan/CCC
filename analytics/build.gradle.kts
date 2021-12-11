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
        implementation(FIREBASE_ANALYTICS)
        // todo https://github.com/Oztechan/CCC/issues/303
        // implementation(HUAWEI_HSM_BASE)
        implementation(ROOT_BEER)
    }
}
