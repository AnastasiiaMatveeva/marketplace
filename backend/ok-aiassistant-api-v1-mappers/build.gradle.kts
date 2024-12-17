plugins {
    id("build-jvm")
}

group = rootProject.group
version = rootProject.version

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":ok-aiassistant-api-v1-jackson"))
    implementation(project(":ok-aiassistant-common"))

    testImplementation(kotlin("test-junit"))
}
