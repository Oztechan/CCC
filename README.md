<h1 align="center">Currency Converter & Calculator</h1>

<div align="center">

A currency converter application for most of the currencies used in the world.

You can quickly convert and make mathematical operations between currencies.

<a href="https://play.google.com/store/apps/details?id=mustafaozhan.github.com.mycurrencies"><img src="https://raw.githubusercontent.com/CurrencyConverterCalculator/CCC/develop/art/play_store.png?raw=true" width="175px"></a>
<a href="https://apps.apple.com/us/app/currency-converter-calculator/id1617484510"><img src="https://raw.githubusercontent.com/CurrencyConverterCalculator/CCC/develop/art/app_store.png?raw=true" width="175px"></a>
<a href="https://appgallery.huawei.com/app/C104920917"><img src="https://raw.githubusercontent.com/CurrencyConverterCalculator/CCC/develop/art/appgallery.png?raw=true" width="175px"></a>

<a href="https://github.com/Oztechan/CCC/actions/workflows/main.yml"><img src="https://github.com/CurrencyConverterCalculator/CCC/workflows/CCC%20CI/badge.svg"></a>
<a href="https://www.codacy.com/gh/Oztechan/CCC/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=CurrencyConverterCalculator/CCC&amp;utm_campaign=Badge_Grade"><img src="https://app.codacy.com/project/badge/Grade/2196f4447c32431b80d582a21ad749db"/></a>
<a href="https://codecov.io/gh/Oztechan/CCC"><img src="https://codecov.io/gh/Oztechan/CCC/branch/develop/graph/badge.svg?token=Lenq2MZgM7"/></a>
<a href="https://www.codacy.com/gh/Oztechan/CCC/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=CurrencyConverterCalculator/CCC&amp;utm_campaign=Badge_Coverage"><img src="https://app.codacy.com/project/badge/Coverage/2196f4447c32431b80d582a21ad749db"/></a>

<a href="https://github.com/Oztechan/CCC/commits/develop"><img src="https://img.shields.io/github/last-commit/Oztechan/CCC?label=Last%20commit&color=blue"></a>
<a href="https://github.com/Oztechan/CCC/pulls?q=is%3Apr+is%3Aopen"><img src="https://img.shields.io/github/issues-pr-raw/Oztechan/CCC?label=Open%20PR&color=green0light"></a>
<a href="https://github.com/Oztechan/CCC/pulls?q=is%3Apr+is%3Aclosed"><img src="https://img.shields.io/github/issues-pr-closed-raw/Oztechan/CCC?label=Closed%20PR&color=white"></a>
<a href="https://github.com/Oztechan/CCC/issues?q=is%3Aopen"><img src="https://img.shields.io/github/issues-raw/Oztechan/CCC?label=Open%20Issue&color=green-light"></a>
<a href="https://github.com/Oztechan/CCC/issues?q=is%3Aclosed"><img src="https://img.shields.io/github/issues-closed-raw/Oztechan/CCC?label=Closed%20Issue&color=white"></a>

<a href='https://ko-fi.com/B0B2TZMH' target='_blank'><img height='36' style='border:0px;height:36px;' src='https://az743702.vo.msecnd.net/cdn/kofi1.png?v=2' border='0' alt='Buy Me a Coffee at ko-fi.com' /></a>

</div>

## Modularization

```mermaid
graph TD;

    client-->android(android)
    client-->ios(ios)

    common-->client
    common-->backend(backend)
    
    submodule{submodule}

    test
```

All the modules in the project are grouped into 6 targets:

- `android`, `ios` and `backend` are app modules that contains platform only codes
- `client` is a KMM module that shared between `ios` and `android`.
- `common` is a KMP modules that shared between all the platforms (`android`, `ios` and `backend`)
- `submodule` these are different git repositories and can be used in any of these modules. (arrows are not shown for the sake of simplicity)
- `test` contains test cases for architecture and coding conventions

## How to clone

The project uses submodules, please clone it as below:

