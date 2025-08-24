package fr.valentinjdt.plugin.updater

import fr.valentinjdt.lib.plugin.Plugin
import kotlinx.coroutines.runBlocking
import requests.getLatestRelease

class UpdaterPlugin: Plugin("Updater", "Plugin that updates other plugins", "1.0.0") {

    override fun onEnable() {
        val version = runBlocking { getLatestRelease() }
        println("Version ${version ?: "unknown"} of Updater plugin is available")
    }

    override fun onDisable() {
        println("Updater plugin disabled")
    }

}