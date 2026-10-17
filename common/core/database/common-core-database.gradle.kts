plugins {
    libs.plugins.apply {
        alias(kotlinMultiplatform)
        alias(androidKotlinMultiplatformLibrary)
        alias(sqlDelight)
    }
}

kotlin {
    android {
        namespace = Modules.Common.Core.database.packageName
        compileSdk = ProjectSettings.COMPILE_SDK_VERSION
        minSdk = ProjectSettings.MIN_SDK_VERSION
        withHostTest {}
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    jvm()

    sourceSets {
        commonMain.dependencies {
            libs.common.apply {
                implementation(koinCore)
                implementation(coroutines)
                implementation(sqlDelightCoroutinesExt)
                implementation(kermit)
            }
            implementation(project(Modules.Common.Core.model))
        }
        commonTest.dependencies {
            libs.common.apply {
                implementation(test)
                implementation(coroutinesTest)
            }
        }
        androidMain.dependencies {
            implementation(libs.android.sqlliteDriver)
        }
        iosMain.dependencies {
            implementation(libs.ios.sqlliteDriver)
        }
        jvmMain.dependencies {
            implementation(libs.jvm.sqlliteDriver)
        }
    }
}

sqldelight {
    linkSqlite.set(true)
    databases {
        create("CurrencyConverterCalculatorDatabase") {
            packageName.set("${Modules.Common.Core.database.packageName}.sql")
            srcDirs.setFrom("src/commonMain/sql")
        }
    }
}
