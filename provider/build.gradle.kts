plugins {
    with(Dependencies.Plugins) {
        kotlin(MULTIPLATFORM)
        kotlin(COCOAPODS)
    }
}

kotlin {
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        with(ProjectSettings) {
            summary = PROJECT_NAME
            homepage = HOMEPAGE
            ios.deploymentTarget = IOS_DEPLOYMENT_TARGET
            version = getVersionName(project)
        }

        framework {
            baseName = Dependencies.Pods.PROVIDER
            with(Dependencies.Modules) {
                export(project(CLIENT))
                export(project(ANALYTICS))
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

                implementation(Dependencies.Common.KOIN_CORE)

                with(Dependencies.Modules) {
                    implementation(project(LOGMOB))
                    api(project(CLIENT))
                    api(project(ANALYTICS))
                }
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
