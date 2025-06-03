plugins {
    java
    `maven-publish`
}

group = "dev.kobura"
version = "1.0.2-SNAPSHOT"

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
    withSourcesJar()
    withJavadocJar()
}

repositories {
    mavenCentral()
}

dependencies {
    // Runtime dependencies
    implementation("com.google.code.gson:gson:2.13.1")
    implementation("io.github.classgraph:classgraph:4.8.162")

    // Lombok
    compileOnly("org.projectlombok:lombok:1.18.38")
    annotationProcessor("org.projectlombok:lombok:1.18.38")

    // Tests
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.1")
}

tasks.withType<JavaCompile>().configureEach {
    options.compilerArgs.add("-parameters")
}

// JUnit Platform
tasks.test {
    useJUnitPlatform()
}

val testJar by tasks.registering(Jar::class) {
    archiveClassifier.set("tests")
    from(sourceSets.test.get().output)
}

artifacts {
    add("archives", testJar)
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])

            artifact(testJar.get()) {
                classifier = "tests"
            }

            groupId = "dev.kobura"
            artifactId = "evascript"
            version = "1.0.2-SNAPSHOT"
        }
    }
    repositories {
        mavenLocal()
    }
}
