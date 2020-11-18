/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
@file:Suppress("SpellCheckingInspection")

object Versions {
    const val kotlin = "1.4.20-RC"
    const val androidPlugin = "4.2.0-alpha16"
    const val androidMaterial = "1.3.0-alpha03"
    const val constraintLayout = "2.0.4"
    const val jUnit = "4.13.1"
    const val ktor = "1.4.2"
    const val logBack = "1.3.0-alpha5"
    const val kotlinXHtml = "0.7.2"
    const val versionChecker = "0.36.0"
    const val react = "17.0.0-pre.129-kotlin-1.4.10"
    const val koin = "3.0.0-alpha-4"
    const val kermit = "0.1.8"
    const val multiplatformSettings = "0.6.3"
    const val coroutines = "1.4.1"
    const val mockK = "1.10.2"
    const val archTesting = "1.1.1"
    const val firebaseCore = "18.0.0"
    const val firebaseCrashlytics = "17.3.0"
    const val anrWatchDog = "1.4.0"
    const val gsm = "4.3.4"
    const val crashlytics = "2.4.1"
    const val dagger = "2.29.1"
    const val retrofit = "2.9.0"
    const val moshi = "1.11.0"
    const val room = "2.3.0-alpha03"
    const val admob = "19.5.0"
    const val multidex = "2.0.1"
    const val navigation = "2.3.1"
    const val moshiConverter = "2.9.0"
    const val playCore = "1.8.3"
}

object Dependencies {

    // KMP
    object Common {
        const val test = "test-common"
        const val testAnnotations = "test-annotations-common"

        const val koinCore = "org.koin:koin-core:${Versions.koin}"
        const val kermit = "co.touchlab:kermit:${Versions.kermit}"
        const val multiplatformSettings =
            "com.russhwolf:multiplatform-settings:${Versions.multiplatformSettings}"
    }

    object Android {
        const val androidMaterial =
            "com.google.android.material:material:${Versions.androidMaterial}"
        const val constraintLayout =
            "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"

        const val koinAndroidViewModel = "org.koin:koin-androidx-viewmodel:${Versions.koin}"

        const val firebaseCrashlytics =
            "com.google.firebase:firebase-crashlytics:${Versions.firebaseCrashlytics}"
        const val firebaseCore = "com.google.firebase:firebase-core:${Versions.firebaseCore}"
        const val anrWatchDog = "com.github.anrwatchdog:anrwatchdog:${Versions.anrWatchDog}"

        const val dagger = "com.google.dagger:dagger-android-support:${Versions.dagger}"
        const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
        const val moshi = "com.squareup.moshi:moshi:${Versions.moshi}"
        const val multiDex = "androidx.multidex:multidex:${Versions.multidex}"
        const val admob = "com.google.android.gms:play-services-ads:${Versions.admob}"
        const val navigation = "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}"
        const val roomRuntime = "androidx.room:room-runtime:${Versions.room}"
        const val roomKtx = "androidx.room:room-ktx:${Versions.room}"
        const val moshiConverter =
            "com.squareup.retrofit2:converter-moshi:${Versions.moshiConverter}"
        const val playCore = "com.google.android.play:core:${Versions.playCore}"

        // Test
        const val jUnit = "junit:junit:${Versions.jUnit}"
        const val mockK = "io.mockk:mockk:${Versions.mockK}"
        const val archTesting = "android.arch.core:core-testing:${Versions.archTesting}"
        const val coroutinesTest =
            "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutines}"
    }

    @Suppress("unused")
    object IOS

    object JVM {
        const val testJUnit = "test-junit"

        const val ktorCore = "io.ktor:ktor-server-core:${Versions.ktor}"
        const val ktorNetty = "io.ktor:ktor-server-netty:${Versions.ktor}"
        const val ktorWebSockets = "io.ktor:ktor-websockets:${Versions.ktor}"
        const val ktorSerialization = "io.ktor:ktor-serialization:${Versions.ktor}"
        const val logBack = "ch.qos.logback:logback-classic:${Versions.logBack}"
    }

    object JS {
        const val test = "test-js"

        const val kotlinXHtml = "org.jetbrains.kotlinx:kotlinx-html-js:${Versions.kotlinXHtml}"
        const val kotlinReact = "org.jetbrains:kotlin-react:${Versions.react}"
        const val kotlinReactDom = "org.jetbrains:kotlin-react-dom:${Versions.react}"
    }
}

object Annotations {
    const val daggerCompiler = "com.google.dagger:dagger-compiler:${Versions.dagger}"
    const val daggerProcessor = "com.google.dagger:dagger-android-processor:${Versions.dagger}"
    const val moshi = "com.squareup.moshi:moshi-kotlin-codegen:${Versions.moshi}"
    const val room = "androidx.room:room-compiler:${Versions.room}"
}

object Classpaths {
    const val androidBuildTools = "com.android.tools.build:gradle:${Versions.androidPlugin}"
    const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    const val kotlinSerialization = "org.jetbrains.kotlin:kotlin-serialization:${Versions.kotlin}"

    const val gsmGoogle = "com.google.gms:google-services:${Versions.gsm}"
    const val crashlytics =
        "com.google.firebase:firebase-crashlytics-gradle:${Versions.crashlytics}"
    const val navigation =
        "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.navigation}"
}

object Modules {
    const val client = ":client"
    const val common = ":common"
    const val data = ":data"
    const val scopemob = ":scopemob"
    const val logmob = ":logmob"
}

object Libs {
    const val mxParser = "libs/MathParser.org-mXparser-v.4.2.0-jdk.1.7.jar"
}

object Plugins {
    // Id
    const val library = "com.android.library"
    const val crashlytics = "com.google.firebase.crashlytics"
    const val googleServices = "com.google.gms.google-services"
    const val safeargs = "androidx.navigation.safeargs"

    // Kapt
    const val android = "android"
    const val kapt = "kapt"

    // KMP
    const val androidApplication = "com.android.application"

    //    const val android = "android"
    const val androidLibrary = "com.android.library"
    const val multiplatform = "multiplatform"
    const val platformJvm = "jvm"
    const val serializationPlugin = "plugin.serialization"
    const val js = "js"
    const val versionChecker = "com.github.ben-manes.versions"
}
