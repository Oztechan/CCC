import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import com.codingfeline.buildkonfig.gradle.BuildKonfigExtension
import config.key.Key
import config.key.secret

plugins {
    libs.plugins.apply {
        alias(kotlinMultiplatform)
        alias(androidKotlinMultiplatformLibrary)
        alias(buildKonfig)
        alias(serialization)
    }
}

kotlin {
    android {
        namespace = Modules.Common.Core.network.packageName
        compileSdk = ProjectSettings.COMPILE_SDK_VERSION
        minSdk = ProjectSettings.MIN_SDK_VERSION
        withHostTest {}
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    jvm()

    sourceSets {
        commonMain.dependencies {
            libs.common.apply {
                implementation(coroutines)
                implementation(koinCore)
                implementation(ktorJson)
                implementation(kermit)
            }
            libs.client.apply {
                implementation(ktorContentNegotiation)
                implementation(ktorLogging)
            }
            implementation(project(Modules.Common.Core.model))
        }
        commonTest.dependencies {
            libs.common.apply {
                implementation(test)
                implementation(coroutinesTest)
            }
            implementation(libs.client.ktorClientMock)
        }
        androidMain.dependencies {
            implementation(libs.android.ktor)
        }
        iosMain.dependencies {
            implementation(libs.ios.ktor)
        }
        jvmMain.dependencies {
            implementation(libs.jvm.ktor)
        }
    }
}

configure<BuildKonfigExtension> {
    packageName = Modules.Common.Core.network.packageName

    defaultConfigs {
        buildConfigField(
            STRING,
            Key.BASE_URL_BACKEND.name,
            secret(Key.BASE_URL_BACKEND),
            const = true
        )
        buildConfigField(STRING, Key.BASE_URL_API.name, secret(Key.BASE_URL_API), const = true)
        buildConfigField(
            STRING,
            Key.BASE_URL_API_PREMIUM.name,
            secret(Key.BASE_URL_API_PREMIUM),
            const = true
        )
        buildConfigField(
            STRING,
            Key.API_KEY_PREMIUM.name,
            secret(Key.API_KEY_PREMIUM),
            const = true
        )
    }
}
