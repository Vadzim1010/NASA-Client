# Popular Movie List Sample App (Compose/Navigation)

## About

I created this project to show a sample of my coding and architecture skills. It based on the modern stack of
technologies, and has modular architecture.


## Project structure

The project has a structure with multiple modules. It has three modules(app, data, domain). The main **app module** is
responsible of presentation. The **data module** is responsible of receiving and processing data. And **domain module**
has no dependencies with other layers and it contains, Use cases & Repository Interfaces..


## Architecture
The app is implemented in compliance with the [Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html) principles.
**MVVM** architecture is used for the presentation layer.


### Presentation layer
Fragments, Activities and other classes responsible for UI.

* **View.** A Fragment instance that doesn't contain any states, data and has no logic, except displaying UI. Informing the ViewModel about user interactions, displaying data and states received from ViewModel.
* **ViewModel.** A class containing all data and states of View. The ViewModel retains while View is recreating. It doesn't contain any Android SDK code. Loads data using the Use Case. Manage the View with changing a Flow value.

### Domain layer
* **Use case** A class combining data loaded from different Repositories so that the ViewModel has a single data source.

### Data layer
* **Repository.** A class loading data from different sources such as the Internet, Database.
* **REST API.** A Retrofit interface helping with HTTP calls.
* **DAO and Database.** A Room database and DAO classes operating with it.


### Libraries
* [Kotlin Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html) - perform async operations.
* [AndroidX](https://developer.android.com/jetpack/androidx) - AndroidX is a major improvement to the original Android Support Library, which is no longer maintained.
* [Material](https://material.io/develop/android/docs/getting-started/) - Material Design themes.
* [Room](https://developer.android.com/topic/libraries/architecture/room) - an abstraction layer over the SQLite database.
* [Retrofit2](https://square.github.io/retrofit/) - a HTTP client.
* [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) - the realization of MVVM architecture.
* [Coil](https://github.com/coil-kt/coil) - an image loading library for Android backed by Kotlin Coroutines.
* [Koin](https://github.com/InsertKoinIO/koin) - a dependency injection framework for Kotlin.
* [Google Map](https://developers.google.com/maps/documentation/android-sdk) - for working with maps  
