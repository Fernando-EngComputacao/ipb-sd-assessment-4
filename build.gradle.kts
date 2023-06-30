plugins {
    java
    id("org.springframework.boot") version "2.7.12"
    id("io.spring.dependency-management") version "1.0.15.RELEASE"
}

group = "pt.ipb.dsys.assessmet"
version = "assessment-4"

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-activemq")
    implementation("org.springframework:spring-web")
    implementation("ch.qos.logback:logback-classic")
    implementation("com.google.guava:guava:31.0.1-jre")
}
