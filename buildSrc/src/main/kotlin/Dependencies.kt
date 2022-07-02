/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

@Suppress("SpellCheckingInspection")
object Dependencies {
    object Common {
        const val TEST = "test"
        const val TEST_ANNOTATIONS = "test-annotations-common"
        const val KOIN_CORE = "io.insert-koin:koin-core:${Versions.KOIN}"
        const val MULTIPLATFORM_SETTINGS = "com.russhwolf:multiplatform-settings:${Versions.MULTIPLATFORM_SETTINGS}"
        const val KOTLIN_X_DATE_TIME = "org.jetbrains.kotlinx:kotlinx-datetime:${Versions.KOTLIN_X_DATE_TIME}"
        const val KTOR_LOGGING = "io.ktor:ktor-client-logging:${Versions.KTOR}"
        const val KTOR_JSON = "io.ktor:ktor-serialization-kotlinx-json:${Versions.KTOR}"
        const val KTOR_CONTENT_NEGOTIATION = "io.ktor:ktor-client-content-negotiation:${Versions.KTOR}"
        const val COROUTINES = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.COROUTINES}"
        const val COROUTINES_TEST = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.COROUTINES}"
        const val SQL_DELIGHT_RUNTIME = "com.squareup.sqldelight:runtime:${Versions.SQL_DELIGHT}"
        const val SQL_DELIGHT_COROUTINES_EXT = "com.squareup.sqldelight:coroutines-extensions:${Versions.SQL_DELIGHT}"
        const val MOKO_RESOURCES = "dev.icerock.moko:resources:${Versions.MOKO_RESOURCES}"
        const val MOCKATIVE = "io.mockative:mockative:${Versions.MOCKATIVE}"
        const val SCOPE_MOB = "com.github.submob:scopemob:${Versions.SCOPE_MOB}"
        const val PARSER_MOB = "com.github.submob:parsermob:${Versions.PARSER_MOB}"
        const val LOG_MOB = "com.github.submob:logmob:${Versions.LOG_MOB}"
    }

    object Android {
        const val ANDROID_MATERIAL = "com.google.android.material:material:${Versions.ANDROID_MATERIAL}"
        const val CONSTRAINT_LAYOUT = "androidx.constraintlayout:constraintlayout:${Versions.CONSTRAINT_LAYOUT}"
        const val KOIN_ANDROID = "io.insert-koin:koin-android:${Versions.KOIN}"
        const val FIREBASE_ANALYTICS = "com.google.firebase:firebase-analytics-ktx:${Versions.FIREBASE_ANALYTICS}"
        const val FIREBASE_REMOTE_CONFIG = "com.google.firebase:firebase-config-ktx:${Versions.FIREBASE_REMOTE_CONFIG}"
        const val DESUGARING = "com.android.tools:desugar_jdk_libs:${Versions.DESUGARING}"
        const val LIFECYCLE_VIEWMODEL = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.LIFECYCLE}"
        const val LIFECYCLE_RUNTIME = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.LIFECYCLE}"
        const val NAVIGATION = "androidx.navigation:navigation-fragment-ktx:${Versions.NAVIGATION}"
        const val KTOR = "io.ktor:ktor-client-android:${Versions.KTOR}"
        const val SQL_DELIGHT = "com.squareup.sqldelight:android-driver:${Versions.SQL_DELIGHT}"
        const val LEAK_CANARY = "com.squareup.leakcanary:leakcanary-android:${Versions.LEAK_CANARY}"
        const val WORK_RUNTIME = "androidx.work:work-runtime:${Versions.WORK_RUNTIME}"
        const val SPLASH_SCREEN = "androidx.core:core-splashscreen:${Versions.SPLASH_SCREEN}"
        const val ROOT_BEER = "com.scottyab:rootbeer-lib:${Versions.ROOT_BEER}"
        const val BASE_MOB = "com.github.submob:basemob:${Versions.BASE_MOB}"

        object GOOGLE {
            const val BILLING = "com.android.billingclient:billing:${Versions.BILLING}"
            const val ADMOB = "com.google.android.gms:play-services-ads:${Versions.ADMOB}"
            const val PLAY_CORE = "com.google.android.play:core:${Versions.PLAY_CORE}"
        }

        @Suppress("unused")
        object HUAWEI
    }

    object IOS {
        const val KTOR = "io.ktor:ktor-client-ios:${Versions.KTOR}"
        const val SQL_DELIGHT = "com.squareup.sqldelight:native-driver:${Versions.SQL_DELIGHT}"
    }

    object JVM {
        const val KTOR_CORE = "io.ktor:ktor-server-core:${Versions.KTOR}"
        const val KTOR_NETTY = "io.ktor:ktor-server-netty:${Versions.KTOR}"
        const val LOG_BACK = "ch.qos.logback:logback-classic:${Versions.LOG_BACK}"
        const val SQLLITE_DRIVER = "com.squareup.sqldelight:sqlite-driver:${Versions.SQL_DELIGHT}"
        const val KTOR = "io.ktor:ktor-client-apache:${Versions.KTOR}"
    }

    object ClassPaths {
        const val ANDROID_GRADLE_PLUGIN = "com.android.tools.build:gradle:${Versions.ANDROID_GRADLE_PLUGIN}"
        const val KOTLIN_GRADLE_PLUGIN = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.KOTLIN}"
        const val KOTLIN_SERIALIZATION = "org.jetbrains.kotlin:kotlin-serialization:${Versions.KOTLIN}"
        const val SQL_DELIGHT = "com.squareup.sqldelight:gradle-plugin:${Versions.SQL_DELIGHT}"
        const val GSM = "com.google.gms:google-services:${Versions.GSM}"
        const val CRASHLYTICS = "com.google.firebase:firebase-crashlytics-gradle:${Versions.CRASHLYTICS}"
        const val NAVIGATION = "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.NAVIGATION}"
        const val MOKO_RESOURCES = "dev.icerock.moko:resources-generator:${Versions.MOKO_RESOURCES}"
        const val BUILD_KONFIG = "com.codingfeline.buildkonfig:buildkonfig-gradle-plugin:${Versions.BUILD_KONFIG}"
        const val KOVER = "org.jetbrains.kotlinx:kover:${Versions.KOVER}"
    }

    object Plugins {
        const val MULTIPLATFORM = "multiplatform"
        const val ANDROID = "android"
        const val ANDROID_APP = "com.android.application"
        const val ANDROID_LIB = "com.android.library"
        const val COCOAPODS = "native.cocoapods"
        const val BUILD_KONFIG = "com.codingfeline.buildkonfig"
        const val CRASHLYTICS = "com.google.firebase.crashlytics"
        const val GOOGLE_SERVICES = "com.google.gms.google-services"
        const val SAFE_ARGS = "androidx.navigation.safeargs"
        const val KOTLIN_X_SERIALIZATION = "kotlinx-serialization"
        const val SQL_DELIGHT = "com.squareup.sqldelight"
        const val MOKO_RESOURCES = "dev.icerock.mobile.multiplatform-resources"
        const val DEPENDENCY_UPDATES = "com.github.ben-manes.versions"
        const val BUILD_HEALTH = "com.autonomousapps.dependency-analysis"
        const val KOVER = "org.jetbrains.kotlinx.kover"
        const val KSP = "com.google.devtools.ksp"
    }

    object Processors {
        const val MOCKATIVE = "io.mockative:mockative-processor:${Versions.MOCKATIVE}"
    }

    object Modules {
        const val CLIENT = ":client"
        const val RESOURCES = ":resources"
        const val COMMON = ":common"
        const val BILLING = ":billing"
        const val AD = ":ad"
        const val ANALYTICS = ":analytics"
        const val CONFIG = ":config"
    }
}
