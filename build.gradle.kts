plugins {
    alias(libs.plugins.java.library)
    alias(libs.plugins.maven.publish)
}

group = "fish.cichlidmc"
version = "2.0.0"

repositories {
    mavenCentral()
}

dependencies {
    compileOnlyApi(libs.jetbrains.annotations)
    testImplementation(libs.bundles.junit)
}

java {
    withSourcesJar()
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

tasks.test {
    useJUnitPlatform()
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
