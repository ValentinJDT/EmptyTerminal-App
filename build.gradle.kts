plugins {
    kotlin("jvm") version "1.8.20"
    application
}

group = "fr.valentin.emptyterminal"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven {
        url = uri("https://jitpack.io")
    }
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("com.github.ValentinJDT:ValLib:v0.1.3")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}

application {
    mainClass.set("fr.valentin.emptyterminal.MainKt")
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "fr.valentin.emptyterminal.MainKt"
    }

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    from(sourceSets.main.get().output)


    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })
}