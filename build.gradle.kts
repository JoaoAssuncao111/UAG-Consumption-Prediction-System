import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("org.springframework.boot") version "2.7.4"
    id("io.spring.dependency-management") version "1.0.14.RELEASE"
    kotlin("jvm") version "1.8.0"
    kotlin("plugin.spring") version "1.6.21"
    application
}

group = "org.example"
version = "0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jdbi:jdbi3-kotlin:3.33.0")
    implementation("org.jdbi:jdbi3-postgres:3.33.0")
    implementation("org.json:json:20220320")
    implementation("org.postgresql:postgresql:42.5.4")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jdbi:jdbi3-core:3.37.1")
    implementation("org.springframework.security:spring-security-core:6.0.2")
    implementation("com.squareup.okhttp3:okhttp:4.9.3")
    implementation ("com.google.code.gson:gson:2.8.9")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("Application.kt")
}
tasks {
    withType<BootJar> {
        archiveFileName.set("uag-consumption-prediction-system.jar") // Replace "custom-jar-name.jar" with the desired JAR name
    }
}
sourceSets {
    main {
        java.srcDirs("code/jvm/src/main/kotlin") // Check this path
    }
}
