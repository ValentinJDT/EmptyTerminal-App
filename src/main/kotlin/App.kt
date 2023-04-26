package fr.valentin.emptyterminal

import fr.valentin.api.event.EventRegister
import fr.valentin.api.event.EventRegisterLegacy
import fr.valentin.api.plugin.Command
import fr.valentin.api.plugin.Plugin
import fr.valentin.emptyterminal.listener.PluginListener
import fr.valentin.emptyterminal.plugin.PluginLoader

class App {


    companion object {
        private const val DEFAULT_CMD_ERROR_MESSAGE: String = "Error: Command \"%command%\" not found"

        /**
         * Change this value to set a new command not found message.
         * The command name is replaced by %command%.
         */
        var commandUnknownMessage = DEFAULT_CMD_ERROR_MESSAGE

        /**
         * Set the default error message.
         */
        fun resetCommandMessage() {
            commandUnknownMessage = DEFAULT_CMD_ERROR_MESSAGE
        }

        /**
         * Current working dir of the terminal. Change this value to set a new working directory path.
         */
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
        EventRegister.registerListener(PluginListener())
    }

    /**
     * Convert line into an array.
     *
     * @param line Input line
     * @return Return list of string
     */
    private fun splitArgs(line: String): List<String> {
        val words = mutableListOf<String>()
        var wordBegin = 0
        var isBetweenQuotes = false

        for((index, char) in line.withIndex()) {
            if(char == '\"') {
                isBetweenQuotes = !isBetweenQuotes
            } else if(!isBetweenQuotes && char == ' ') {
                words.add(removeArroundQuotes(line.substring(wordBegin, index)))
                wordBegin = index + 1
            }
        }

        words.add(removeArroundQuotes(line.substring(wordBegin)))

        return words
    }

    /**
     * Remove quotes arround [line].
     */
    private fun removeArroundQuotes(line: String): String {
        return if(line.startsWith("\"") && line.endsWith("\"")) {
            line.substring(1, line.length - 1)
        } else {
            line
        }
    }
}