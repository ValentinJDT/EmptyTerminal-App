plugins {
    kotlin("jvm") version "1.8.20"
    application
}

group = "fr.valentin.emptyterminal"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation(files("dependencies/ValLib-0.1.2.jar"))
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