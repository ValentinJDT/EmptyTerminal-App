package fr.valentinjdt.plugin.async

import fr.valentinjdt.components.async.VERSION
import fr.valentinjdt.lib.plugin.Command
import fr.valentinjdt.lib.plugin.PluginLoader
import fr.valentinjdt.lib.plugin.SubCommandCompletion
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch


class AsyncCommand: Command("async", "Run in another thread any command", VERSION) {

    override val subCommandsCompletions: List<SubCommandCompletion>
        get() = listOf()

    override fun execute(args: List<String>) {
        if(args.isEmpty()) {
            println("Arguments not found")
            return
        }

        if(args[0] == name) {
            println("Can't thread himself command")
            return
        }

        var id = 0L;

        MainScope().launch {
            id = Thread.currentThread().id

            PluginLoader.executeCommand(args[0], args.drop(1))
        }.start()

        println("Command running in thread $id")
    }

}