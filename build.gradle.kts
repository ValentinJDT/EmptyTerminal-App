import org.gradle.configurationcache.extensions.capitalized
import org.gradle.kotlin.dsl.support.uppercaseFirstChar

plugins {
    kotlin("jvm") version "2.1.10"
    application
}

group = "fr.valentinjdt.emptyterminal"
version = property("version") as String

val vallibVersion = property("vallib-version") as String

val mainGroup = group

repositories {
    mavenCentral()
    maven {
        url = uri("https://jitpack.io")
    }
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("com.github.ValentinJDT:ValLib:${vallibVersion}")
    implementation("org.jline:jline:3.30.5")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}

application {
    mainClass.set("${mainGroup}.MainKt")
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "${mainGroup}.MainKt"
    }

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    from(sourceSets.main.get().output)


    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })
}

tasks.register<GenerateClassTask>("generateClass")

tasks.named("compileKotlin") {
    dependsOn("generateClass")
}

sourceSets {
    main {
        java {
            srcDir("build/generated/sources/kotlin/main")
        }
    }
}

subprojects {
    apply(plugin = "kotlin")
    apply(plugin = "application")

    repositories {
        mavenLocal()
        mavenCentral()
        maven {
            url = uri("https://jitpack.io")
        }
    }

    dependencies {
        implementation("com.github.ValentinJDT:ValLib:${vallibVersion}")
    }

    kotlin {
        jvmToolchain(17)
    }

    if(this.name == "commands" || this.name == "plugins") return@subprojects

    this@subprojects.version = property("${this@subprojects.name}-version") as String

    val subPackage = "fr.valentinjdt.plugin.${this@subprojects.name.replace("-", "")}"
    val subMainClass = "${subPackage}.MainKt"

    application {
        mainClass.set(subMainClass)
    }

    tasks.withType<Jar> {
        manifest {
            attributes["Main-Class"] = subMainClass
        }

        archiveBaseName = "${project.name.snakeToUpperCamelCase()}${project.projectDir.parentFile.name.uppercaseFirstChar().dropLast(1)}"

        duplicatesStrategy = DuplicatesStrategy.EXCLUDE

        from(sourceSets.main.get().output)
        dependsOn(configurations.runtimeClasspath)

        val list = mutableListOf(
            "kotlin-stdlib-2.1.10.jar", "plugin-${vallibVersion}.jar",

            // Updater plugin
            "kotlinx-coroutines-core-jvm-1.10.2.jar", "gson", "retrofit", "okhttp", "okio-jvm",
        )


        from({
            configurations.runtimeClasspath.get().filter { jar ->
                list.any { jar.name.contains(it) }
            }.map { zipTree(it) }
        })
    }

    tasks.register<GenerateClassTask>("generateClass")

    tasks.named("compileKotlin") {
        dependsOn("generateClass")
    }

    sourceSets {
        main {
            java {
                srcDir("build/generated/sources/kotlin/main")
            }
        }
    }
}

open class GenerateClassTask : DefaultTask() {

    @TaskAction
    fun generate() {
        val outputDir = project.layout.buildDirectory.asFile.get().resolve("generated/sources/kotlin/main")
        val outputFile = File(outputDir, "GeneratedProperties.kt")

        outputDir.mkdirs()
        outputFile.writeText("package fr.valentinjdt.components.${project.name.lowercase().replace("-", "")}\n\n")
        outputFile.addConstant("version", project.version.toString(), "Version of the component.")
    }

    inline fun <reified T : Comparable<*>> File.addConstant(name: String, value: T, description: String? = null) {
        if(description != null && description.isNotBlank()) {
            appendText("/** $description */\n")
        }

        if(value is Number) {
            appendText("const val ${name.uppercase()} = $value\n\n")
        } else {
            appendText("const val ${name.uppercase()} = \"$value\"\n\n")
        }
    }
}

fun String.camelToSnakeCase(): String {
    return "(?<=[a-zA-Z])[A-Z]".toRegex().replace(this) {
        "-${it.value}"
    }.lowercase()
}

fun String.snakeToUpperCamelCase(): String {
    return "-[a-zA-Z]".toRegex().replace(this) {
        it.value.replace("-","")
            .uppercase()
    }.capitalized()
}