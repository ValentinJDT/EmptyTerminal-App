package fr.valentin.emptyterminal.event

import fr.valentin.api.event.Event

open class PluginEvent(open val path: String): Event()