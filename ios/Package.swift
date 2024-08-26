// swift-tools-version: 5.10
// The swift-tools-version declares the minimum version of Swift required to build this package.

import PackageDescription

let package = Package(
    name: "CCC",
    products: [
        .library(
            name: "CCC",
            targets: ["CCC"]
        )
    ],
    dependencies: [
        .package(
            url: "https://github.com/firebase/firebase-ios-sdk",
            from: "10.29.0"
        ),
        .package(
            url: "https://github.com/googleads/swift-package-manager-google-mobile-ads.git",
            from: "11.8.0"
        ),
        .package(
            url: "https://github.com/exyte/PopupView.git",
            from: "2.10.4"
        ),
        .package(
            url: "https://github.com/matteopuc/swiftui-navigation-stack",
            from: "1.0.6"
        )
    ],
    targets: [
        .target(name: "CCC")
    ]
)
