plugins {
    id ("java")
    id ("org.springframework.boot") version "3.2.4"
    id ("io.spring.dependency-management") version "1.1.4"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val assertjVersion = "3.24.2"
val postgresqlVersion = "42.7.1"
val javaxVersion = "2.0.1.Final"
val jakartaVersion = "4.0.2"
val flywayVersion = "10.6.0"

dependencies {
    testImplementation (enforcedPlatform("org.junit:junit-bom:5.9.1"))
    //testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation (enforcedPlatform("org.mockito:mockito-bom:5.7.0"))

    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.mockito:mockito-core")
    testImplementation("org.mockito:mockito-junit-jupiter")
    testImplementation("org.assertj:assertj-core:$assertjVersion")
    testImplementation("org.springframework:spring-test")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-autoconfigure")
    implementation("org.springframework.boot:spring-boot-devtools")
    implementation("org.springframework.boot:spring-boot-starter-mail")

    implementation("org.thymeleaf.extras:thymeleaf-extras-springsecurity6")
    implementation("org.postgresql:postgresql:$postgresqlVersion")

    implementation("javax.validation:validation-api:$javaxVersion")
    compileOnly("jakarta.servlet:jakarta.servlet-api:$jakartaVersion")

    implementation("com.stripe:stripe-java:4.2.0")
}

tasks.test {
    useJUnitPlatform()
}
