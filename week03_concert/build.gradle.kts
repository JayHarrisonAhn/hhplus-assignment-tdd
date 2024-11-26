plugins {
	java
	id("org.springframework.boot") version "3.3.4"
	id("io.spring.dependency-management") version "1.1.6"
}

group = "com.example"
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
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.kafka:spring-kafka")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")
	implementation("org.redisson:redisson-spring-boot-starter:3.37.0")
	implementation ("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
	implementation ("com.fasterxml.jackson.core:jackson-databind")

	compileOnly("org.projectlombok:lombok")
	runtimeOnly("org.mariadb.jdbc:mariadb-java-client")
	annotationProcessor("org.projectlombok:lombok")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.mockito:mockito-core:3.+")

	testImplementation("org.testcontainers:junit-jupiter:1.20.2")
	testImplementation("org.testcontainers:mariadb:1.20.2")
	testImplementation("io.rest-assured:rest-assured:5.5.0")
	testImplementation("org.testcontainers:kafka:1.20.3")
	testImplementation("org.springframework.kafka:spring-kafka-test:3.+")

	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	testRuntimeOnly("org.mariadb.jdbc:mariadb-java-client")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
