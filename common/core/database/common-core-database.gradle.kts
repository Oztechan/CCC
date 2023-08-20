plugins {
    libs.plugins.apply {
        id(multiplatform.get().pluginId)
        id(androidLib.get().pluginId)
        id(sqlDelight.get().pluginId)
    }
}

kotlin {
    @Suppress("OPT_IN_USAGE")
    targetHierarchy.default()

    androidTarget()

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    jvm()

    @Suppress("UNUSED_VARIABLE")
    sourceSets {
        val commonMain by getting {
            dependencies {
                libs.common.apply {
                    implementation(koinCore)
                    implementation(coroutines)
                    implementation(sqlDelightCoroutinesExt)
                }
                implementation(project(Modules.Common.Core.model))
            }
        }
        val commonTest by getting {
            dependencies {
                libs.common.apply {
                    implementation(test)
                    implementation(coroutinesTest)
                }
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(libs.android.sqlliteDriver)
            }
        }
        val iosMain by getting {
            dependencies {
                implementation(libs.ios.sqlliteDriver)
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(libs.jvm.sqlliteDriver)
            }
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
