plugins {
    @Suppress("DSL_SCOPE_VIOLATION")
    libs.plugins.apply {
        id(multiplatform.get().pluginId)
        id(cocoapods.get().pluginId)
    }
}

kotlin {
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
            Modules.apply {
                baseName = provider.frameworkName
                export(project(client))
                export(project(analytics))
            }
        }
    }

    @Suppress("UNUSED_VARIABLE")
    sourceSets {
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
            dependencies {

                implementation(libs.common.koinCore)

                Modules.apply {
                    api(project(client))
                    api(project(analytics))

                    implementation(project(common))
                }

                Modules.Common.Core.apply {
                    implementation(project(database))
                }

                implementation(project(Modules.Submodules.logmob))
            }
        }
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by creating {
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }
    }
}
