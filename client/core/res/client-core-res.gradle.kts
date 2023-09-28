import io.gitlab.arturbosch.detekt.Detekt

plugins {
    libs.plugins.apply {
        id(multiplatform.get().pluginId)
        id(androidLib.get().pluginId)
        id(mokoResources.get().pluginId)
    }
}

kotlin {
    @Suppress("OPT_IN_USAGE")
    targetHierarchy.default()

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

    @Suppress("UNUSED_VARIABLE")
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.common.mokoResources)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(libs.common.test)
            }
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

    // Todo https://github.com/icerockdev/moko-resources/issues/510
    sourceSets {
        getByName("main").java.srcDirs("build/generated/moko/androidMain/src")
    }
}

multiplatformResources {
    multiplatformResourcesPackage = Modules.Client.Core.res.packageName
    disableStaticFrameworkWarning = true
    multiplatformResourcesClassName = Modules.Client.Core.res.frameworkName
}

// todo https://github.com/icerockdev/moko-resources/issues/421
tasks.withType<Detekt> {
    dependsOn("generateMRcommonMain")
}
