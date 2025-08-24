package fr.valentinjdt.plugin.changedir

import fr.valentinjdt.emptyterminal.App
import fr.valentinjdt.lib.plugin.Command
import fr.valentinjdt.lib.plugin.SubCommandCompletion
import java.io.File
import java.nio.file.InvalidPathException
import java.nio.file.Paths

class ChangeDirCommand: Command("cd", "Change working directory of the terminal", VERSION) {

    override val subCommandsCompletions: List<SubCommandCompletion>
        get() = listOf()

    override fun onEnable() {
        App.commandPromptPrefix = App.workingDir + "> "
    }

    override fun onDisable() {
        App.resetPromptPrefix()
    }

    override fun execute(args: List<String>) {

        if(args.isEmpty() || args[0].isBlank()) {
            println("Current working directory : " + App.workingDir)

        } else if(isValidPath(args[0])) {
            val directory = if(isAbsolutePath(args[0])) { File(args[0]) } else { File(App.workingDir, args[0]) }

            if(!directory.exists() || !directory.isDirectory) {
                println("This directory doesn't exist")
                return
            }

            App.workingDir = directory.canonicalPath
            println("Working directory updated : " + App.workingDir)
        } else {
            println("Invalid path")
        }
    }

    private fun isValidPath(path: String): Boolean {
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