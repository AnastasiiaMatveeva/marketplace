plugins {
    id("build-jvm")
    application
}

application {
    mainClass.set("ru.otus.otuskotlin.aiassistant.app.tmp.MainKt")
}

dependencies {
    implementation(project(":ok-aiassistant-api-log1"))
    implementation("ru.otus.otuskotlin.aiassistant.libs:ok-aiassistant-lib-logging-common")
    implementation("ru.otus.otuskotlin.aiassistant.libs:ok-aiassistant-lib-logging-logback")

    implementation(project(":ok-aiassistant-common"))

    implementation(libs.coroutines.core)
}
