import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import com.codingfeline.buildkonfig.gradle.BuildKonfigExtension
import config.key.Key
import config.key.secret

plugins {
    libs.plugins.apply {
        alias(kotlinMultiplatform)
        id(kotlinXSerialization.get().pluginId)
        alias(androidLibrary)
        alias(buildKonfig)
    }
}

kotlin {
    @Suppress("OPT_IN_USAGE")
    targetHierarchy.default()

    androidTarget()

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    jvm()

    @Suppress("UNUSED_VARIABLE")
    sourceSets {
        val commonMain by getting {
            dependencies {
                libs.common.apply {
                    implementation(coroutines)
                    implementation(koinCore)
                    implementation(ktorLogging)
                    implementation(ktorClientContentNegotiation)
                    implementation(ktorJson)
                }
                implementation(project(Modules.Common.Core.model))
            }
        }
        val commonTest by getting {
            dependencies {
                libs.common.apply {
                    implementation(test)
                    implementation(coroutinesTest)
                }
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(libs.android.ktor)
            }
        }
        val iosMain by getting {
            dependencies {
                implementation(libs.ios.ktor)
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(libs.jvm.ktor)
            }
        }
    }
}

android {
    ProjectSettings.apply {
        namespace = Modules.Common.Core.network.packageName
        compileSdk = COMPILE_SDK_VERSION
        defaultConfig.minSdk = MIN_SDK_VERSION

        compileOptions {
            sourceCompatibility = JAVA_VERSION
            targetCompatibility = JAVA_VERSION
        }
    }
}

configure<BuildKonfigExtension> {
    packageName = Modules.Common.Core.network.packageName

    defaultConfigs {
        buildConfigField(STRING, Key.BASE_URL_BACKEND.name, secret(Key.BASE_URL_BACKEND), const = true)
        buildConfigField(STRING, Key.BASE_URL_API.name, secret(Key.BASE_URL_API), const = true)
        buildConfigField(STRING, Key.BASE_URL_API_PREMIUM.name, secret(Key.BASE_URL_API_PREMIUM), const = true)
        buildConfigField(STRING, Key.API_KEY_PREMIUM.name, secret(Key.API_KEY_PREMIUM), const = true)
    }
}
