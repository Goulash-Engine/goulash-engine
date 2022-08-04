import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.0"
    id("org.jlleitschuh.gradle.ktlint") version "10.3.0"
    id("io.gitlab.arturbosch.detekt").version("1.21.0")
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("java")
    id("groovy")
    application
}

group = "com.barbarus"
version = "0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.codehaus.groovy:groovy-all:3.0.12")
    implementation("ch.qos.logback:logback-core:1.2.11")
    implementation("ch.qos.logback:logback-classic:1.2.11")
    implementation("org.slf4j:slf4j-api:1.7.36")
    implementation("com.github.h0tk3y.betterParse:better-parse:0.4.4")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.7.0")
    implementation("org.springframework.boot:spring-boot-starter-web:2.7.2")
    testImplementation("org.spockframework:spock-core:2.1-groovy-3.0")
    testImplementation("com.willowtreeapps.assertk:assertk:0.25")
    testImplementation(kotlin("test"))
    testImplementation("io.mockk:mockk:1.12.5")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

application {
    mainClass.set("com.barbarus.prosper.MainKt")
}
