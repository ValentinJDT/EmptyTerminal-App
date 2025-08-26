package fr.valentinjdt.plugin.updater

import fr.valentinjdt.lib.plugin.Plugin
import kotlinx.coroutines.runBlocking
import requests.getLatestRelease

class UpdaterPlugin: Plugin("Updater", "Plugin that update terminal", "1.0.0") {
    override fun onEnable() {
        val version = runBlocking { getLatestRelease() }
        if(version != null && version != this.version) {
            println("A new version of EmptyTerminal is available: $version (current: ${this.version})")
            Updater.installLatest()
        }
    }
}