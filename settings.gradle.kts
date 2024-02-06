plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}
/*
 * Copyright (c) 2024 Ashley (uwuvern) <uwuvern@outlook.com>
 *
 * This project is licensed under the MIT license, check the root of the project for
 * more information.
 */

rootProject.name = "binding"
include("common")
include("action")
include("core")
include("bindables")
include("kotlin")
