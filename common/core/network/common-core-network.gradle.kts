import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import com.codingfeline.buildkonfig.gradle.BuildKonfigExtension
import config.key.Key
import config.key.secret

plugins {
    @Suppress("DSL_SCOPE_VIOLATION")
    libs.plugins.apply {
        id(multiplatform.get().pluginId)
        id(kotlinXSerialization.get().pluginId)
        id(androidLib.get().pluginId)
        id(buildKonfig.get().pluginId)
    }
}

kotlin {

    android()

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    jvm()

    @Suppress("UNUSED_VARIABLE")
    sourceSets {

        val commonMain by getting {
            dependencies {
                libs.common.apply {
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
        val androidUnitTest by getting

        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependencies {
                implementation(libs.ios.ktor)
            }
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }

        val jvmMain by getting {
            dependencies {
                implementation(libs.jvm.ktor)
            }
        }
        val jvmTest by getting
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
