# FXUtils

A utility library for common JavaFX functionalities.


## Getting Started

You can use FXUtils as a dependency for your JavaFX project. 

### Installing

FXUtils is built with Maven and Java 8. It is not available on Maven Central yet, so you have to clone the project and build it on your local machine. 
If you have Maven, just run `mvn clean install` from the top level folder, then add it as a dependency in your JavaFX project.

Make sure to use FXUtils with Java 8. It won't work on older versions of the JVM.


## Structure

- **fxu**: class(es) with generic static methods that don't have their own category
- **fxu.builder.***: builder-style classes and interfaces that streamline the construction of JavaFX views
- **fxu.controller**: base class for JavaFX controllers
- **fxu.dialog.***: utility methods and wrappers for JavaFX dialogs
- **fxu.event.***: utility methods for JavaFX events
- **fxu.exception**: custom exceptions
- **fxu.input**: extensions or alternate implementations of common JavaFX controls
- **fxu.intl**: support for localization / internationalization
- **fxu.keyboard**: utility methods for JavaFX keyboard-based functionalities


## Current version

2.1.9-SNAPSHOT

The library is being developed. Commits are not that frequent, but if you have a specific request, feel free to ask.

## Authors

* **Gabriele Vaccari** - *Initial work* - [Vibridi](https://github.com/vibridi/)

Currently there are no other contributors


## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details
