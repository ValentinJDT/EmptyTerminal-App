package fr.valentin.emptyterminal.listener

import fr.valentin.emptyterminal.event.PluginCanNotLoadEvent
import fr.valentin.emptyterminal.event.PluginLoadEvent
import fr.valentin.api.event.EventHandler
import fr.valentin.api.event.Listener
import fr.valentin.emptyterminal.event.PluginAlreadyLoadedEvent
import fr.valentin.emptyterminal.event.PluginUnLoadEvent

class PluginListener: Listener() {

    @EventHandler
    fun onPluginLoad(event: PluginLoadEvent) {
        println(event.plugin.name + " loaded (catch)")
    }

    @EventHandler
    fun onPluginUnloaded(event: PluginUnLoadEvent) {
        println(event.plugin.name + " unloaded (catch)")
    }

    @EventHandler
    fun onPluginLoadFail(event: PluginCanNotLoadEvent) {
        println("Can't load : " + event.path)
    }

    @EventHandler
    fun onPluginAlreadyLoaded(event: PluginAlreadyLoadedEvent) {
        println("Plugin file already loaded : " + event.path)
    }

}