plugins {
    libs.plugins.apply {
        alias(kotlinMultiplatform)
        alias(androidLibrary)
        alias(sqlDelight)
    }
}

kotlin {
    @Suppress("Deprecation")
    androidTarget()

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

android {
    ProjectSettings.apply {
        namespace = Modules.Common.Core.database.packageName
        compileSdk = COMPILE_SDK_VERSION
        defaultConfig.minSdk = MIN_SDK_VERSION

        compileOptions {
            sourceCompatibility = JAVA_VERSION
            targetCompatibility = JAVA_VERSION
        }
    }
}

sqldelight {
    database("CurrencyConverterCalculatorDatabase") {
        packageName = "${Modules.Common.Core.database.packageName}.sql"
        sourceFolders = listOf("sql")
        linkSqlite = true
    }
}
