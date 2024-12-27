rootProject.name = "backend"

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

pluginManagement {
    includeBuild("../build-plugin")
    plugins {
        id("build-jvm") apply false
        id("build-kmp") apply false
    }
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(":ok-aiassistant-api-log1")
include(":ok-aiassistant-api-v1-jackson")
include(":ok-aiassistant-api-v1-mappers")
include(":ok-aiassistant-api-v2-kmp")
include(":ok-aiassistant-common")
include(":ok-aiassistant-app-common")
include(":ok-aiassistant-app-rabbit")
include(":ok-aiassistant-app-spring")
include(":ok-aiassistant-biz")
include(":ok-aiassistant-stubs")

include(":ok-aiassistant-repo-common")
include(":ok-aiassistant-repo-inmemory")
include(":ok-aiassistant-repo-stubs")
include(":ok-aiassistant-repo-tests")
include(":ok-aiassistant-repo-postgres")
