<p align="center">A currency converter application for most of the currencies used in the world. You can quickly convert and make mathematical operations between currencies</p>
<p align="center"><a href="https://play.google.com/store/apps/details?id=mustafaozhan.github.com.mycurrencies"><img src="https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png" width="300px"></a></p>
<p align="center"><a href="https://www.codacy.com/gh/CurrencyConverterCalculator/CCC?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=CurrencyConverterCalculator/CCC&amp;utm_campaign=Badge_Grade"><img src="https://api.codacy.com/project/badge/Grade/2196f4447c32431b80d582a21ad749db"/></a>   <img src="https://github.com/CurrencyConverterCalculator/CCC/workflows/CCC%20CI/badge.svg">  <img src="https://img.shields.io/github/last-commit/CurrencyConverterCalculator/CCC.svg">  <img src="https://img.shields.io/github/issues/CurrencyConverterCalculator/CCC.svg">   <img src="https://img.shields.io/github/issues-closed/CurrencyConverterCalculator/CCC.svg">
<p align="center"><a href='https://ko-fi.com/B0B2TZMH' target='_blank'><img height='36' style='border:0px;height:36px;' src='https://az743702.vo.msecnd.net/cdn/kofi1.png?v=2' border='0' alt='Buy Me a Coffee at ko-fi.com' /></a></p>

## Branches

* [master](https://github.com/CurrencyConverterCalculator/CCC/tree/master) - Kotlin Multi Platform (WIP)

    * ![badge][badge-android], completely moved to KMP (in beta, staged %50 in Production)
    * ![badge][badge-ios], In progress
    * ![badge][badge-backend], completely moved to KMP (in beta)
    * ![badge][badge-web], will be implemented
    * ![badge][badge-desktop], will be implemented
    * ![badge][badge-client], shared module for FE targets
    * ![badge][badge-common], shared module for BE and FE targets

* [android](https://github.com/CurrencyConverterCalculator/CCC/tree/android) - Pure Android market branch (in Production). If you want to see how the app was implemented with pure Android technologies and libraries check this branch!

* [minsdk16](https://github.com/CurrencyConverterCalculator/CCC/tree/minsdk16) - Android market branch with min sdk 16 support. (in Production for SDK16 - SDK21)

### :warning: Note

Project uses submodules, so if you clone it submodules will be missing. After cloning, inside the
project folder please run

```shell
git submodule update --init --recursive
```

## Included Currencies

[![currencies.png](https://github.com/CurrencyConverterCalculator/CCC/raw/master/art/currencies.png)](https://github.com/CurrencyConverterCalculator/CCC/raw/master/art/currencies.png)

## Android

<img src="https://raw.githubusercontent.com/CurrencyConverterCalculator/CCC/master/art/android/1.png" width="400px"/><img src="https://raw.githubusercontent.com/CurrencyConverterCalculator/CCC/master/art/android/2.png" width="320px"/><img src="https://raw.githubusercontent.com/CurrencyConverterCalculator/CCC/master/art/android/3.png" width="400px"/><img src="https://raw.githubusercontent.com/CurrencyConverterCalculator/CCC/master/art/android/4.png" width="400px"/>

<img src="https://raw.githubusercontent.com/CurrencyConverterCalculator/CCC/master/art/android/5.png" width="400px"/><img src="https://raw.githubusercontent.com/CurrencyConverterCalculator/CCC/master/art/android/6.png" width="400px"/><img src="https://raw.githubusercontent.com/CurrencyConverterCalculator/CCC/master/art/android/7.png" width="400px"/><img src="https://raw.githubusercontent.com/CurrencyConverterCalculator/CCC/master/art/android/6.png" width="400px"/>

## iOS

<img src="https://github.com/CurrencyConverterCalculator/CCC/blob/master/art/ios/dark.gif?raw=true" width="400px"/> <img src="https://github.com/CurrencyConverterCalculator/CCC/blob/master/art/ios/light.gif?raw=true" width="400px"/>

### License
```markdown
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

[badge-android]: https://img.shields.io/badge/module-android-green
[badge-ios]: https://img.shields.io/badge/module-ios-orange
[badge-backend]: https://img.shields.io/badge/module-backend-blue
[badge-web]: https://img.shields.io/badge/module-web-yellow
[badge-desktop]: https://img.shields.io/badge/module-desktop-red
[badge-client]: https://img.shields.io/badge/module-client-white
[badge-common]: https://img.shields.io/badge/module-common-darkgrey
