val javaParserVersion = "3.15.22"
val velocityVersion = "1.7"
val lombokVersion = "1.18.12"
val log4jVersion = "2.13.3"
val persistenceApiVersion = "2.2"

plugins {
    `java-library`
    id("java")
}

group = "org.pensatocode.simplicity.generator"
version = "0.1.0"
description = "Automatic generator of Java classes for the Simplicity Java framework"
java.sourceCompatibility = JavaVersion.VERSION_11

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
    jcenter()
    flatDir {
        dirs("libs")
    }
}

dependencies {

    // Java Parser
    implementation( "com.github.javaparser:javaparser-symbol-solver-core:${javaParserVersion}")

    // Apache Velocity
    implementation("org.apache.velocity:velocity:${velocityVersion}")

    // Lombok
    compileOnly("org.projectlombok:lombok:${lombokVersion}")
    annotationProcessor("org.projectlombok:lombok:${lombokVersion}")

    // Persistence API
    implementation("javax.persistence:javax.persistence-api:${persistenceApiVersion}")

    // Log4j2
    implementation("org.apache.logging.log4j:log4j-api:${log4jVersion}")
    implementation("org.apache.logging.log4j:log4j-core:${log4jVersion}")
    implementation("org.apache.logging.log4j:log4j-slf4j-impl:${log4jVersion}")
}

tasks.register("simplicityGenerator", JavaExec::class) {
    dependsOn(JavaPlugin.COMPILE_JAVA_TASK_NAME)
    doFirst {
        classpath = files("$buildDir/classes/java/main", "$buildDir/resources/main", "$buildDir/libs")
        classpath += sourceSets["main"].runtimeClasspath
    }
    group = "simplicity"
    description = "Generate all classes for Simplicity entities"
    main = "org.pensatocode.simplicity.generator.Application"
}


