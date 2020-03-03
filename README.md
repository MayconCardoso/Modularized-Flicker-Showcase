# Modularized-Flicker-Showcase

[![CircleCI](https://circleci.com/gh/MayconCardoso/Modularized-Flicker-Showcase/tree/master.svg?style=svg&circle-token=2a1c5500456ffe02c742dc311012b8a7b13e35e0)](https://circleci.com/gh/MayconCardoso/Modularized-Flicker-Showcase/tree/master)

This is a simple [Flicker](https://www.flickr.com/services/apps/create/apply/) Single Activity application Showcase. It has been structured in a multi-module fashion, with semantics guided by Clean Architecture; this means that high-level modules don't know anything about low-level ones.

In order to avoid writing architecture boilerplate I have used one of my personal libraries: [ArchitectureBoilerplateGenerator](https://github.com/MayconCardoso/ArchitectureBoilerplateGenerator)

## Setup
- Create an account on [Flicker Developer Console](https://www.flickr.com/services/apps/create/apply/)
- On your ```local.properties``` file, create a variable and fill it up with your flicker public api key.

```
publicKey = "YOUR_PUBLIC_KEY"
```
## Features
- Search a tag
- Tag search history
- List photos of the searched tag
- Infinite pagination
- Support offline 
- Memory efficient
- Swipe to refresh and invalidate cache

## Architecture

As I mentioned before, it was guided by Clean Architecture, which means that we have at least three layers: 
- [Domain](https://github.com/MayconCardoso/Modularized-Flicker-Showcase/tree/master/features/feature-flicker-domain)
It is a whole kotlin module, without Android dependencies, where we can find every Business Logic like ```interactions``` or ```UseCases```. Every ```UseCase``` is guided by a [Result](https://github.com/MayconCardoso/Modularized-Flicker-Showcase/blob/master/features/feature-flicker-domain/src/main/java/com/mctech/showcase/feature/flicker_domain/interactions/Result.kt) state pattern to make sure that an unexpected issue doesn't happen

- [Data](https://github.com/MayconCardoso/Modularized-Flicker-Showcase/tree/master/features/feature-flicker-data)
It is an Android module, with all data logic, which means that it has the whole [orchestration](https://github.com/MayconCardoso/Modularized-Flicker-Showcase/blob/master/features/feature-flicker-data/src/main/java/com/mctech/showcase/feature/flicker_data/photo/FlickerStrategyRepository.kt) of Data Sources like [Database](https://github.com/MayconCardoso/Modularized-Flicker-Showcase/blob/master/features/feature-flicker-data/src/main/java/com/mctech/showcase/feature/flicker_data/photo/local/FlickerLocalDataSource.kt), [Memory Cache](https://github.com/MayconCardoso/Modularized-Flicker-Showcase/blob/master/features/feature-flicker-data/src/main/java/com/mctech/showcase/feature/flicker_data/photo/cache/FlickerCacheDataSource.kt) and [Remote](https://github.com/MayconCardoso/Modularized-Flicker-Showcase/blob/master/features/feature-flicker-data/src/main/java/com/mctech/showcase/feature/flicker_data/photo/remote/FlickerRemoteDataSourceImpl.kt)

- [Presentation](https://github.com/MayconCardoso/Modularized-Flicker-Showcase/tree/master/features/feature-flicker-presentation)
It is an Android module, with all UI logic. MVVM is the architecture pattern used on the app. A [Component State](https://github.com/MayconCardoso/Modularized-Flicker-Showcase/blob/master/libraries/library-architecture/src/main/java/com/mctech/library/architecture/ComponentState.kt) pattern is used to make it easier to be tested

## Unit test
I have covered initially 100% of ```domain``` layer, ```ViewModel``` implementations and ```Repositories```. But the whole code is easily testable.

## Libraries

Here you can check out the [dependencies file.](https://github.com/MayconCardoso/Modularized-Flicker-Showcase/blob/master/build-dependencies.gradle) But basically these are the libraries used in this example:

- [Koin](https://insert-koin.io/)
- [Navigation Component](https://developer.android.com/guide/navigation/)
- [LifeCycle](https://developer.android.com/topic/libraries/architecture/lifecycle)
- [LiveData](https://developer.android.com/topic/libraries/architecture/livedata)
- [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)
- [Retrofit](https://github.com/square/retrofit)
- [Picasso](https://github.com/square/picasso)
