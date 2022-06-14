# Gamedge

![Min API](https://img.shields.io/badge/API-21%2B-orange.svg?style=flat)
[![Platform](https://img.shields.io/badge/platform-Android-green.svg)](http://developer.android.com/index.html)
[![Build](https://github.com/mars885/gamedge/workflows/Build/badge.svg?branch=master)](https://github.com/mars885/gamedge/actions)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

An Android application for browsing video games and checking the latest gaming news from around the world.

Built entirely using the Jetpack Compose.

The aim of this project is to showcase the latest trends in Android development by utilizing the best practices, libraries, and tools to develop a fully-fledged Android application.

## Contents
* [Demonstration](#demonstration)
  * [Videos](#videos)
  * [Screenshots](#screenshots)
* [Tech Stack](#tech-stack)
* [Architecture](#architecture)
* [Development Setup](#development-setup)
  * [IGDB](#igdb)
  * [Gamespot](#gamespot)
* [Download](#download)
* [Contribution](#contribution)
* [Questions](#questions)
* [License](#license)

## Demonstration

### Videos

<a href="https://user-images.githubusercontent.com/14782808/111520186-88671800-8760-11eb-8995-8e45a5cd9213.mp4">
<img src="/media/demo1_thumbnail.png" width="32%"/>
</a>
<a href="https://user-images.githubusercontent.com/14782808/111520260-9b79e800-8760-11eb-9665-1062ed2b2c24.mp4">
<img src="/media/demo2_thumbnail.png" width="32%"/>
</a>
<a href="https://user-images.githubusercontent.com/14782808/111520365-b187a880-8760-11eb-9dbe-0ffc44635ef8.mp4">
<img src="/media/demo3_thumbnail.png" width="32%"/>
</a>

### Screenshots

<p>
<img src="/media/screenshot1.jpg" width="32%"/>
<img src="/media/screenshot2.jpg" width="32%"/>
<img src="/media/screenshot3.jpg" width="32%"/>
</p>
<p>
<img src="/media/screenshot4.jpg" width="32%"/>
<img src="/media/screenshot5.jpg" width="32%"/>
<img src="/media/screenshot6.jpg" width="32%"/>
</p>

## Tech Stack

- [Kotlin](https://kotlinlang.org/) - First class and official programming language for Android development.
- [Jetpack Compose](https://developer.android.com/jetpack/compose) - Android’s modern toolkit for building native UI.
- [Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html) and [Flow](https://kotlinlang.org/docs/reference/coroutines/flow.html#asynchronous-flow) - Official Kotlin's tooling for performing asynchronous work.
- [MVVM/MVI Architecture](https://developer.android.com/jetpack/guide) - Official recommended architecture for building robust, production-quality apps.
- [Android Jetpack](https://developer.android.com/jetpack) - Jetpack is a suite of libraries to help developers build state-of-the-art applications.
  - [Navigation Compose](https://developer.android.com/jetpack/compose/navigation) - Navigation Compose is a framework for navigating between composables while taking advantage of the Navigation component’s infrastructure and features.
  - [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) - The ViewModel is designed to store and manage UI-related data in a lifecycle conscious way.
  - [StateFlow](https://developer.android.com/kotlin/flow/stateflow-and-sharedflow#stateflow) - StateFlow is a state-holder observable flow that emits the current and new state updates to its collectors.
  - [Room](https://developer.android.com/topic/libraries/architecture/room) - The Room library provides an abstraction layer over SQLite to allow for more robust database access.
  - [DataStore](https://developer.android.com/topic/libraries/architecture/datastore) - DataStore is a data storage solution that stores key-value pairs or typed objects with [protocol buffers](https://developers.google.com/protocol-buffers).
  - [Dagger Hilt](https://developer.android.com/training/dependency-injection/hilt-android) - Hilt is a dependency injection library for Android.
  - [MotionLayout](https://developer.android.com/training/constraint-layout/motionlayout) - MotionLayout allows you to create beautiful animations in your app without too much hassle.
  - [Custom Tabs](https://developers.google.com/web/android/custom-tabs/implementation-guide) - Custom Tabs is a browser feature that gives apps more control over their web experience.
- [Accompanist](https://github.com/google/accompanist) - A collection of extension libraries for Jetpack Compose.
- [OkHttp](https://github.com/square/okhttp) - An HTTP client for making network calls.
- [Retrofit](https://github.com/square/retrofit) - A library for building REST API clients.
- [KotlinX Serialization](https://github.com/Kotlin/kotlinx.serialization) - A multiplatform Kotlin serialization library.
- [Coil](https://github.com/coil-kt/coil) - An image loading library.
- [Hilt Binder](https://github.com/mars885/hilt-binder) - An annotating processing library that automatically generates Dagger Hilt's `@Binds` methods.
- [Kotlin Result](https://github.com/michaelbull/kotlin-result) - A multiplatform Result monad for modelling success or failure operations.
- [Detekt](https://github.com/detekt/detekt) - A static code analysis library for Kotlin.
- [Ktlint](https://github.com/pinterest/ktlint) - A library for formatting Kotlin code according to official guidelines.
- [Testing](https://developer.android.com/training/testing) - The app is currently covered with unit tests and instrumentation tests.
  - [JUnit](https://junit.org/junit5) - JUnit is a unit testing framework for the Java programming language.
  - [AssertJ](https://assertj.github.io/doc) - AssertJ is a java library providing a rich set of assertions.
  - [MockK](https://github.com/mockk/mockk) - MockK is a mocking library for Kotlin.
  - [Coroutines Test](https://github.com/Kotlin/kotlinx.coroutines/tree/master/kotlinx-coroutines-test) - A library for testing Kotlin coroutines.
  - [Turbine](https://github.com/cashapp/turbine) - A testing library for Kotlin Flows.
  - [Dagger Hilt Test](https://developer.android.com/training/dependency-injection/hilt-testing) - A testing library for modifying the Dagger bindings in instrumented tests.
  - [Room Testing](https://developer.android.com/training/data-storage/room/migrating-db-versions#test) - A library for testing Room migrations.
  - [MockWebServer](https://github.com/square/okhttp/tree/master/mockwebserver) - A scriptable web server for testing HTTP clients.
- [Gradle's Kotlin DSL](https://docs.gradle.org/current/userguide/kotlin_dsl.html) - Gradle’s Kotlin DSL is an alternative syntax to the Groovy DSL with an enhanced editing experience.
- [buildSrc](https://docs.gradle.org/current/userguide/organizing_gradle_projects.html#sec:build_sources) - A special module within the project to manage dependencies and whatnot.

For more information about used dependencies, see [this](/buildSrc/src/main/java/Dependencies.kt) file.

## Architecture

![architecture](/media/architecture.png)

## Development Setup

You'll need to supply API/client keys for the various services that the app uses in order to build the application.

### IGDB

[IGDB](https://www.igdb.com/discover) is a website dedicated to combining all the relevant information about games into a comprehensive resource for gamers everywhere. This is the main API that the app uses to fetch information about pretty much any video game there is.

Check [this link](https://api-docs.igdb.com/#account-creation) on how to obtain a client ID and secret. Once you have obtained the keys, you can set them in your `~/.gradle/gradle.properties`:

```
TWITCH_APP_CLIENT_ID=yout_client_id_here
TWITCH_APP_CLIENT_SECRET=your_client_secret_here
```

### Gamespot

[Gamespot](https://www.gamespot.com/) is a video gaming website that provides news, reviews, previews, downloads, and other information on video games. The app uses its API to solely retrieve the latest news in the gaming world.

Check [this link](https://www.gamespot.com/api/) on how to obtain an API key. Once you have obtained the key, you can set it in your `~/.gradle/gradle.properties`:

```
GAMESPOT_API_KEY=your_api_key_here
```

## Download

Go to the [Releases](https://github.com/mars885/gamedge/releases) to download the latest APK.

## Contribution

See the [CONTRIBUTION.md](/CONTRIBUTION.md) file.

## Questions

If you have any questions regarding the codebase, hit me up on [Twitter](https://twitter.com/PRybitskyi).

## License

Gamedge is licensed under the [Apache 2.0 License](LICENSE).
