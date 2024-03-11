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
    maven {
        url = uri("https://repo.spring.io/plugins-snapshot")
    }
}

dependencies {
    api(libs.org.springframework.boot.spring.boot.starter.web)
    api(libs.org.springframework.boot.spring.boot.starter.security)
    api(libs.org.springframework.boot.spring.boot.starter.thymeleaf)
    api(libs.org.webjars.webjars.locator)
    api(libs.org.webjars.bootstrap)
    api(libs.org.springframework.boot.spring.boot.starter.data.jpa)
    api(libs.io.hypersistence.hypersistence.utils.hibernate.v60)
    api(libs.org.liquibase.liquibase.core)
    api(libs.org.springframework.boot.spring.boot.starter.validation)
    api(libs.org.ocpsoft.prettytime.prettytime)
    api(libs.io.github.jpenren.thymeleaf.spring.data.dialect)
    api(libs.org.thymeleaf.extras.thymeleaf.extras.springsecurity6)
    api(libs.org.springframework.boot.spring.boot.starter.actuator)
    implementation(libs.org.mapstruct.mapstruct)

    annotationProcessor(libs.org.projectlombok.lombok)
    annotationProcessor(libs.org.mapstruct.mapstruct.processor)
    compileOnly(libs.org.projectlombok.lombok)
    compileOnly(libs.org.projectlombok.lombok.mapstruct.binding)

    runtimeOnly(libs.org.postgresql.postgresql)
    runtimeOnly(libs.org.springframework.boot.spring.boot.devtools)

    testAnnotationProcessor(libs.org.projectlombok.lombok)
    testAnnotationProcessor(libs.org.mapstruct.mapstruct.processor)
    testCompileOnly(libs.org.projectlombok.lombok)
    testCompileOnly(libs.org.projectlombok.lombok.mapstruct.binding)
    testImplementation(libs.org.springframework.boot.spring.boot.starter.test)
    testImplementation(libs.org.springframework.security.spring.security.test)
    testRuntimeOnly(libs.org.junit.platform.launcher)
    testImplementation(libs.org.testcontainers.junit.jupiter)
    testImplementation(libs.org.testcontainers.postgresql)
    testImplementation(libs.com.github.database.rider.rider.spring)
}

group = "io.hexlet"
version = "0.0.1-SNAPSHOT"
description = "Hexlet Typo Reporter"
java.sourceCompatibility = JavaVersion.VERSION_19

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
        includeTestsMatching("*.${rootProject.name}.domain.*")
        includeTestsMatching("*.${rootProject.name}.repository.*")
        includeTestsMatching("*.${rootProject.name}.service.*")
    }
}

tasks.create("integrationTest", type = Test::class) {
    filter {
        includeTestsMatching("*.${rootProject.name}.web.*")
    }
}
