# Binding
[![](https://jitpack.io/v/uwuvern/binding.svg)](https://jitpack.io/#uwuvern/binding)

## Introduction
Binding is a Thread Safe Java library aimed at adding reactive "Bindable" objects for use in your personal projects, it's a very similar adaption to PPY's osu-framework bindable implementation, but with a lot of tweaks to hopefully make this useful for java projects, and before you ask, no these are not AtomicReferences, if you are going to complain to me about saying its the same thing, no it isnt, this is not the place to complain, I don't want to hear about that, these work very differently, please read the docs, and actually learn java before complaining to me about that.

## Wiki
Head over to the wiki for more information on how to use bindables [here](https://github.com/uwuvern/binding/wiki)

## Usage
You can get the latest version in the releases page, or you can use jitpack to get the latest version, the latest version is [![](https://jitpack.io/v/uwuvern/binding.svg)](https://jitpack.io/#uwuvern/binding)

If needed here's a reference to the release page [here](https://github.com/uwuvern/binding/releases)

This project is multi modular, so using it requires having the modules as dependencies, the current modules are

- core      | the base implementation of the bindables
- bindables | extra bindable types
- kotlin    | The kotlin extensions for bindables

please replace the {module} field with your desired module & the {version} field with your desired version

### Gradle

### Groovy
First add the repository
```groovy
repositories {
    maven { url 'https://jitpack.io' }
}
```

Then add the dependenc(y/ies)
```groovy
dependencies {
    implementation 'com.github.uwuvern.binding:{module}:{version}'
}
```

### Kotlin
First add the repository
```kotlin
repositories {
    maven("https://jitpack.io")
}
```
Then add the dependency
```kotlin
dependencies {
    implementation("com.github.uwuvern.binding:{module}:{version}")
}
```

### Maven
First add the repository
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```
Then add the dependency
```xml
<dependency>
    <groupId>com.github.uwuvern.binding</groupId>
    <artifactId>{module}</artifactId>
    <version>{version}</version>
</dependency>
```

# Quick Notice
## Adaptation
this is a code adaption in java of ppy's bindable implementation in c#, for the source of the original code please visit [here](https://github.com/ppy/osu-framework/blob/master/osu.Framework/Bindables/Bindable.cs), this version is aimed at rewriting it to have more features, and to work with java, another commonly used language similar to c#, all credit of the original implementation goes to ppy, and the osu-framework contributors.

# Contributing
## Issues
If you find any issues regarding the library, please open a ticket, and explain in detail your issue, and it will be discussed within the ticket before any action is taken.
## Pull Requests
If you wish to contribute to the library, please fork the project, create a branch to work on, and then once finished create a pull request explaining in detail your changes, why you want them changed, what is the purpose, and how it will affect the library, and it will be discussed within the pull request before any action is taken.
