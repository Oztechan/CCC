<p align="center">A currency application for most of the currencies of world.You can quickly convert and make mathematichal operations between currencies</p>
<p align="center"><a href="https://play.google.com/store/apps/details?id=mustafaozhan.github.com.mycurrencies"><img src="https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png" width="300px"></a></p>
<!---<p align="center"><a href="https://play.google.com/store/apps/details?id=mustafaozhan.github.com.mycurrencies"><img src="https://www.androidpolice.com/wp-content/uploads/2016/03/nexus2cee_apkm2.gif" width="260px"></a></p>--->

## Features 
- 168 Currencies !!!
- Rates Updated every 10 minutes !
- Clean Interface
- Conversation
- Mathematical Calculation
- Offline Support
- Fast
- Filterable Currencies
- European Central Bank Rates

## Screenshots

<img src="https://i.postimg.cc/ZTpknnkt/8.png?dl=1" width="218px"/><img src="https://i.postimg.cc/c0kG5P7j/2.png?dl=1" width="218px"/><img src="https://i.postimg.cc/Ykk746qX/3.png?dl=1" width="218px"/><img src="https://i.postimg.cc/q4vdwpKy/4.png?dl=1" width="218px"/>

<img src="https://i.postimg.cc/fM3Q45Bb/5.png?dl=1" width="218px"/><img src="https://i.postimg.cc/XjkTKP7s/6.png?dl=1" width="218px"/><img src="https://i.postimg.cc/2kDsZYmq/7.png?dl=1" width="218px"/><img src="https://i.postimg.cc/ZZcS6WWJ/1.png?dl=1" width="218px"/>

## Included Currencies

<img src="https://i.postimg.cc/1yLhy6jr/cover.png?dl=1"/>

## Dependencies
```
dependencies {
    implementation fileTree(include: ['*.jar'], dir: 'libs')
    implementation "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlin_version"
    //noinspection GradleCompatible
    implementation "com.android.support:design:${rootProject.ext.supportLibraryVersion}"
    testImplementation 'junit:junit:4.12'
    androidTestImplementation 'com.android.support.test:runner:1.0.2'
    androidTestImplementation 'com.android.support.test.espresso:espresso-core:3.0.2'
    implementation "com.android.support:recyclerview-v7:${rootProject.ext.supportLibraryVersion}"
    implementation 'com.android.support.constraint:constraint-layout:1.1.3'

    // Dagger
    kapt "com.google.dagger:dagger-compiler:${rootProject.ext.daggerVersion}"
    implementation "com.google.dagger:dagger:${rootProject.ext.daggerVersion}"

    // Rx
    implementation "com.jakewharton.rxbinding2:rxbinding-kotlin:${rootProject.ext.rxBindingVersion}"

    // LiveData
    implementation 'android.arch.lifecycle:extensions:1.1.1'

    // Http client
    implementation "com.squareup.okhttp3:okhttp:${rootProject.ext.okHttpVersion}"

    // Retrofit
    implementation "com.google.code.gson:gson:${gsonVersion}"
    implementation "com.squareup.retrofit2:retrofit:${rootProject.ext.retrofitVersion}"
    implementation "com.squareup.retrofit2:converter-gson:${rootProject.ext.retrofitVersion}"
    implementation 'com.squareup.retrofit2:adapter-rxjava2:2.4.0'

    // Material Spinner
    implementation 'com.jaredrummler:material-spinner:1.2.1'

    // Loading View
    implementation 'com.wang.avi:library:2.1.3'

    // Room
    implementation 'android.arch.persistence.room:runtime:1.1.1'
    kapt "android.arch.persistence.room:compiler:1.1.1"

    // Admob
    implementation 'com.google.android.gms:play-services-ads:17.1.3'

    // Firebase
    implementation 'com.google.firebase:firebase-core:16.0.7'

    // Crashlytics
    implementation 'com.crashlytics.sdk.android:crashlytics:2.9.9'

    // Anko
    implementation 'org.jetbrains.anko:anko-commons:0.10.5'

    // Multidex
    implementation 'com.android.support:multidex:1.0.3'

    // Snacky
    implementation 'com.github.matecode:Snacky:1.0.3'

    implementation files('libs/MathParser.org-mXparser-v.4.2.0-jdk.1.7.jar')
}
```

### License
Copyright 2017 Mustafa Ozhan

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
