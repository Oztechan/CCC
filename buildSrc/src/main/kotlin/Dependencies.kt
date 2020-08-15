/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
@file:Suppress("SpellCheckingInspection")
/*
Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
*/

object Versions {
    const val KOTLIN_VERSION = "1.3.72"
    const val ANDROID_PLUGIN_VERSION = "4.0.1"
    const val GSM_GOOGLE_VERSION = "4.3.3"
    const val CRASHLYTICS_VERSION = "2.1.0"
    const val DAGGER_VERSION = "2.28.3"
    const val ANDROID_MATERIAL_VERSION = "1.2.0"
    const val CONSTRAINT_LAYOUT_VERSION = "1.1.3"
    const val JUNIT_VERSION = "4.13"
    const val MOCKK_VERSION = "1.10.0"
    const val ARCH_TESTING_VERSION = "1.1.1"
    const val RETROFIT_VERSION = "2.9.0"
    const val MOSHI_VERSION = "1.9.3"
    const val ROOM_VERSION = "2.2.5"
    const val COROUTINES_VERSION = "1.3.7"
    const val ADMOB_VERSION = "19.3.0"
    const val MULTIDEX_VERSION = "2.0.1"
    const val TIMBER_VERSION = "4.7.1"
    const val NAVIGATION_VERSION = "2.3.0"
}

object Dependency {
    const val kotlin = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.KOTLIN_VERSION}"
    const val androidMaterial = "com.google.android.material:material:${Versions.ANDROID_MATERIAL_VERSION}"
    const val constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.CONSTRAINT_LAYOUT_VERSION}"
    const val dagger = "com.google.dagger:dagger-android-support:${Versions.DAGGER_VERSION}"
    const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.RETROFIT_VERSION}"
    const val moshi = "com.squareup.moshi:moshi:${Versions.MOSHI_VERSION}"
    const val multiDex = "androidx.multidex:multidex:${Versions.MULTIDEX_VERSION}"
    const val timber = "com.jakewharton.timber:timber:${Versions.TIMBER_VERSION}"
    const val admob = "com.google.android.gms:play-services-ads:${Versions.ADMOB_VERSION}"
    const val navigation = "androidx.navigation:navigation-fragment-ktx:${Versions.NAVIGATION_VERSION}"
    const val roomRuntime = "androidx.room:room-runtime:${Versions.ROOM_VERSION}"
    const val roomKtx = "androidx.room:room-ktx:${Versions.ROOM_VERSION}"
}

object Test {
    const val jUnit = "junit:junit:${Versions.JUNIT_VERSION}"
    const val mockK = "io.mockk:mockk:${Versions.MOCKK_VERSION}"
    const val archTesting = "android.arch.core:core-testing:${Versions.ARCH_TESTING_VERSION}"
    const val coroutinesTest = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.COROUTINES_VERSION}"
}

object Annotation {
    const val daggerCompiler = "com.google.dagger:dagger-compiler:${Versions.DAGGER_VERSION}"
    const val daggerProcessor = "com.google.dagger:dagger-android-processor:${Versions.DAGGER_VERSION}"
    const val moshi = "com.squareup.moshi:moshi-kotlin-codegen:${Versions.MOSHI_VERSION}"
    const val room = "androidx.room:room-compiler:${Versions.ROOM_VERSION}"
}

object Classpath {
    const val androidBuildTools = "com.android.tools.build:gradle:${Versions.ANDROID_PLUGIN_VERSION}"
    const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.KOTLIN_VERSION}"
    const val gsmGoogle = "com.google.gms:google-services:${Versions.GSM_GOOGLE_VERSION}"
    const val crashlytics = "com.google.firebase:firebase-crashlytics-gradle:${Versions.CRASHLYTICS_VERSION}"
    const val navigation = "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.NAVIGATION_VERSION}"
}

object Lib {
    const val mxParser = "libs/MathParser.org-mXparser-v.4.2.0-jdk.1.7.jar"
}

object Modules {
    const val ui = ":ui"
    const val data = ":data"
    const val baseMob = ":basemob"
    const val logMob = ":logmob"
    const val scopeMob = ":logmob"
}
