package fr.valentinjdt.plugin.patcher.utils

import java.io.File
import java.io.FileOutputStream
import java.net.URL
import java.nio.file.Files
import java.nio.file.InvalidPathException
import java.nio.file.Path
import java.nio.file.Paths
import java.util.zip.ZipFile

fun unzipFile(zipFileName: String, to: String) {
    ZipFile(zipFileName).use { zip ->
        zip.entries().asSequence().forEach { entry ->
            zip.getInputStream(entry).use { input ->
                File(to + "/" + entry.name).outputStream().use { output ->
                    input.copyTo(output)
                }
            }
        }
    }
}

fun downloadFile(url: URL, fileName: String): String {
    var downloadedBytes = 0L
    var fileSize: Long

    val connection = url.openConnection()
    connection.getInputStream().use { _ ->
        fileSize = connection.getHeaderField("Content-Length").toLong()
        println("File size: ${fileSize/1024.0/1024.0} MB")
    }

    print("Download ${url.file.substring(url.file.lastIndexOf("/"))}\n[          ]")

    url.openStream().use { input ->
        val file = File(fileName)
        FileOutputStream(file).use { output ->
            val buffer = ByteArray(4096)
            var bytesRead = input.read(buffer)

            while (bytesRead != -1) {
                output.write(buffer, 0, bytesRead)
                downloadedBytes += bytesRead
                val progress = (downloadedBytes * 100.0 / fileSize).toInt()
                print("\r[")
                repeat(progress / 5) { print("=") }
                repeat(20 - progress / 5) { print(" ") }
                print("] $progress%")
                bytesRead = input.read(buffer)
            }
        }
    }

    println("\nDownload finished")
    return fileName;
}

fun isValidPath(path: String): Boolean {
    try {
        Paths.get(path)
    } catch(ex: InvalidPathException) {
        return false
    }
    return true
}

fun isAbsolutePath(path: String): Boolean {
    val regex = Regex(pattern = "^[a-zA-Z]:[/|\\\\]")
    return regex.find(path)?.run { true } ?: false
}

class TempDir(absPath: String) : AutoCloseable {
    val path: Path = Files.createTempDirectory(absPath)
    override fun close(): Unit = run {
        if(Files.exists(path))
            path.toFile().deleteRecursively()
    }
}