plugins {
    alias(libs.plugins.java.library)
    alias(libs.plugins.maven.publish)
}

group = "fish.cichlidmc"
version = "3.0.0"

repositories {
    mavenCentral()
}

dependencies {
    compileOnlyApi(libs.jspecify)
    testImplementation(libs.bundles.junit)
}

java {
    withSourcesJar()
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

tasks.compileJava {
    // sync module version so it can be read at runtime
    options.javaModuleVersion = provider { version as String }
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
