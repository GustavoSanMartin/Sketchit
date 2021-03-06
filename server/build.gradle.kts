plugins {
    kotlin("jvm") version "1.4.32"
    kotlin("plugin.serialization") version "1.4.32"
}

group = "ca.gustavo.sketchit_server"
version = "1.0-SNAPSHOT"

repositories {
    jcenter()
    mavenCentral()
}

val ktor_version: String by project
val logback_version: String by project

dependencies {
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("io.ktor:ktor-websockets:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.1.0")
}