```shell
git clone https://github.com/CurrencyConverterCalculator/CCC.git &&
cd CCC &&
git submodule update --init --recursive
```

Submodules:

- [LogMob](https://github.com/SubMob/LogMob) KMP logging library with Crashlytics support
- [ScopeMob](https://github.com/SubMob/ScopeMob) Useful set of Kotlin scope functions with KMP support
- [BaseMob](https://github.com/SubMob/BaseMob) Android base classes
- [ParserMob](https://github.com/SubMob/ParserMob) KMP parsing library

## How to run

Be sure that you have latest Android Studio Canary build installed and XCode 13.0 or later.

### Android

Open CCC folder with Android Studio and select `android:app` from configurations and run

### iOS

Open `CCC/ios/CCC.xcworkspace` with XCode after the packages are resolved you can run the project. Generally you should use the latest stable XCode version.

### Backend

```shell
./gradlew :backend:run
```

## Testing

After you run the app probably your all API calls will fail, it is expected since the private URLs are not shared publicly. If you want the test the app with real API calls, I have prepared a fake response. Please replace all the `getConversion` methods in

- `com.oztechan.ccc.common.core.network.api.backend.BackendApiImpl`
- `com.oztechan.ccc.common.core.network.api.premium.PremiumApiImpl`

with below;

```kotlin
override suspend fun getConversion(base: String): ExchangeRate = client.get {
    url {
        takeFrom("https://gist.githubusercontent.com/mustafaozhan/fa6d05e65919085f871adc825accea46/raw/d3bf3a7771e872e0c39541fe23b4058f4ae24c41/response.json")
    }
}.body()
```

## Architecture

```mermaid
graph TD;
    subgraph Backend[Backend]
        database(database) --> datasource
        api(api) --> service
    
        datasource --> Controller
        service --> Controller
    
        Controller --> App
    end
    
    subgraph Mobile[Mobile]
        Persistence(Persistence) --> Storage
        Database(Database) --> DataSource
    
        API(API) --> Service
        RemoteConfig(RemoteConfig) --> ConfigService
    
        Storage --> ViewModel
        DataSource --> ViewModel
    
        Repository --> ViewModel
    
        Storage --> Repository
        DataSource --> Repository
        Service --> Repository
        ConfigService --> Repository
    
        Service --> ViewModel
        ConfigService --> ViewModel
    
        ViewModel --> Views
    end
```

## Android Preview

<img src="https://raw.githubusercontent.com/CurrencyConverterCalculator/CCC/develop/art/android/1.png" width="400px"/><img src="https://raw.githubusercontent.com/CurrencyConverterCalculator/CCC/develop/art/android/2.png" width="320px"/><img src="https://raw.githubusercontent.com/CurrencyConverterCalculator/CCC/develop/art/android/3.png" width="400px"/><img src="https://raw.githubusercontent.com/CurrencyConverterCalculator/CCC/develop/art/android/4.png" width="400px"/>

<img src="https://raw.githubusercontent.com/CurrencyConverterCalculator/CCC/develop/art/android/5.png" width="400px"/><img src="https://raw.githubusercontent.com/CurrencyConverterCalculator/CCC/develop/art/android/6.png" width="400px"/><img src="https://raw.githubusercontent.com/CurrencyConverterCalculator/CCC/develop/art/android/7.png" width="400px"/><img src="https://raw.githubusercontent.com/CurrencyConverterCalculator/CCC/develop/art/android/6.png" width="400px"/>

## iOS Preview

<img src="https://github.com/CurrencyConverterCalculator/CCC/blob/develop/art/ios/dark.gif?raw=true" width="400px"/> <img src="https://github.com/CurrencyConverterCalculator/CCC/blob/develop/art/ios/light.gif?raw=true" width="400px"/>

## Included Currencies

[![currencies.png](https://github.com/CurrencyConverterCalculator/CCC/raw/develop/art/currencies.png)](https://github.com/CurrencyConverterCalculator/CCC/raw/develop/art/currencies.png)

## License

```text
Copyright 2017 Mustafa Ozhan

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```