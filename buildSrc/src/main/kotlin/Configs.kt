/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
@file:Suppress("SpellCheckingInspection")

object Versions {
    const val kotlinVersion = "1.4.0"
    const val androidPluginVersion = "4.0.1"
    const val gsmVersion = "4.3.3"
    const val crashlyticsVersion = "2.1.0"
    const val daggerVersion = "2.28.3"
    const val androidMaterialVersion = "1.2.0"
    const val constraintLayoutVersion = "2.0.1"
    const val jUnitVersion = "4.13"
    const val mockKVersion = "1.10.0"
    const val archTestingVersion = "1.1.1"
    const val retrofitVersion = "2.9.0"
    const val moshiVersion = "1.9.3"
    const val roomVersion = "2.2.5"
    const val coroutinesVersion = "1.3.7"
    const val admobVersion = "19.3.0"
    const val multidexVersion = "2.0.1"
    const val timberVersion = "4.7.1"
    const val navigationVersion = "2.3.0"
    const val firebaseCoreVersion = "17.4.4"
    const val firebaseCrashlyticsVersion = "17.1.1"
    const val anrWatchDogVersion = "1.4.0"
    const val moshiConverterVersion = "2.9.0"
    const val okhttpInterceptorVersion = "4.8.1"
    const val versionsVersion = "0.29.0"
    const val playCoreVersion = "1.8.0"
}

object Dependencies {
    const val kotlin = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlinVersion}"
    const val androidMaterial = "com.google.android.material:material:${Versions.androidMaterialVersion}"
    const val constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintLayoutVersion}"
    const val dagger = "com.google.dagger:dagger-android-support:${Versions.daggerVersion}"
    const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofitVersion}"
    const val moshi = "com.squareup.moshi:moshi:${Versions.moshiVersion}"
    const val multiDex = "androidx.multidex:multidex:${Versions.multidexVersion}"
    const val timber = "com.jakewharton.timber:timber:${Versions.timberVersion}"
    const val admob = "com.google.android.gms:play-services-ads:${Versions.admobVersion}"
    const val navigation = "androidx.navigation:navigation-fragment-ktx:${Versions.navigationVersion}"
    const val roomRuntime = "androidx.room:room-runtime:${Versions.roomVersion}"
    const val roomKtx = "androidx.room:room-ktx:${Versions.roomVersion}"
    const val firebaseCrashlytics = "com.google.firebase:firebase-crashlytics:${Versions.firebaseCrashlyticsVersion}"
    const val firebaseCore = "com.google.firebase:firebase-core:${Versions.firebaseCoreVersion}"
    const val anrWatchDog = "com.github.anrwatchdog:anrwatchdog:${Versions.anrWatchDogVersion}"
    const val moshiConverter = "com.squareup.retrofit2:converter-moshi:${Versions.moshiConverterVersion}"
    const val okhttpInterceptor = "com.squareup.okhttp3:logging-interceptor:${Versions.okhttpInterceptorVersion}"
    const val playCore = "com.google.android.play:core:${Versions.playCoreVersion}"
}

object TestDependencies {
    const val jUnit = "junit:junit:${Versions.jUnitVersion}"
    const val mockK = "io.mockk:mockk:${Versions.mockKVersion}"
    const val archTesting = "android.arch.core:core-testing:${Versions.archTestingVersion}"
    const val coroutinesTest = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutinesVersion}"
}

object Annotations {
    const val daggerCompiler = "com.google.dagger:dagger-compiler:${Versions.daggerVersion}"
    const val daggerProcessor = "com.google.dagger:dagger-android-processor:${Versions.daggerVersion}"
    const val moshi = "com.squareup.moshi:moshi-kotlin-codegen:${Versions.moshiVersion}"
    const val room = "androidx.room:room-compiler:${Versions.roomVersion}"
}

object Classpaths {
    const val androidBuildTools = "com.android.tools.build:gradle:${Versions.androidPluginVersion}"
    const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlinVersion}"
    const val gsmGoogle = "com.google.gms:google-services:${Versions.gsmVersion}"
    const val crashlytics = "com.google.firebase:firebase-crashlytics-gradle:${Versions.crashlyticsVersion}"
    const val navigation = "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.navigationVersion}"
}

object Modules {
    const val ui = ":ui"
    const val data = ":data"
    const val basemob = ":basemob"
    const val logmob = ":logmob"
    const val scopemob = ":scopemob"
}

object Libs {
    const val mxParser = "libs/MathParser.org-mXparser-v.4.2.0-jdk.1.7.jar"
}

object Plugins {
    // Id
    const val application = "com.android.application"
    const val library = "com.android.library"
    const val crashlytics = "com.google.firebase.crashlytics"
    const val googleServices = "com.google.gms.google-services"
    const val versions = "com.github.ben-manes.versions"
    const val safeargs = "androidx.navigation.safeargs"

    // Kapt
    const val android = "android"
    const val kapt = "kapt"
}
