package fr.valentin.emptyterminal

import java.io.File
import java.nio.file.InvalidPathException
import java.nio.file.Paths


fun main(args: Array<String>) {
    App.workingDir = if(args.size == 1 && isValidPath(args[0])) {
        File(args[0]).canonicalPath
    } else {
        System.getProperty("user.dir")
    }

    if(!System.getProperty("os.name").contains("Windows", true))
        println("Warning: This app is intended for Windows 10/11.")

    println("Current working directory: ${App.workingDir}")

    App()
}

fun isValidPath(path: String): Boolean {
    try {
        Paths.get(path)
    } catch(ex: InvalidPathException) {
        return false
    }

    return true
}