plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
}

group = "ru.otus.otuskotlin.aiassistant"
version = "0.0.1"

repositories {
    mavenCentral()
}

subprojects {
    repositories {
        mavenCentral()
    }
    group = rootProject.group
    version = rootProject.version
}

//tasks {
//    val buildImages: Task by creating {
//        dependsOn(gradle.includedBuild("ok-aiassistant-be").task(":buildImages"))
//    }
//
//    create("check") {
//        group = "verification"
////        dependsOn(gradle.includedBuild("ok-marketplace-be").task(":check"))
//        dependsOn(buildImages)
//    }
//}
