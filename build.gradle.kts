plugins {
    id("java-library")
    id("maven-publish")
}

group = "fish.cichlidmc"
version = "2.0.0"

repositories {
    mavenCentral()
}

dependencies {
    compileOnlyApi("org.jetbrains:annotations:24.1.0")
}

java {
    withSourcesJar()
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }

    repositories {
        listOf("Releases", "Snapshots").forEach {
            maven("https://mvn.devos.one/${it.lowercase()}") {
                name = "devOs$it"
                credentials(PasswordCredentials::class)
            }
        }
    }
}
