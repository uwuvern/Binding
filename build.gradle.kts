/*
 * Copyright (c) 2024 Ashley (uwuvern) <uwuvern@outlook.com>
 *
 * This project is licensed under the MIT license, check the root of the project for
 * more information.
 */

plugins {
    id("java")
    `maven-publish`
}

group = "me.ashydev.bindable"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = properties["groupId"].toString()
            artifactId = properties["artifactId"].toString()
            version = properties["version"].toString()

            from(components["java"])
        }
    }
}

repositories {
    mavenCentral()
    maven ("https://jitpack.io")
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    implementation("com.github.jitpack:gradle-simple:1.0")
}

tasks.test {
    useJUnitPlatform()
}