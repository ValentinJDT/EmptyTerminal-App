package fr.valentinjdt.emptyterminal.listener

import fr.valentinjdt.lib.event.EventHandler
import fr.valentinjdt.lib.event.Listener
import fr.valentinjdt.lib.plugin.event.PluginAlreadyLoadedEvent
import fr.valentinjdt.lib.plugin.event.PluginCanNotLoadEvent
import fr.valentinjdt.lib.plugin.event.PluginLoadEvent
import fr.valentinjdt.lib.plugin.event.PluginUnLoadEvent

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