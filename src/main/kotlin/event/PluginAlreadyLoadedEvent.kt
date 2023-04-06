package fr.valentin.emptyterminal.event

import fr.valentin.api.plugin.IPlugin

data class PluginAlreadyLoadedEvent(val plugin: IPlugin): PluginCanNotLoadEvent(plugin.url.path)
