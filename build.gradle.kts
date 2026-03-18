buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("org.flywaydb:flyway-database-postgresql:11.10.0")
    }
}

val installGitHooks = tasks.register<Copy>("installGitHooks") {
    from(layout.projectDirectory.dir("githooks/pre-commit"))
    into(layout.projectDirectory.dir(".git/hooks"))
}
tasks.withType<Test>().configureEach { dependsOn(installGitHooks) }
tasks.withType<JavaCompile>().configureEach { dependsOn(installGitHooks) }
tasks.withType<ProcessResources>().configureEach { dependsOn(installGitHooks) }
tasks.named("prepareKotlinBuildScriptModel") { dependsOn(installGitHooks) }

plugins {
    java
    id("org.springframework.boot") version "3.4.7"
    id("io.spring.dependency-management") version "1.1.7"
    id("com.diffplug.spotless") version "6.25.0"
    id("io.freefair.lombok") version "8.11"
    id("org.flywaydb.flyway") version "11.10.0"
}

group = "jp.ne.yonem"
version = "1.0.0"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // Web & Server
    implementation("org.springframework.boot:spring-boot-starter-web")

    // Security & Logic
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-aop")

    // LLM
    implementation("dev.langchain4j:langchain4j-google-ai-gemini:1.10.0")
    implementation("dev.langchain4j:langchain4j-ollama:1.10.0")
    implementation("dev.langchain4j:langchain4j:1.10.0")
    implementation("dev.langchain4j:langchain4j-embeddings-all-minilm-l6-v2-q:1.10.0-beta18")
    implementation("dev.langchain4j:langchain4j-document-parser-apache-pdfbox:1.10.0-beta18")

    // JWT
    implementation("io.jsonwebtoken:jjwt-api:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.6")

    // DB & Mybatis
    implementation("org.mybatis.spring.boot:mybatis-spring-boot-starter:3.0.4")
    implementation("org.postgresql:postgresql:42.7.7")

    // Utilities
    implementation("net.logstash.logback:logstash-logback-encoder:9.0")
    implementation("net.lingala.zip4j:zip4j:2.11.5")
    implementation("org.apache.commons:commons-csv:1.9.0")
    implementation("com.opencsv:opencsv:5.12.0")
    implementation("com.google.zxing:javase:3.5.3")
    implementation("org.jboss.aerogear:aerogear-otp-java:1.0.0")
    implementation("commons-codec:commons-codec:1.16.1")

    // Messaging & Others
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-mail")
    implementation("org.springframework.kafka:spring-kafka")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.8")

    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.mybatis.spring.boot:mybatis-spring-boot-starter-test:3.0.4")
    testRuntimeOnly("com.h2database:h2")
    testImplementation("com.tngtech.archunit:archunit-junit5:1.3.0")
}

flyway {
    url = "jdbc:postgresql://localhost:5432/your_dev_db"
    user = "your_user"
    password = "your_password"
    cleanDisabled = false
}

spotless {
    java {
        target("src/main/java/**/*.java", "src/test/java/**/*.java")
        googleJavaFormat("1.27.0")
        importOrder()
        removeUnusedImports()
        endWithNewline()
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}