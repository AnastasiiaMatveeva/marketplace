plugins {
    id("build-kmp")
}

kotlin {
    sourceSets {
        all { languageSettings.optIn("kotlin.RequiresOptIn") }

        commonMain {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation(libs.cor)
                implementation(project(":ok-aiassistant-common"))
                implementation(project(":ok-aiassistant-stubs"))
            }
        }
        commonTest {
            dependencies {
                implementation(project(":ok-aiassistant-repo-common"))
                implementation(project(":ok-aiassistant-repo-inmemory"))
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))

                api(libs.coroutines.test)
//                implementation(projects.okMarketplaceRepoTests)
//                implementation(projects.okMarketplaceRepoInmemory)
            }
        }
        jvmMain {
            dependencies {
                implementation(kotlin("stdlib-jdk8"))
            }
        }
        jvmTest {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }
    }
}
