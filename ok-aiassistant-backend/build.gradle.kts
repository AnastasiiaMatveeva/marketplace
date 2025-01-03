plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.muschko.remote) apply false
    alias(libs.plugins.muschko.java) apply false
}

group = "ru.otus.otuskotlin.aiassistant"
version = "0.0.1"

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    group = rootProject.group
    version = rootProject.version
}

ext {
    val specDir = layout.projectDirectory.dir("../specs")
    set("spec-v1", specDir.file("specs-v1.yaml").toString())
    set("spec-v2", specDir.file("specs-v2.yaml").toString())
    set("spec-log1", specDir.file("specs-log1.yaml").toString())

}

tasks {
    arrayOf("build", "clean", "check").forEach {tsk ->
        create(tsk) {
            group = "build"
            dependsOn(subprojects.map {  it.getTasksByName(tsk,false)})
        }
    }
    create("buildImages") {
        dependsOn(project("ok-aiassistant-app-spring").tasks.getByName("bootBuildImage"))
    }
}