plugins {
    libs.plugins.apply {
        alias(kotlinMultiplatform)
        alias(androidKotlinMultiplatformLibrary)
        alias(mokoResources)
    }
}

kotlin {
    androidLibrary {
        namespace = Modules.Client.Core.res.packageName
        compileSdk = ProjectSettings.COMPILE_SDK_VERSION
        minSdk = ProjectSettings.MIN_SDK_VERSION
    }

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

multiplatformResources {
    resourcesPackage.set(Modules.Client.Core.res.packageName)
    resourcesClassName.set(Modules.Client.Core.res.frameworkName)
}
