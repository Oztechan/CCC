/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

@Suppress("SpellCheckingInspection")
object ClassPaths {
    const val androidBuildTools = "com.android.tools.build:gradle:${Versions.androidPlugin}"
    const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    const val kotlinSerialization = "org.jetbrains.kotlin:kotlin-serialization:${Versions.kotlin}"
    const val sqldelight = "com.squareup.sqldelight:gradle-plugin:${Versions.sqlDelight}"

    const val gsmGoogle = "com.google.gms:google-services:${Versions.gsm}"
    const val crashlytics =
        "com.google.firebase:firebase-crashlytics-gradle:${Versions.crashlytics}"
    const val navigation =
        "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.navigation}"
}
