group = "io.hexlet"
version = "0.0.1-SNAPSHOT"
description = "Hexlet Typo Reporter"
java.sourceCompatibility = JavaVersion.VERSION_19

plugins {
    `java-library`
    `maven-publish`
    alias(libs.plugins.org.spring.gradle.dependency.management)
    alias(libs.plugins.org.springframework.boot)
    alias(libs.plugins.versions)
}

repositories {
    mavenLocal()
    maven {
        url = uri("https://repo.maven.apache.org/maven2/")
    }
}

dependencies {
    // Spring
    implementation(libs.org.springframework.boot.spring.boot.starter.web)
    implementation(libs.org.springframework.boot.spring.boot.starter.data.jpa)
    implementation(libs.org.springframework.boot.spring.boot.starter.security)
    implementation(libs.org.springframework.boot.spring.boot.starter.thymeleaf)
    implementation(libs.org.springframework.boot.spring.boot.starter.actuator)
    implementation(libs.org.springframework.boot.spring.boot.starter.validation)
    // Thymeleaf
    implementation(libs.org.thymeleaf.extras.thymeleaf.extras.springsecurity6)
    implementation(libs.io.github.jpenren.thymeleaf.spring.data.dialect)
    implementation(libs.org.webjars.webjars.locator)
    implementation(libs.org.webjars.bootstrap)
    // Database
    runtimeOnly(libs.org.postgresql.postgresql)
    runtimeOnly(libs.org.springframework.boot.spring.boot.devtools)
    implementation(libs.io.hypersistence.hypersistence.utils.hibernate.v60)
    implementation(libs.org.liquibase.liquibase.core)
    // Utils
    compileOnly(libs.org.projectlombok.lombok)
    compileOnly(libs.org.projectlombok.lombok.mapstruct.binding)
    implementation(libs.org.ocpsoft.prettytime.prettytime)
    implementation(libs.org.mapstruct.mapstruct)
    // Annotation processors
    annotationProcessor(libs.org.projectlombok.lombok)
    annotationProcessor(libs.org.mapstruct.mapstruct.processor)

    // Testing
    testImplementation(libs.org.springframework.boot.spring.boot.starter.test)
    testImplementation(libs.org.springframework.security.spring.security.test)
    testImplementation(platform(libs.org.testcontainers.bom))
    testImplementation(libs.org.testcontainers.junit.jupiter)
    testImplementation(libs.org.testcontainers.postgresql)
    testImplementation(libs.com.github.database.rider.rider.spring)
    testCompileOnly(libs.org.projectlombok.lombok)
    testCompileOnly(libs.org.projectlombok.lombok.mapstruct.binding)
    testRuntimeOnly(libs.org.junit.platform.launcher)
    testAnnotationProcessor(libs.org.projectlombok.lombok)
    testAnnotationProcessor(libs.org.mapstruct.mapstruct.processor)
}

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.compilerArgs.add("-Amapstruct.defaultComponentModel=spring")
}

tasks.withType<Javadoc> {
    options.encoding = "UTF-8"
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.create("unitTest", type = Test::class) {
    filter {
        includeTestsMatching("${project.group}.${rootProject.name}.domain.*")
        includeTestsMatching("${project.group}.${rootProject.name}.repository.*")
        includeTestsMatching("${project.group}.${rootProject.name}.service.*")
    }
}

tasks.create("integrationTest", type = Test::class) {
    filter {
        includeTestsMatching("${project.group}.${rootProject.name}.web.*")
    }
}
