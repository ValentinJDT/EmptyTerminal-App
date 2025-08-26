package fr.valentinjdt.plugin.updater

import fr.valentinjdt.components.updater.VERSION
import fr.valentinjdt.lib.plugin.Plugin
import kotlinx.coroutines.runBlocking
import requests.getLatestRelease

class UpdaterPlugin: Plugin("Updater", "Plugin that update terminal", VERSION) {
    override fun onEnable() {
        val version = runBlocking { getLatestRelease() }
        if(version != null && version != fr.valentinjdt.components.emptyterminal.VERSION) {
            println("A new version of EmptyTerminal is available: $version (current: ${this.version})")
            Updater.installLatest()
        }
    }
}