package fr.valentinjdt.plugin.patcher.patch.vlc

import java.io.File
import fr.valentinjdt.plugin.patcher.patch.Format
import fr.valentinjdt.plugin.patcher.patch.Patch
import fr.valentinjdt.plugin.patcher.utils.TempDir
import fr.valentinjdt.plugin.patcher.utils.downloadFile
import fr.valentinjdt.plugin.patcher.utils.isValidPath
import fr.valentinjdt.plugin.patcher.utils.unzipFile
import java.net.URL
import java.nio.file.Files
import java.util.*

class BluRayPatch(override val format: Format) : Patch {

    private val tempDir: TempDir = TempDir("/EmptyTerminal/VLC/")

    override fun apply(args: List<String>) {

        if(args.isEmpty() || !isValidPath(args[0]) || !File(args[0]).exists() || File(args[0]).isFile) {
            println("Please specify a valid VLC directory.")
            return
        }

        installKEYDB()
        installAACS(args[0])

        tempDir.close()
    }

    private fun installAACS(vlcDir: String) {
        val filePath = downloadFile(URL("https://vlc-bluray.whoknowsmy.name/files/${format.name.lowercase(Locale.getDefault())}/libaacs.dll"), tempDir.path.toFile().canonicalPath + "/libaacs.dll")
        val destinationAACS = File(vlcDir)

        if(!destinationAACS.exists()) {
            return
        }

        Files.move(File(filePath).toPath(), File(filePath, "libaacs.dll").toPath())
    }

    private fun installKEYDB() {
        val filePath = downloadFile(URL("http://fvonline-db.bplaced.net/export/keydb_eng.zip"), tempDir.path.toFile().canonicalPath + "/keydb_eng.zip")

        val destinationKEYDB = File(System.getenv("APPDATA") + "\\aacs\\")

        if(!destinationKEYDB.exists())
            destinationKEYDB.mkdirs()

        unzipFile(filePath, destinationKEYDB.absolutePath)

        File(filePath).delete()
    }

    override fun verify(): Boolean {
        val appdata = File(System.getenv("APPDATA")).canonicalPath

        return arrayListOf(
            File(appdata, "/aacs/keydb.cfg")
        ).all { it.exists() }
    }

}

