package fr.valentinjdt.plugin.describe

import fr.valentinjdt.lib.plugin.Command
import fr.valentinjdt.lib.plugin.Plugin
import fr.valentinjdt.lib.plugin.PluginLoader
import fr.valentinjdt.lib.plugin.SubCommandCompletion
import java.io.File

class DescribeCommand: Command("info", "Display information about add-ons and manage them.", VERSION) {

    override val subCommandsCompletions: List<SubCommandCompletion>
        get() = listOf(
            SubCommandCompletion("commands", null),
            SubCommandCompletion("plugins", null),
            SubCommandCompletion("load", listOf(
                SubCommandCompletion("command", File(commandLoader.directory).listFiles()!!.map { SubCommandCompletion(it.name, null) }),
                SubCommandCompletion("plugin", File(pluginLoader.directory).listFiles()!!.map { SubCommandCompletion(it.name, null) })
            )),
            SubCommandCompletion("unload", listOf(
                SubCommandCompletion("command", commandLoader.getPlugins().map { SubCommandCompletion(it.name, null) }),
                SubCommandCompletion("plugin", pluginLoader.getPlugins().map { SubCommandCompletion(it.name, null) }),
            )),
            SubCommandCompletion("reload", listOf(
                SubCommandCompletion("command", commandLoader.getPlugins().map { SubCommandCompletion(it.name, null) }),
                SubCommandCompletion("plugin", pluginLoader.getPlugins().map { SubCommandCompletion(it.name, null) }),
            ))
        )

    private lateinit var commandLoader: PluginLoader<Command>
    private lateinit var pluginLoader: PluginLoader<Plugin>

    override fun onEnable() {
        commandLoader = PluginLoader.getInstance()
        pluginLoader = PluginLoader.getInstance()
    }

    override fun execute(args: List<String>) {
        if(args.isEmpty()) {
            return
        }

        when(args[0]) {
            "commands" -> {
                if(args.size == 1) {
                    commandLoader.getPlugins().forEach { println(it.name + " -> " + it.javaClass.name + " (version: ${getVersion(args[1])})") }
                } else if(args.size == 2) {
                    println(args[1] + " -> " + getClassName(args[1]) + " (version: ${getVersion(args[1])})")
                }
            }
            "plugins" -> {
                if(args.size == 1) {
                    pluginLoader.getPlugins().forEach { println(it.name + " -> " + it.javaClass.name) }
                } else if(args.size == 2) {
                    println(args[1] + " -> " + getClassName(args[1]) + " (version: ${getVersion(args[1])})")
                }
            }
            "load" -> {
                if(args.size == 3) {
                    when(args[1]) {
                        "command" -> commandLoader.loadPlugin(File(commandLoader.directory, args[2]), true)
                        "plugin" -> pluginLoader.loadPlugin(File(pluginLoader.directory, args[2]), true)
                        else ->  println("Argument not found")
                    }
                } else {
                    println("Argument not found")
                }
            }
            "unload" -> {
                if(args.size == 3) {
                    when(args[1]) {
                        "command" -> commandLoader.unloadPlugin(args[2])
                        "plugin" -> pluginLoader.unloadPlugin(args[2])
                        else -> println("Arguments not found")
                    }
                } else {
                    println("Argument not found")
                }
            }
            "reload" -> {
                if(args.size == 3) {
                    when(args[1]) {
                        "command" -> commandLoader.reloadPlugin(args[2])
                        "plugin" -> pluginLoader.reloadPlugin(args[2])
                        else ->  println("Argument not found")
                    }
                } else {
                    println("Argument not found")
                }
            }
            else -> println("Argument not found")
        }
    }

    private fun getClassName(name: String): String {
        return commandLoader.getPlugins().find { command -> command.name == name }?.javaClass?.name ?: "Not found"
    }

    private fun getVersion(name: String): String {
        return commandLoader.getPlugins().find { command -> command.name == name }?.version ?: "null"
    }
}