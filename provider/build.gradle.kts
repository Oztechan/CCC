plugins {
    with(Dependencies.Plugins) {
        kotlin(MULTIPLATFORM)
        kotlin(COCOAPODS)
    }
}
version = ProjectSettings.getVersionName(project)

kotlin {
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        summary = "CCC"
        homepage = "https://github.com/CurrencyConverterCalculator/CCC"
        ios.deploymentTarget = "14.0"
        framework {
            baseName = "Provider"
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
