/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

@Suppress("SpellCheckingInspection")
object ClassPaths {
    const val ANDROID_GRADLE_PLUGIN =
        "com.android.tools.build:gradle:${Versions.ANDROID_GRADLE_PLUGIN}"
    const val KOTLIN_GRADLE_PLUGIN = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.KOTLIN}"
    const val KOTLIN_SERIALIZATION = "org.jetbrains.kotlin:kotlin-serialization:${Versions.KOTLIN}"
    const val COMPOSE = "org.jetbrains.compose:compose-gradle-plugin:${Versions.COMPOSE}"
    const val SQL_DELIGHT = "com.squareup.sqldelight:gradle-plugin:${Versions.SQL_DELIGHT}"
    const val GSM = "com.google.gms:google-services:${Versions.GSM}"
    const val CRASHLYTICS =
        "com.google.firebase:firebase-crashlytics-gradle:${Versions.CRASHLYTICS}"
    const val NAVIGATION =
        "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.NAVIGATION}"
    const val MOKO_RESOURCES = "dev.icerock.moko:resources-generator:${Versions.MOKO_RESOURCES}"
    const val BUILD_KONFIG =
        "com.codingfeline.buildkonfig:buildkonfig-gradle-plugin:${Versions.BUILD_KONFIG}"
}
