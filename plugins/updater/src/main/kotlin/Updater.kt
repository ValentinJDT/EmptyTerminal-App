package fr.valentinjdt.plugin.updater

import kotlinx.coroutines.runBlocking
import requests.getJar
import requests.getLatestRelease

object Updater {

    private val JAR: String = javaClass
        .protectionDomain
        .codeSource
        .location
        .file

    fun installLatest() = runBlocking {
        println("Installing latest version...")
        getLatestRelease()?.let {
            intallVersion(it)
        }
        println("Done! Restart to use the new version.")
    }

    fun intallVersion(version: String) = runBlocking {
        val jar = getJar(version)?.body()
        jar?.byteStream().use { input ->
            val file = java.io.File(JAR)
            file.setWritable(true)
            input?.readBytes()?.let {
                file.writeBytes(it)
            }
        }
    }

}