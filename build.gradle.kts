val mavenGroup: String by rootProject
val buildVersion: String by rootProject

plugins {
    idea
    kotlin("jvm") version "1.8.20" apply false
    id("com.github.johnrengelman.shadow") version("7.1.0")
    id("fabric-loom") version("1.2-SNAPSHOT") apply false
}

subprojects {
    apply(plugin = "kotlin")
    apply(plugin = "com.github.johnrengelman.shadow")
}

allprojects {
    repositories {
        mavenCentral()
    }
}

group = mavenGroup
version = buildVersion
