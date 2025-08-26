package fr.valentinjdt.plugin.updater

import fr.valentinjdt.components.updater.VERSION
import fr.valentinjdt.lib.plugin.Plugin
import kotlinx.coroutines.runBlocking
import requests.getLatestRelease

class UpdaterPlugin: Plugin("updater", "Plugin that update terminal", VERSION) {
    override fun onEnable() {
        val currVersion = fr.valentinjdt.components.emptyterminal.VERSION
        val version = runBlocking { getLatestRelease() }
        if(version != null && version.substring(1) != currVersion) {
            println("A new version of EmptyTerminal is available: $version (current: ${currVersion})")
            Updater.installLatest()
        }
    }
}