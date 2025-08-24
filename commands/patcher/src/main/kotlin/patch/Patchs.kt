package fr.valentinjdt.plugin.patcher.patch

import fr.valentinjdt.plugin.patcher.patch.vlc.BluRayPatch


enum class Patchs(val patchName: String, val clazz: Class<out Patch>, val format: Format = if(System.getProperty("sun.arch.data.model") == "32") { Format.WIN32 } else { Format.WIN64 } ) {
    BLU_RAY("bluray", BluRayPatch::class.java),
    BLU_RAY_WIN64("bluray_win64", BluRayPatch::class.java, Format.WIN64),
    BLU_RAY_WIN32("bluray_win32", BluRayPatch::class.java, Format.WIN32);

    fun get(): Patch = clazz.getDeclaredConstructor(Format::class.java).newInstance(format)

    companion object {
        fun getPath(name: String): Patch? {
            return entries.find { it.patchName == name }?.run { this.get() }
        }
    }
}