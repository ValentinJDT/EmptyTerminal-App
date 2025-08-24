package fr.valentinjdt.plugin.patcher

import fr.valentinjdt.lib.plugin.Command
import fr.valentinjdt.lib.plugin.SubCommandCompletion
import fr.valentinjdt.plugin.patcher.patch.Patchs

class PatcherCommand: Command("patch", "Multiples patch for lot of tools/games", VERSION) {

    override val subCommandsCompletions: List<SubCommandCompletion>
        get() = Patchs.entries.map { SubCommandCompletion(it.patchName, null) }


    override fun execute(args: List<String>) {

        if(!System.getProperty("os.name").contains("Windows", true)) {
            println("This command is intended for Windows 10/11")
            return
        }

        if(args.isEmpty()) {
            println("Available patchs :\n" + Patchs.entries.joinToString(", ") { it.patchName })
            return
        }

        val patch = Patchs.getPath(args[0])

        if(patch != null) {
            patch.apply(args.drop(1))

            when(patch.verify()) {
                true -> println("Patch applied !")
                false -> println("⚠ Patch not correctly applied.")
                else -> println("Can't verify this patch. It may not work.")
            }

        } else {
            println("This patch does not exists")
        }
    }
}