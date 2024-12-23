plugins {
    id("build-jvm")
    application
    alias(libs.plugins.shadowJar)
    alias(libs.plugins.muschko.java)
}

application {
    mainClass.set("ru.otus.otuskotlin.aiassistant.app.rabbit.ApplicationKt")
}

dependencies {

    implementation(kotlin("stdlib"))

    implementation(libs.rabbitmq.client)
    implementation(libs.jackson.databind)
    implementation(libs.logback)
    implementation(libs.coroutines.core)

    implementation(project(":ok-aiassistant-common"))
    implementation(project(":ok-aiassistant-app-common"))
    implementation("ru.otus.otuskotlin.aiassistant.libs:ok-aiassistant-lib-logging-logback")

    // v1 api
    implementation(project(":ok-aiassistant-api-v1-jackson"))
    implementation(project(":ok-aiassistant-api-v1-mappers"))

    // v2 api
    implementation(project(":ok-aiassistant-api-v2-kmp"))

    implementation(project(":ok-aiassistant-biz"))
    implementation(project(":ok-aiassistant-stubs"))

    testImplementation(libs.testcontainers.rabbitmq)
    testImplementation(kotlin("test"))
}
