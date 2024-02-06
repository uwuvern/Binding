plugins {
    kotlin("jvm") version "1.9.22"
}
dependencies {
    implementation(project(":common"))
    implementation(project(":action"))
    implementation(project(":core"))
    implementation(project(":bindables"))

    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

kotlin {
    jvmToolchain(
        Integer.parseInt(properties["java"] as String)
    )
}