package fr.valentinjdt.plugin.globalhelp

import fr.valentinjdt.emptyterminal.App
import fr.valentinjdt.lib.plugin.Command
import fr.valentinjdt.lib.plugin.PluginLoader
import fr.valentinjdt.lib.plugin.SubCommandCompletion

class GlobalHelpCommand: Command("help", "Display all commands", VERSION) {

    override val subCommandsCompletions: List<SubCommandCompletion>
        get() = listOf()

    private lateinit var pluginLoader: PluginLoader<Command>

    override fun onEnable() {
        pluginLoader = PluginLoader.getInstance()
        App.commandUnknownMessage = "Error: Command \"%command%\" not found.\nType \"help\" to display the commands."
    }

    override fun onDisable() {
        App.resetCommandMessage()
    }

    override fun execute(args: List<String>) {
        val commands = pluginLoader.getPlugins().joinToString("\n") { "> ${it.name} : ${it.description}" }

        println("Available commands :")
        println("> exit : Stop current program\n$commands")
    }

}