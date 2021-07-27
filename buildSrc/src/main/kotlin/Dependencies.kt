/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

@Suppress("SpellCheckingInspection")
object Dependencies {
    object Common {
        const val TEST = "test-common"
        const val TEST_ANNOTATIONS = "test-annotations-common"
        const val KOIN_CORE = "io.insert-koin:koin-core:${Versions.KOIN}"
        const val KERMIT = "co.touchlab:kermit:${Versions.KERMIT}"
        const val MULTIPLATFORM_SETTINGS =
            "com.russhwolf:multiplatform-settings:${Versions.MULTIPLATFORM_SETTINGS}"
        const val KOTLIN_X_DATE_TIME =
            "org.jetbrains.kotlinx:kotlinx-datetime:${Versions.KOTLIN_X_DATE_TIME}"
        const val KTOR_LOGGING = "io.ktor:ktor-client-logging:${Versions.KTOR}"
        const val KTOR_SETIALIZATION = "io.ktor:ktor-client-serialization:${Versions.KTOR}"
        const val COROUTINES =
            "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.COROUTINES}"
        const val SQL_DELIGHT_RUNTIME = "com.squareup.sqldelight:runtime:${Versions.SQL_DELIGHT}"
        const val SQL_DELIGHT_COROUTINES_EXT =
            "com.squareup.sqldelight:coroutines-extensions:${Versions.SQL_DELIGHT}"
        const val MOKO_RESOURCES = "dev.icerock.moko:resources:${Versions.MOKO_RESOURCES}"
    }

    object Android {
        const val ANDROID_MATERIAL =
            "com.google.android.material:material:${Versions.ANDROID_MATERIAL}"
        const val CONSTRAINT_LAYOUT =
            "androidx.constraintlayout:constraintlayout:${Versions.CONSTRAINT_LAYOUT}"
        const val KOIN_ANDROID = "io.insert-koin:koin-android:${Versions.KOIN}"
        const val FIREBASE_CRASHLYTICS =
            "com.google.firebase:firebase-crashlytics:${Versions.FIREBASE_CRASHLYTICS}"
        const val FIREBASE_CORE = "com.google.firebase:firebase-core:${Versions.FIREBASE_CORE}"
        const val DESUGARING = "com.android.tools:desugar_jdk_libs:${Versions.DESUGARING}"
        const val ANR_WATCH_DOG = "com.github.anrwatchdog:anrwatchdog:${Versions.ANR_WATCH_DOG}"
        const val ADMOB = "com.google.android.gms:play-services-ads:${Versions.ADMOB}"
        const val LIFECYCLE_VIEWMODEL =
            "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.LIFECYCLE}"
        const val LIFECYCLE_RUNTIME =
            "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.LIFECYCLE}"
        const val NAVIGATION = "androidx.navigation:navigation-fragment-ktx:${Versions.NAVIGATION}"
        const val PLAY_CORE = "com.google.android.play:core:${Versions.PLAY_CORE}"
        const val KTOR = "io.ktor:ktor-client-android:${Versions.KTOR}"
        const val SQL_DELIGHT = "com.squareup.sqldelight:android-driver:${Versions.SQL_DELIGHT}"
        const val BILLING = "com.android.billingclient:billing:${Versions.BILLING}"
        const val LEAK_CANARY = "com.squareup.leakcanary:leakcanary-android:${Versions.LEAK_CANARY}"
    }

    object IOS {
        const val KTOR = "io.ktor:ktor-client-ios:${Versions.KTOR}"
        const val SQL_DELIGHT = "com.squareup.sqldelight:native-driver:${Versions.SQL_DELIGHT}"
    }

    object JVM {
        const val TEST_J_UNIT = "test-junit"
        const val KTOR_CORE = "io.ktor:ktor-server-core:${Versions.KTOR}"
        const val KTOR_NETTY = "io.ktor:ktor-server-netty:${Versions.KTOR}"
        const val KTOR_SERIALIZATIONM = "io.ktor:ktor-serialization:${Versions.KTOR}"
        const val LOG_BACK = "ch.qos.logback:logback-classic:${Versions.LOG_BACK}"
        const val SQLLITE_DRIVER = "com.squareup.sqldelight:sqlite-driver:${Versions.SQL_DELIGHT}"
        const val KTOR = "io.ktor:ktor-client-apache:${Versions.KTOR}"
    }

    object JS {
        const val TEST = "test-js"
        const val KOTLIN_X_HTML = "org.jetbrains.kotlinx:kotlinx-html-js:${Versions.KOTLIN_X_HTML}"
        const val KOTLIN_REACT = "org.jetbrains:kotlin-react:${Versions.KOTLIN_REACT}"
        const val KOTLIN_REACT_DOM = "org.jetbrains:kotlin-react-dom:${Versions.KOTLIN_REACT}"
        const val KTOR = "io.ktor:ktor-client-js:${Versions.KTOR}"
    }
}
