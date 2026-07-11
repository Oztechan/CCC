import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.INT
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import com.codingfeline.buildkonfig.gradle.BuildKonfigExtension

plugins {
    libs.plugins.apply {
        alias(androidKotlinMultiplatformLibrary)
        alias(kotlinMultiplatform)
        id(buildKonfig.get().pluginId)
        alias(mokkery)
    }
}

kotlin {
    android {
        namespace = Modules.Client.Repository.appConfig.packageName
        compileSdk = ProjectSettings.COMPILE_SDK_VERSION
        minSdk = ProjectSettings.MIN_SDK_VERSION
        enableCoreLibraryDesugaring = true
        withHostTest {}
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {
            libs.submob.apply {
                implementation(scopemob)
            }

            implementation(libs.common.koinCore)

            Modules.Client.ConfigService.apply {
                implementation(project(update))
                implementation(project(review))
            }
            implementation(project(Modules.Client.Storage.app))
            implementation(project(Modules.Client.Core.shared))
        }
        commonTest.dependencies {
            implementation(libs.common.test)
        }
    }
}
dependencies {
    coreLibraryDesugaring(libs.android.androidDesugaring)
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
