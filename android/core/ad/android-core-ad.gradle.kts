import config.BuildType
import config.DeviceFlavour
import config.DeviceFlavour.Companion.implementation
import config.key.FlavoredKey
import config.key.TypedKey
import config.key.resId
import config.key.secret
import config.key.string

plugins {
    libs.plugins.apply {
        alias(androidLibrary)
        alias(kotlinAndroid)
    }
}

android {
    ProjectSettings.apply {
        namespace = Modules.Android.Core.ad.packageName
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

                FlavoredKey.values().forEach {
                    resValue(string, it.name.resId, secret(it, DeviceFlavour.GOOGLE))
                }
            }

            create(huawei) {
                dimension = flavorDimension

                FlavoredKey.values().forEach {
                    resValue(string, it.name.resId, secret(it, DeviceFlavour.HUAWEI))
                }
            }
        }
    }

    buildTypes {
        getByName(BuildType.release) {
            TypedKey.values().forEach {
                resValue(string, it.name.resId, secret(it, BuildType.RELEASE))
            }
        }

        getByName(BuildType.debug) {
            TypedKey.values().forEach {
                resValue(string, it.name.resId, secret(it, BuildType.DEBUG))
            }
        }
    }
}

dependencies {
    libs.common.apply {
        implementation(koinCore)
        implementation(kermit)
    }

    libs.android.apply {
        implementation(activity)
        google.apply {
            DeviceFlavour.GOOGLE.implementation(googleAds)
        }
        huawei.apply {
            DeviceFlavour.HUAWEI.implementation(huaweiAds)
            DeviceFlavour.HUAWEI.implementation(huaweiOsm)
        }
    }
}
