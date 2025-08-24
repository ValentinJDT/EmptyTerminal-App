package fr.valentinjdt.plugin.example

import fr.valentinjdt.lib.plugin.Command
import fr.valentinjdt.lib.plugin.SubCommandCompletion

class ExampleCommand : Command("example", "An example command", VERSION) {

    override val subCommandsCompletions: List<SubCommandCompletion>
        get() = listOf(
            SubCommandCompletion("subcommand1", null),
            SubCommandCompletion("subcommand2", listOf(
                SubCommandCompletion("option1", null),
                SubCommandCompletion("option2", null)
            ))
        )

    override fun execute(args: List<String>) {
        TODO("Not yet implemented")
    }

}