import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.INT
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import com.codingfeline.buildkonfig.gradle.BuildKonfigExtension

plugins {
    libs.plugins.apply {
        alias(androidLibrary)
        alias(kotlinMultiplatform)
        id(buildKonfig.get().pluginId)
        alias(mokkery)
    }
}

kotlin {
    @Suppress("Deprecation")
    androidTarget()

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {
            implementation(libs.common.koinCore)

            Modules.Client.ConfigService.apply {
                implementation(project(update))
                implementation(project(review))
            }
            implementation(project(Modules.Client.Storage.app))
            implementation(project(Modules.Client.Core.shared))
            implementation(Submodules.scopemob)
        }
        commonTest.dependencies {
            implementation(libs.common.test)
        }
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
            // needed for gitlive remoteconfig, we have it in app module though
            isCoreLibraryDesugaringEnabled = true
        }

        dependencies {
            coreLibraryDesugaring(libs.android.androidDesugaring)
        }
    }
}

configure<BuildKonfigExtension> {
    packageName = Modules.Client.Repository.appConfig.packageName

    defaultConfigs {
        buildConfigField(
            INT,
            "versionCode",
            ProjectSettings.getVersionCode(project).toString(),
            const = true
        )
        buildConfigField(
            STRING,
            "versionName",
            ProjectSettings.getVersionName(project),
            const = true
        )
    }
}
