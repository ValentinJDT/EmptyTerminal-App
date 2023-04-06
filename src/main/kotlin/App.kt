package fr.valentin.emptyterminal

import fr.valentin.api.event.EventRegister
import fr.valentin.api.plugin.Command
import fr.valentin.api.plugin.Plugin
import fr.valentin.emptyterminal.listener.PluginListener
import fr.valentin.emptyterminal.plugin.PluginLoader

class App {

    private val eventRegister: EventRegister = EventRegister.getInstance()

    companion object {
        private const val DEFAULT_CMD_ERROR_MESSAGE: String = "Error: Command \"%command%\" not found"

        var commandUnknownMessage = DEFAULT_CMD_ERROR_MESSAGE

        fun resetCommandMessage() {
            commandUnknownMessage = DEFAULT_CMD_ERROR_MESSAGE
        }

        lateinit var workingDir: String;
    }

    init {
        registerListener()
        PluginLoader.createInstance<Command>()
        PluginLoader.createInstance<Plugin>()

        Runtime.getRuntime().addShutdownHook(Thread {
            PluginLoader.unloadAll()
        })

        var stringInput: String

        do {
            stringInput = try {
                readln()
            } catch(exception: Exception) {
                "exit"
            }

            if(stringInput == "exit" || stringInput == "") continue

            val commandElements = splitArgs(stringInput)

            if(!PluginLoader.executeCommand(commandElements[0], commandElements.drop(1)))
                println(commandUnknownMessage.replace("%command%", commandElements[0]))

        } while(stringInput != "exit")
    }

    private fun registerListener() {
        eventRegister.registerListener(PluginListener())
    }

    private fun splitArgs(line: String): List<String> {
        val words = mutableListOf<String>()
        var wordBegin = 0
        var isBetweenQuotes = false

        for((index, char) in line.withIndex()) {
            if(char == '\"') {
                isBetweenQuotes = !isBetweenQuotes
            } else if(!isBetweenQuotes && char == ' ') {
                words.add(replaceQuotes(line.substring(wordBegin, index)))
                wordBegin = index + 1
            }
        }

        words.add(replaceQuotes(line.substring(wordBegin)))

        return words
    }

    private fun replaceQuotes(line: String): String {
        return if(line.startsWith("\"") && line.endsWith("\"")) {
            line.substring(1, line.length - 1)
        } else {
            line
        }
    }
}