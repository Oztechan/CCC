plugins {
    @Suppress("DSL_SCOPE_VIOLATION")
    libs.plugins.apply {
        id(multiplatform.get().pluginId)
        id(cocoapods.get().pluginId)
        id(androidLib.get().pluginId)
        id(mokoResources.get().pluginId)
    }
}

kotlin {
    android()

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        ProjectSettings.apply {
            summary = PROJECT_NAME
            homepage = HOMEPAGE
            ios.deploymentTarget = IOS_DEPLOYMENT_TARGET
            version = getVersionName(project)
        }
        framework {
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

        val androidMain by getting
        val androidUnitTest by getting

        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
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

    // todo can be removed after
    // https://github.com/icerockdev/moko-resources/issues/384
    // https://github.com/icerockdev/moko-resources/issues/353
    sourceSets["main"].res.srcDir(File(buildDir, "generated/moko/androidMain/res"))
}

multiplatformResources {
    multiplatformResourcesPackage = Modules.Client.Core.res.packageName
    disableStaticFrameworkWarning = true
    multiplatformResourcesClassName = Modules.Client.Core.res.frameworkName
}
