plugins {
    libs.plugins.apply {
        alias(kotlinMultiplatform)
        alias(androidKotlinMultiplatformLibrary)
        alias(mokkery)
    }
}

kotlin {
    android {
        namespace = Modules.Common.DataSource.conversion.packageName
        compileSdk = ProjectSettings.COMPILE_SDK_VERSION
        minSdk = ProjectSettings.MIN_SDK_VERSION
        withHostTest {}
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    jvm()

    // The iOS test binaries pull in SQLDelight's native driver, which needs the system
    // sqlite to be linked explicitly; sqldelight's own linkSqlite only covers :common:core:database.
    targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget>().configureEach {
        binaries.withType<org.jetbrains.kotlin.gradle.plugin.mpp.TestExecutable>().configureEach {
            linkerOpts("-lsqlite3")
        }
    }

    sourceSets {
        commonMain.dependencies {
            libs.common.apply {
                implementation(koinCore)
                implementation(coroutines)
                implementation(kermit)
            }
            Modules.Common.Core.apply {
                implementation(project(database))
                implementation(project(model))
                implementation(project(infrastructure))
            }
        }
        commonTest.dependencies {
            libs.common.apply {
                implementation(test)
                implementation(coroutinesTest)
            }
        }
        getByName("androidHostTest").dependencies {
            implementation(libs.jvm.sqlliteDriver)
        }
        iosTest.dependencies {
            implementation(libs.ios.sqlliteDriver)
        }
        jvmTest.dependencies {
            implementation(libs.jvm.sqlliteDriver)
        }
    }
}
