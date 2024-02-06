plugins {
    id("java")
    `maven-publish`
}

group = properties["group"] as String
version = properties["version"] as String

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    implementation(project(":common"))
    implementation(project(":core"))
    implementation(project(":action"))

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}