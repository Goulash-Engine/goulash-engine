import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.0"
    id("org.jlleitschuh.gradle.ktlint") version "10.3.0"
    id("java")
    id("groovy")
    application
}

group = "com.barbarus"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.codehaus.groovy:groovy-all:3.0.11")
    testImplementation("org.spockframework:spock-core:2.1-groovy-3.0")
    testImplementation("com.willowtreeapps.assertk:assertk:0.25")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("MainKt")
}
