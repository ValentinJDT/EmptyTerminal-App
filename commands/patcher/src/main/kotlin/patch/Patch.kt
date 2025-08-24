package fr.valentinjdt.plugin.patcher.patch

interface Patch {
    val format: Format
    fun apply(args: List<String>)
    fun verify(): Boolean? = null
}