/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
@file:Suppress("SpellCheckingInspection")

object Versions {
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
    const val moshiConverterVersion = "2.9.0"
    const val playCoreVersion = "1.8.0"

    // KMP
    const val kotlin = "1.4.20-RC"
    const val androidPlugin = "4.2.0-alpha16"
    const val androidMaterial = "1.3.0-alpha03"
    const val constraintLayout = "2.0.4"
    const val jUnit = "4.13.1"
    const val ktor = "1.4.1"
    const val logBack = "1.3.0-alpha5"
    const val kotlinXHtml = "0.7.2"
    const val versionChecker = "0.35.0"
    const val react = "17.0.0-pre.129-kotlin-1.4.10"
    const val koin = "3.0.0-alpha-4"
    const val kermit = "0.1.8"
    const val multiplatformSettings = "0.6.3"
    const val firebaseCore = "17.4.4"
    const val crashlytics = "17.1.1"
    const val anrWatchDog = "1.4.0"
}

object Dependencies {
    const val kotlin = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"
    const val androidMaterial =
        "com.google.android.material:material:${Versions.androidMaterialVersion}"
    const val constraintLayout =
        "androidx.constraintlayout:constraintlayout:${Versions.constraintLayoutVersion}"
    const val dagger = "com.google.dagger:dagger-android-support:${Versions.daggerVersion}"
    const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofitVersion}"
    const val moshi = "com.squareup.moshi:moshi:${Versions.moshiVersion}"
    const val multiDex = "androidx.multidex:multidex:${Versions.multidexVersion}"
    const val timber = "com.jakewharton.timber:timber:${Versions.timberVersion}"
    const val admob = "com.google.android.gms:play-services-ads:${Versions.admobVersion}"
    const val navigation =
        "androidx.navigation:navigation-fragment-ktx:${Versions.navigationVersion}"
    const val roomRuntime = "androidx.room:room-runtime:${Versions.roomVersion}"
    const val roomKtx = "androidx.room:room-ktx:${Versions.roomVersion}"
    const val moshiConverter =
        "com.squareup.retrofit2:converter-moshi:${Versions.moshiConverterVersion}"
    const val playCore = "com.google.android.play:core:${Versions.playCoreVersion}"

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

        const val crashlytics = "com.google.firebase:firebase-crashlytics:${Versions.crashlytics}"
        const val firebaseCore = "com.google.firebase:firebase-core:${Versions.firebaseCore}"
        const val anrWatchDog = "com.github.anrwatchdog:anrwatchdog:${Versions.anrWatchDog}"

        const val jUnit = "junit:junit:${Versions.jUnit}"
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

object TestDependencies {
    const val jUnit = "junit:junit:${Versions.jUnitVersion}"
    const val mockK = "io.mockk:mockk:${Versions.mockKVersion}"
    const val archTesting = "android.arch.core:core-testing:${Versions.archTestingVersion}"
    const val coroutinesTest =
        "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutinesVersion}"
}

object Annotations {
    const val daggerCompiler = "com.google.dagger:dagger-compiler:${Versions.daggerVersion}"
    const val daggerProcessor =
        "com.google.dagger:dagger-android-processor:${Versions.daggerVersion}"
    const val moshi = "com.squareup.moshi:moshi-kotlin-codegen:${Versions.moshiVersion}"
    const val room = "androidx.room:room-compiler:${Versions.roomVersion}"
}

object Classpaths {
    const val androidBuildTools = "com.android.tools.build:gradle:${Versions.androidPlugin}"
    const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    const val kotlinSerialization = "org.jetbrains.kotlin:kotlin-serialization:${Versions.kotlin}"

    const val gsmGoogle = "com.google.gms:google-services:${Versions.gsmVersion}"
    const val crashlytics =
        "com.google.firebase:firebase-crashlytics-gradle:${Versions.crashlyticsVersion}"
    const val navigation =
        "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.navigationVersion}"
}

object Modules {
    const val client = ":client"
    const val common = ":common"
    const val data = ":data"
    const val scopemob = ":scopemob"
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
