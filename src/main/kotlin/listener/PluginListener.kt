package fr.valentin.emptyterminal.listener

import fr.valentin.lib.vallib.event.EventHandler
import fr.valentin.lib.vallib.event.Listener
import fr.valentin.lib.vallib.plugin.event.PluginAlreadyLoadedEvent
import fr.valentin.lib.vallib.plugin.event.PluginCanNotLoadEvent
import fr.valentin.lib.vallib.plugin.event.PluginLoadEvent
import fr.valentin.lib.vallib.plugin.event.PluginUnLoadEvent

class PluginListener: Listener() {

    @EventHandler
    fun onPluginLoad(event: PluginLoadEvent) {
        if(!event.cancel) {
            println(event.plugin.name + " loaded (catch)")
        }
    }

    @EventHandler
    fun onPluginUnloaded(event: PluginUnLoadEvent) {
        if(!event.cancel) {
            println(event.plugin.name + " unloaded (catch)")
        }
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