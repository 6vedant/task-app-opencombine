// swift-tools-version: 5.8
// The swift-tools-version declares the minimum version of Swift required to build this package.

import PackageDescription

let package = Package(
    name: "SwiftAndroidExample",
    products: [
        // Products define the executables and libraries a package produces, making them visible to other packages.
        .library(
            name: "SwiftAndroidExample",
            type: .dynamic,
            targets: ["SwiftAndroidExample"]),
    ],
    dependencies: [
        .package(url: "https://github.com/scade-platform/swift-java.git", branch: "main"),
        .package(url: "https://github.com/apple/swift-crypto.git", .branch("main"))
    ],
    targets: [
        // Targets are the basic building blocks of a package, defining a module or a test suite.
        // Targets can depend on other targets in this package and products from dependencies.
        .target(
            name: "SwiftAndroidExample",
            dependencies: [
                    .product(name: "Java", package: "swift-java"),
                    .product(name: "Crypto", package: "swift-crypto")
            ]),
        .testTarget(
            name: "SwiftAndroidExampleTests",
            dependencies: ["SwiftAndroidExample"]),
    ]
)
