<p align="center">A currency application for most of the currencies of world.You can quickly convert and make mathematical operations between currencies</p>
<p align="center"><a href="https://play.google.com/store/apps/details?id=mustafaozhan.github.com.mycurrencies"><img src="https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png" width="300px"></a></p>
<p align="center"><a href="https://www.codacy.com/gh/CurrencyConverterCalculator/androidCCC?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=CurrencyConverterCalculator/androidCCC&amp;utm_campaign=Badge_Grade"><img src="https://api.codacy.com/project/badge/Grade/2196f4447c32431b80d582a21ad749db"/></a>   <img src="https://travis-ci.com/CurrencyConverterCalculator/androidCCC.svg?branch=master">  <img src="https://img.shields.io/github/last-commit/CurrencyConverterCalculator/androidCCC.svg">  <img src="https://img.shields.io/github/issues/CurrencyConverterCalculator/androidCCC.svg">   <img src="https://img.shields.io/github/issues-closed/CurrencyConverterCalculator/androidCCC.svg">  <img src="https://img.shields.io/github/license/CurrencyConverterCalculator/androidCCC.svg"></p>
<p align="center"><a href='https://ko-fi.com/B0B2TZMH' target='_blank'><img height='36' style='border:0px;height:36px;' src='https://az743702.vo.msecnd.net/cdn/kofi1.png?v=2' border='0' alt='Buy Me a Coffee at ko-fi.com' /></a></p>

## Features

-   168 Currencies !
-   Rates Updated every 10 minutes !
-   Clean Interface
-   Conversation
-   Mathematical Calculation
-   Offline Support
-   Fast
-   Filterable Currencies

## Screenshots

<img src="https://i.postimg.cc/7wQ3PPVM/1.png?dl=1" width="216px"/><img src="https://i.postimg.cc/sfgWybGN/2.png?dl=1" width="216px"/><img src="https://i.postimg.cc/4s3vTHzx/3.png?dl=1" width="216px"/><img src="https://i.postimg.cc/HTcQrGvG/4.png?dl=1" width="216px"/>

<img src="https://i.postimg.cc/HpbXtwCB/5.png?dl=1" width="216px"/><img src="https://i.postimg.cc/ZbZ3sWbZ/6.png?dl=1" width="216px"/><img src="https://i.postimg.cc/VzxXM72J/7.png?dl=1" width="216px"/><img src="https://i.postimg.cc/cxm3pdC3/8.png?dl=1" width="216px"/>

## Included Currencies

<img src="https://i.postimg.cc/1yLhy6jr/cover.png?dl=1"/>

## Dependencies
```gradle
dependencies {
    implementation(
            kotlin,
            androidMaterial,
            recyclerView,
            constraintLayout,
            dagger,
            okHttp,
            moshi,
            materialSpinner,
            multiDex,
            room,
            coroutines,
            admob,
            firebaseConfig,
            snacky,
            toasty,
            loadingView,
            jodaTime,
            rxAndroid,
            navigation,
            retrofit.values(),
            lifeCycle.values()
    )

    kapt(annotations.values())

    testImplementation(jUnit)

    implementation(files(mxParser))

    implementation project(':scopemob')
    implementation project(':basemob')
    implementation project(':logmob')
}
```

### License
Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
