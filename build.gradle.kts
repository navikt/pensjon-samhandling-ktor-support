import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "no.nav.pensjonsamhandling"

plugins {
    kotlin("jvm") version "1.4.0"
    kotlin("plugin.serialization") version "1.4.0"
    id("se.patrikerdes.use-latest-versions") version "0.2.14"
    id("net.researchgate.release") version "2.8.1"
    `maven-publish`

}

repositories {
    jcenter()
    mavenCentral()
    maven("https://packages.confluent.io/maven/")
}

dependencies {

}

release {
    newVersionCommitMessage = "[Release Plugin] - next version commit: "
    tagTemplate = "release-\${version}"
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/navikt/${rootProject.name}")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_14
    targetCompatibility = JavaVersion.VERSION_14
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "14"
}