plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.5.3"
    id("io.spring.dependency-management") version "1.1.7"
    id("checkstyle")
}

checkstyle {
    isIgnoreFailures = true
    val checkstyleTasks = tasks.withType<Checkstyle>()
    checkstyleTasks.configureEach {
        reports {
            xml.required.set(false)
            html.required.set(true)
        }
    }
}

group = "org"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    testImplementation("org.springframework.boot:spring-boot-starter-jdbc")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("org.springframework.boot:spring-boot-starter-web:3.5.3")
    implementation(project(":link-parser"))
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.9")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")
    implementation("org.liquibase:liquibase-core:4.32.0")
    implementation("org.postgresql:postgresql:42.7.6")
    testImplementation("org.testcontainers:testcontainers:1.21.3")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation ("org.reflections:reflections:0.10.2")
    implementation("org.springframework.boot:spring-boot-starter-amqp:3.5.0")
    implementation("org.apache.maven.plugins:maven-checkstyle-plugin:3.6.0")
    implementation("org.springframework.boot:spring-boot-starter-actuator:3.5.3")
    implementation("io.micrometer:micrometer-registry-prometheus:1.15.1")
    implementation("io.micrometer:micrometer-tracing-bridge-brave:1.5.1")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
