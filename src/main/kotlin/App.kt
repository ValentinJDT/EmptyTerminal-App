package fr.valentinjdt.emptyterminal

import fr.valentinjdt.emptyterminal.listener.PluginListener
import fr.valentinjdt.lib.event.EventRegister
import fr.valentinjdt.lib.plugin.Command
import fr.valentinjdt.lib.plugin.Plugin
import fr.valentinjdt.lib.plugin.PluginLoader
import org.jline.reader.Candidate
import org.jline.reader.LineReader
import org.jline.reader.LineReaderBuilder
import org.jline.terminal.Terminal
import org.jline.terminal.TerminalBuilder

/**
 * Main application class for the terminal.
 * Handles command registration, input reading, and command execution.
 */
class App {

    companion object {
        private const val DEFAULT_CMD_ERROR_MESSAGE: String = "Error: Command \"%command%\" not found"
        private const val DEFAULT_CMD_PREFIX: String = "> "

        /**
         * Change this value to set a new command not found message.
         * The command name is replaced by %command%.
         */
        var commandUnknownMessage = DEFAULT_CMD_ERROR_MESSAGE

        /**
         * Change this value to set a new command prompt prefix.
         */
        var commandPromptPrefix = DEFAULT_CMD_PREFIX

        /**
         * Set the default error message.
         */
        fun resetCommandMessage() {
            commandUnknownMessage = DEFAULT_CMD_ERROR_MESSAGE
        }

        /**
         * Set the default command prompt prefix.
         */
        fun resetPromptPrefix() {
            commandPromptPrefix = DEFAULT_CMD_PREFIX
        }

        /**
         * Current working dir of the terminal. Change this value to set a new working directory path.
         */
        lateinit var workingDir: String;
    }

    init {
        // Register event listeners
        registerListener()

        // Initialize plugin loaders for commands and plugins
        PluginLoader.createInstance<Command>()
        PluginLoader.createInstance<Plugin>()

        // Add a shutdown hook to unload all plugins on application exit
        Runtime.getRuntime().addShutdownHook(Thread {
            PluginLoader.unloadAll()
        })

        // Retrieve all registered commands
        val commands = PluginLoader.getInstance<Command>().getPlugins()

        // Build the terminal
        val terminal: Terminal? = TerminalBuilder.builder()
            .system(true)
            .build()

        // Build the line reader with a custom completer
        val reader: LineReader = LineReaderBuilder.builder()
            .terminal(terminal)
            .setupCompleter(commands)
            .build()

        var stringInput: String

        // Main loop to read and execute commands
        do {
            stringInput = try {
                reader.readLine(commandPromptPrefix) // Read user input
            } catch(_: Exception) {
                "exit" // Default to "exit" on error
            }.trim()

            // Skip empty input or "exit" command
            if(stringInput == "exit" || stringInput == "") continue

            // Split the input into command and arguments
            val commandElements = splitArgs(stringInput)

            // Execute the command and display an error message if not found
            if(!PluginLoader.executeCommand(commandElements[0], commandElements.drop(1)))
                println(commandUnknownMessage.replace("%command%", commandElements[0]))

            println()

        } while(stringInput != "exit") // Exit the loop when "exit" is entered
        reader.terminal.close()
    }

    /**
     * Registers the plugin listener for handling events.
     */
    private fun registerListener() {
        EventRegister.registerListener(PluginListener())
    }

    /**
     * Splits a line of input into an array of arguments.
     * Handles quoted strings as single arguments.
     *
     * @param line The input line to split.
     * @return A list of arguments extracted from the input line.
     */
    private fun splitArgs(line: String): List<String> {
        val words = mutableListOf<String>()
        var wordBegin = 0
        var isBetweenQuotes = false

        for((index, char) in line.withIndex()) {
            if(char == '\"') {
                isBetweenQuotes = !isBetweenQuotes
            } else if(!isBetweenQuotes && char == ' ') {
                words.add(removeAroundQuotes(line.substring(wordBegin, index)))
                wordBegin = index + 1
            }
        }

        words.add(removeAroundQuotes(line.substring(wordBegin)))

        return words
    }

    /**
     * Removes surrounding quotes from a string, if present.
     *
     * @param line The string to process.
     * @return The string without surrounding quotes.
     */
    private fun removeAroundQuotes(line: String): String {
        return if(line.startsWith("\"") && line.endsWith("\"")) {
            line.substring(1, line.length - 1)
        } else {
            line
        }
    }

    /**
     * Sets up the command completer for the line reader.
     * Provides suggestions for commands and subcommands based on user input.
     *
     * @param commands The set of available commands.
     * @return The configured LineReaderBuilder instance.
     */
    private fun LineReaderBuilder.setupCompleter(commands: MutableSet<Command>): LineReaderBuilder =
        completer { _, line, candidates ->
            val args = splitArgs(line.line())
            if(args.isEmpty() || args[0].isEmpty()) {
                candidates.add(Candidate("exit"))
                commands.forEach { candidates.add(Candidate(it.name)) }
                return@completer
            }

            if(line.wordIndex() == 0) {
                candidates.add(Candidate("exit"))
                commands.filter { it.name.startsWith(args[0], true) }
                    .forEach { candidates.add(Candidate(it.name)) }
            } else {
                val command = commands.firstOrNull { it.name.equals(args[0], true) } ?: return@completer
                var subCommands = command.subCommandsCompletions

                for(i in 1 until line.wordIndex()) {
                    val arg = args.getOrNull(i)
                    val subCommand = subCommands.firstOrNull {
                        it.command?.equals(arg, true) == true
                    } ?: return@completer
                    subCommands = subCommand.subCommands ?: return@completer
                }

                val currentArg = args.getOrNull(line.wordIndex())
                if(currentArg.isNullOrEmpty()) {
                    subCommands.forEach {
                        if(!it.command.isNullOrEmpty()) candidates.add(Candidate(it.command))
                    }
                } else {
                    subCommands.filter {
                        it.command?.startsWith(currentArg, true) ?: false
                    }.forEach { candidates.add(Candidate(it.command)) }
                }
            }
        }
}