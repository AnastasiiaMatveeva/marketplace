pluginManagement {
    plugins {
        val kotlinVersion: String by settings
        val kotestVersion: String by settings
        kotlin("jvm") version kotlinVersion
        id("io.kotest.multiplatform") version kotestVersion
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}
rootProject.name = "lessons"

include("m1l1-first")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
