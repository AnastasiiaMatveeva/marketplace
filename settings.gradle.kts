pluginManagement {
    val kotlinVersion: String by settings
    plugins {
        kotlin("jvm") version kotlinVersion
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "aiassistant"

includeBuild("lessons")
includeBuild("ok-aiassistant-backend")
includeBuild("ok-aiassistant-libs")
//include("ok-aiassistant-backend:ok-aiassistant-biz:src:commonTest:kotlin")
//findProject(":ok-aiassistant-backend:ok-aiassistant-biz:src:commonTest:kotlin")?.name = "kotlin"
