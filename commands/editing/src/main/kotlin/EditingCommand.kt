package fr.valentinjdt.plugin.editing

import fr.valentinjdt.components.editing.VERSION
import fr.valentinjdt.emptyterminal.App
import fr.valentinjdt.lib.plugin.Command
import fr.valentinjdt.lib.plugin.SubCommandCompletion
import java.io.File
import java.nio.file.Files
import java.nio.file.InvalidPathException
import java.nio.file.Paths

class EditingCommand: Command("edfi", "File manipulator", VERSION) {

    override val subCommandsCompletions: List<SubCommandCompletion>
        get() = listOf(
            SubCommandCompletion("move", null),
            SubCommandCompletion("rename", null),
            SubCommandCompletion("dir", null)
        )

    override fun execute(args: List<String>) {
        if(args.isEmpty()) {
            return
        }

        when(args[0]) {
            "move" -> {
                if(args.size == 3) {
                    if(!isValidPath(args[1])) {
                        println("Invalid file path")
                        return
                    }

                    if(!isValidPath(args[2])) {
                        println("Invalid destination path")
                        return
                    }

                    val file = if(isAbsolutePath(args[1])) { File(args[1]) } else { File(App.workingDir, args[1]) }

                    if(!file.exists()) {
                        println("File doesn't exist !")
                        return
                    }

                    val compl = if(file.isDirectory) {
                        args[2]
                    } else { args[2] + "/" + file.name }

                    val destination = if(isAbsolutePath(args[2])) { File(compl) } else { File(App.workingDir, compl) }

                    Files.move(file.toPath(), destination.toPath())

                    file.delete()
                }
            }
            "rename" -> {
                if(args.size != 3) {
                    println("Argument not found")
                    return
                }

                if(!isValidPath(args[1])) {
                    println("Invalid path")
                    return
                }

                val file = if(isAbsolutePath(args[1])) { File(args[1]) } else { File(App.workingDir, args[1]) }

                if(!file.exists()) {
                    println("File doesn't exist !")
                    return
                }

                if(file.renameTo(File(file.absolutePath.replace(file.name, args[2])))) {
                    println("File is renamed")
                } else {
                    println("File can't be renamed")
                }
            }
            "dir" -> println(App.workingDir)
            else -> println("Argument not found")
        }

    }

    fun isValidPath(path: String): Boolean {
        try {
            Paths.get(path)
        } catch(_: InvalidPathException) {
            return false
        }
        return true
    }

    private fun isAbsolutePath(path: String): Boolean {
        val regex = Regex(pattern = "^[a-zA-Z]:[/|\\\\]")
        return regex.find(path)?.run { true } ?: false
    }
}