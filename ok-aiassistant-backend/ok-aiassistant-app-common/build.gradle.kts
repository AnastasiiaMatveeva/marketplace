plugins {
    id("build-kmp")
}

kotlin {
    sourceSets {
        val coroutinesVersion: String by project
        commonMain {
            dependencies {
                implementation(kotlin("stdlib-jdk8"))
                implementation(libs.coroutines.core)

                // transport models
                implementation(project(":ok-aiassistant-common"))
                implementation(project(":ok-aiassistant-api-log1"))

                implementation(project(":ok-aiassistant-biz"))
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))

                implementation(libs.coroutines.test)

                implementation(project(":ok-aiassistant-api-v2-kmp"))
            }
        }

        jvmTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        nativeTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}
