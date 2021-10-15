<p align="center">A currency converter application for most of the currencies used in the world.</p>
<p align="center">You can quickly convert and make mathematical operations between currencies.</p>
<p align="center"><a href="https://play.google.com/store/apps/details?id=mustafaozhan.github.com.mycurrencies"><img src="https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png" width="300px"></a></p>
<p align="center"><a href="https://www.codacy.com/gh/CurrencyConverterCalculator/CCC?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=CurrencyConverterCalculator/CCC&amp;utm_campaign=Badge_Grade"><img src="https://api.codacy.com/project/badge/Grade/2196f4447c32431b80d582a21ad749db"/></a>   <img src="https://github.com/CurrencyConverterCalculator/CCC/workflows/CCC%20CI/badge.svg">  <img src="https://img.shields.io/github/last-commit/CurrencyConverterCalculator/CCC.svg">  <img src="https://img.shields.io/github/issues/CurrencyConverterCalculator/CCC.svg">   <img src="https://img.shields.io/github/issues-closed/CurrencyConverterCalculator/CCC.svg">
<p align="center"><a href='https://ko-fi.com/B0B2TZMH' target='_blank'><img height='36' style='border:0px;height:36px;' src='https://az743702.vo.msecnd.net/cdn/kofi1.png?v=2' border='0' alt='Buy Me a Coffee at ko-fi.com' /></a></p>

## Included Currencies

[![currencies.png](https://github.com/CurrencyConverterCalculator/CCC/raw/master/art/currencies.png)](https://github.com/CurrencyConverterCalculator/CCC/raw/master/art/currencies.png)

## Main modules

*  [x] ![badge-android][badge-android] in production :muscle:
*  [x] ![badge-backend][badge-backend] in production :muscle:
*  [ ] ![badge-ios][badge-ios] in progress :bow:
*  [ ] ![badge-web][badge-web] coming soon :eyes:
*  [ ] ![badge-desktop][badge-desktop] coming soon :eyes:
*  [x] ![badge-client][badge-client] shared between FE targets :recycle:
*  [x] ![badge-common][badge-common] shared between all targets :recycle:

## How to clone

The project uses submodules, please clone it as below:

```shell
git clone https://github.com/CurrencyConverterCalculator/CCC.git &&
cd CCC &&
git submodule update --init --recursive
```

## How to run

Be sure that you have latest Android Studio Canary build installed and XCode 13.0 or later.

### Android

Select `android` from configurations and run

### iOS

```shell
cd CCC &&
./gradlew :client:podspec &&
cd ios/CCC
pod install --repo-update
```

Then open CCC/ios/CCC.xcworkspace with XCode and run the build

### backend

```shell
cd CCC &&
./gradlew :backend:run
```

## Screenshots

### Android

<img src="https://raw.githubusercontent.com/CurrencyConverterCalculator/CCC/master/art/android/1.png" width="400px"/><img src="https://raw.githubusercontent.com/CurrencyConverterCalculator/CCC/master/art/android/2.png" width="320px"/><img src="https://raw.githubusercontent.com/CurrencyConverterCalculator/CCC/master/art/android/3.png" width="400px"/><img src="https://raw.githubusercontent.com/CurrencyConverterCalculator/CCC/master/art/android/4.png" width="400px"/>

<img src="https://raw.githubusercontent.com/CurrencyConverterCalculator/CCC/master/art/android/5.png" width="400px"/><img src="https://raw.githubusercontent.com/CurrencyConverterCalculator/CCC/master/art/android/6.png" width="400px"/><img src="https://raw.githubusercontent.com/CurrencyConverterCalculator/CCC/master/art/android/7.png" width="400px"/><img src="https://raw.githubusercontent.com/CurrencyConverterCalculator/CCC/master/art/android/6.png" width="400px"/>

### iOS

<img src="https://github.com/CurrencyConverterCalculator/CCC/blob/master/art/ios/dark.gif?raw=true" width="400px"/> <img src="https://github.com/CurrencyConverterCalculator/CCC/blob/master/art/ios/light.gif?raw=true" width="400px"/>

## License
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

[badge-android]: https://img.shields.io/badge/app-android-green
[badge-ios]: https://img.shields.io/badge/app-ios-orange
[badge-backend]: https://img.shields.io/badge/app-backend-blue
[badge-web]: https://img.shields.io/badge/app-web-yellow
[badge-desktop]: https://img.shields.io/badge/app-desktop-red
[badge-client]: https://img.shields.io/badge/shared-client-white
[badge-common]: https://img.shields.io/badge/shared-common-darkgrey
