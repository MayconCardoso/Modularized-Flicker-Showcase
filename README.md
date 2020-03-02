# Modularized-Flicker-Showcase

[![CircleCI](https://circleci.com/gh/MayconCardoso/Modularized-Flicker-Showcase/tree/master.svg?style=svg)](https://circleci.com/gh/MayconCardoso/Modularized-Flicker-Showcase/tree/master)

This is a simple [Flicker](https://www.flickr.com/services/apps/create/apply/) Single Activity application Showcase. It has been structured in a multi-module fashion, with semantics guided by Clean Architecture; this means that high-level modules don't know anything about low-level ones.

In order to avoid writing architecture boilerplate I have used one of my personal libraries: [ArchitectureBoilerplateGenerator](https://github.com/MayconCardoso/ArchitectureBoilerplateGenerator)

## Setup
- Create an account on [Marvel Developer Console](https://developer.marvel.com/)
- On your ```local.properties``` file, create these two variables and fill it up with your marvel api credentions.

```
publicKey = "YOUR_PUBLIC_KEY"
```
