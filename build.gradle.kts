plugins {
    kotlin("jvm") version "1.8.0"
    application
}

group = "fr.valentin.emptyterminal"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation(files("C:\\Users\\v.jeandot\\IdeaProjects\\EventAPI\\build\\libs\\EventAPI-1.0-SNAPSHOT.jar", "C:\\Users\\v.jeandot\\IdeaProjects\\PluginAPI\\API\\build\\libs\\API-1.0-SNAPSHOT.jar"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(8)
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

tasks.named<JavaExec>("run") {
    standardInput = System.`in`
}