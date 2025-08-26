package fr.valentinjdt.plugin.updater

import fr.valentinjdt.components.updater.VERSION
import fr.valentinjdt.lib.plugin.Plugin
import kotlinx.coroutines.runBlocking
import requests.getLatestRelease

class UpdaterPlugin: Plugin("Updater", "Plugin that updates other plugins", VERSION) {

    override fun onEnable() {
        val version = runBlocking { getLatestRelease() }
        if("v" + fr.valentinjdt.components.emptyterminal.VERSION != version) {
            println("Version ${version ?: "unknown"} of EmptyTerminal is available")
        }
    }

    override fun onDisable() {
        println("Updater plugin disabled")
    }

}