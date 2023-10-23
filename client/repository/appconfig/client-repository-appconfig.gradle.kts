import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.INT
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import com.codingfeline.buildkonfig.gradle.BuildKonfigExtension

plugins {
    libs.plugins.apply {
        id(androidLib.get().pluginId)
        alias(kotlinMultiplatform)
        id(buildKonfig.get().pluginId)
        alias(ksp)
    }
}

kotlin {
    @Suppress("OPT_IN_USAGE")
    targetHierarchy.default()

    androidTarget()

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    @Suppress("UNUSED_VARIABLE")
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.common.koinCore)

                Modules.Client.ConfigService.apply {
                    implementation(project(update))
                    implementation(project(review))
                }
                implementation(project(Modules.Client.Storage.app))
                implementation(project(Modules.Client.Core.shared))
                implementation(Submodules.scopemob)
            }
        }
        val commonTest by getting {
            dependencies {
                libs.common.apply {
                    implementation(test)
                    implementation(mockative)
                }
            }
        }
    }
}

dependencies {
    configurations
        .filter { it.name.startsWith("ksp") && it.name.contains("Test") }
        .forEach {
            add(it.name, libs.processors.mockative)
        }
}

android {
    ProjectSettings.apply {
        namespace = Modules.Client.Repository.appConfig.packageName
        compileSdk = COMPILE_SDK_VERSION
        defaultConfig.minSdk = MIN_SDK_VERSION

        compileOptions {
            sourceCompatibility = JAVA_VERSION
            targetCompatibility = JAVA_VERSION
        }
    }
}

configure<BuildKonfigExtension> {
    packageName = Modules.Client.Repository.appConfig.packageName

    defaultConfigs {
        buildConfigField(INT, "versionCode", ProjectSettings.getVersionCode(project).toString(), const = true)
        buildConfigField(STRING, "versionName", ProjectSettings.getVersionName(project), const = true)
    }
}
