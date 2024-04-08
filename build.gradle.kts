plugins {
    id("org.jetbrains.kotlin.jvm") version "1.9.23"
    id("org.jetbrains.kotlin.plugin.spring") version "1.9.23"
    id("org.springframework.boot") version "3.2.4"
    id("com.google.cloud.tools.jib") version "3.4.2"
}

group = "de.solugo"

repositories {
    mavenCentral()
}

dependencies {
    implementation(platform("org.springframework.boot:spring-boot-dependencies:3.2.4"))
    implementation(platform("org.jetbrains.kotlinx:kotlinx-coroutines-bom:1.8.0"))

    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("org.bitbucket.b_c:jose4j:0.9.6")
}

kotlin {
    jvmToolchain(21)
}

jib {
    from {
        image = "openjdk:23-slim-bullseye"
    }
    container {
        mainClass = "OidcServer"
    }
    to {
        image = "oidc-server"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}