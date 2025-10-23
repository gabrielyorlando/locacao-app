val lombokVersion = "1.18.34"
val mapstructVersion = "1.5.5.Final"
val lombokBindingVersion = "0.2.0"

plugins {
    java
    id("org.springframework.boot") version "3.5.6"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "com.gabrielyorlando"
version = "1.0.0"
description = "locacao-app-backend"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
    maven { url = uri("https://repo.spring.io/milestone") }
    maven { url = uri("https://repo.spring.io/snapshot") }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")

    compileOnly("org.projectlombok:lombok:$lombokVersion")
    annotationProcessor("org.projectlombok:lombok:$lombokVersion")

    implementation("org.mapstruct:mapstruct:$mapstructVersion")
    annotationProcessor("org.mapstruct:mapstruct-processor:$mapstructVersion")

    annotationProcessor("org.projectlombok:lombok-mapstruct-binding:$lombokBindingVersion")

    runtimeOnly("org.mariadb.jdbc:mariadb-java-client:3.2.0")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // implementation("org.flywaydb:flyway-core")
    // implementation("org.flywaydb:flyway-mysql")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

