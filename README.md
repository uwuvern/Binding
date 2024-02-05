# Binding
[![](https://jitpack.io/v/uwuvern/binding.svg)](https://jitpack.io/#uwuvern/binding)

## Introduction
Binding is a Java library aimed at adding reactive "Bindable" objects for use in your personal projects, it's a very similar adaption to PPY's osu-framework bindable implementation, but with a lot of tweaks to make it nicer for me, I think it's a useful lib.

## Usage
You can get the latest version in the releases page, or you can use jitpack to get the latest version, the latest version is [![](https://jitpack.io/v/uwuvern/binding.svg)](https://jitpack.io/#uwuvern/binding)

If needed here's a reference to the release page [here](https://github.com/uwuvern/binding/releases)
### Gradle

### Groovy
First add the repository
```groovy
repositories {
    maven { url 'https://jitpack.io' }
}
```

Then add the dependency
```groovy
dependencies {
    implementation 'com.github.uwuvern:binding:{version}'
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
    implementation("com.github.uwuvern:binding:{version}")
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
    <groupId>com.github.uwuvern</groupId>
    <artifactId>binding</artifactId>
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
