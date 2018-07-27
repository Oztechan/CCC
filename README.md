<p align="center">A currency application for popular currencies listed in European Central Bank.You can quickly convert and make mathematichal operations between currencies</p>
<p align="center"><a href="https://play.google.com/store/apps/details?id=mustafaozhan.github.com.mycurrencies"><img src="https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png"></a></p>

## Features 
- Clean Interface
- Conversation
- Mathematical Calculation
- Offline Support
- Fast
- Filterable Currencies
- European Central Bank Rates

## Screenshots


<img src="https://s19.postimg.cc/vryhmo9ub/image.png" width="218px"/> <img src="https://s19.postimg.cc/5jncxb56b/image.png" width="218px"/> <img src="https://s19.postimg.cc/aiavbv3ub/image.png" width="218px"/> <img src="https://s19.postimg.cc/ksdab3m03/image.png" width="218px"/>

<img src="https://s19.postimg.cc/sxvc99poj/image.png" width="218px"/> <img src="https://s19.postimg.cc/4hd6eru2r/image.png" width="218px"/> <img src="https://s19.postimg.cc/l54oh99er/image.png" width="218px"/> <img src="https://s19.postimg.cc/a5jh5o64z/image.png" width="218px"/>


## Dependencies
```
dependencies {
    implementation fileTree(dir: 'libs', include: ['*.jar'])
    implementation "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlin_version"
    implementation "com.android.support:design:${rootProject.ext.supportLibraryVersion}"
    testImplementation 'junit:junit:4.12'
    androidTestImplementation 'com.android.support.test:runner:1.0.2'
    androidTestImplementation 'com.android.support.test.espresso:espresso-core:3.0.2'

    implementation "com.android.support:recyclerview-v7:${rootProject.ext.supportLibraryVersion}"
    implementation 'com.android.support.constraint:constraint-layout:1.1.2'

    // Dagger
    kapt "com.google.dagger:dagger-compiler:${rootProject.ext.daggerVersion}"
    implementation "com.google.dagger:dagger:${rootProject.ext.daggerVersion}"

    // Rx java
    implementation "com.jakewharton.rxbinding2:rxbinding-kotlin:${rootProject.ext.rxBindingVersion}"

    // LiveData
    implementation "android.arch.lifecycle:extensions:1.1.1"

    // Http client
    implementation "com.squareup.okhttp3:okhttp:${rootProject.ext.okHttpVersion}"

    // Retrofit
    implementation "com.google.code.gson:gson:${gsonVersion}"
    implementation "com.squareup.retrofit2:retrofit:${rootProject.ext.retrofitVersion}"
    implementation "com.squareup.retrofit2:converter-gson:${rootProject.ext.retrofitVersion}"
    implementation 'com.squareup.retrofit2:adapter-rxjava2:2.3.0'

    //material Spinner
    implementation 'com.jaredrummler:material-spinner:1.2.1'

    //Loading View
    implementation 'com.wang.avi:library:2.1.3'

    //Room
    implementation "android.arch.persistence.room:runtime:1.1.1"
    kapt "android.arch.persistence.room:compiler:1.1.1"

    //Admob
    implementation 'com.google.android.gms:play-services-ads:15.0.1'

    //Fabric
    implementation('com.crashlytics.sdk.android:crashlytics:2.9.4@aar') {
        transitive = true
    }

    //anko
    implementation 'org.jetbrains.anko:anko-commons:0.10.5'

    //multidex
    implementation 'com.android.support:multidex:1.0.3'

    //Snacky
    implementation 'com.github.matecode:Snacky:1.0.3'
}
```
