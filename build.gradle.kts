/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath(Classpaths.androidBuildTools)
        classpath(Classpaths.kotlinGradlePlugin)
        classpath(Classpaths.gsmGoogle)
        classpath(Classpaths.crashlytics)
        classpath(Classpaths.navigation)
    }
}

allprojects {
    repositories {
        jcenter()
        google()
    }
}
