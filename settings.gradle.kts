
rootProject.name = "EmptyTerminal"

File(rootDir, "commands").listFiles().filter { it.isDirectory && it.name != "build" }.forEach { module ->
    include(":commands:${module.name}")
}

File(rootDir, "plugins").listFiles().filter { it.isDirectory && it.name != "build" }.forEach { module ->
    include(":plugins:${module.name}")
}