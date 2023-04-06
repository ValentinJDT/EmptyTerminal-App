package fr.valentin.emptyterminal.event

import fr.valentin.api.event.Cancellable
import fr.valentin.api.plugin.IPlugin

data class PluginUnLoadEvent(val plugin: IPlugin): PluginEvent(plugin.url.path), Cancellable {
    override var cancel: Boolean = false
}