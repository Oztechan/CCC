plugins {
    libs.plugins.apply {
        alias(kotlinMultiplatform)
        alias(androidLibrary)
        alias(mokoResources)
    }
}

kotlin {
    androidTarget()

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = Modules.Client.Core.res.frameworkName
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.common.mokoResources)
        }
        commonTest.dependencies {
            implementation(libs.common.test)
        }
    }
}

android {
    ProjectSettings.apply {
        namespace = Modules.Client.Core.res.packageName
        compileSdk = COMPILE_SDK_VERSION
        defaultConfig.minSdk = MIN_SDK_VERSION

        compileOptions {
            sourceCompatibility = JAVA_VERSION
            targetCompatibility = JAVA_VERSION
        }
    }
}

multiplatformResources {
    resourcesPackage.set(Modules.Client.Core.res.packageName)
    resourcesClassName.set(Modules.Client.Core.res.frameworkName)
}
