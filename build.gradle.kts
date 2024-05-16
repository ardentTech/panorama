plugins {
    kotlin("jvm") version "1.9.23"
    kotlin("plugin.serialization") version "1.9.23"
}

group = "tech.ardent"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
    implementation("com.squareup:kotlinpoet:1.16.0")
    implementation("org.slf4j:slf4j-simple:2.0.3")
    implementation("io.github.oshai:kotlin-logging-jvm:5.1.0")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(8)
}