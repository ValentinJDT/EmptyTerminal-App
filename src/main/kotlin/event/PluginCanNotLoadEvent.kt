package fr.valentin.emptyterminal.event

open class PluginCanNotLoadEvent(override val path: String): PluginEvent(path)