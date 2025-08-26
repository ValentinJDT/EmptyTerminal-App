package requests

import fr.valentinjdt.plugin.requests.Release
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.time.Instant
import kotlin.io.path.Path


interface GitHubApiService {
    @GET("repos/ValentinJDT/EmptyTerminal-App/releases")
    suspend fun getReleases(): Response<List<Release>>

    @GET("ValentinJDT/EmptyTerminal-App/releases/download/v{version}/EmptyTerminal-{version}.jar")
    suspend fun getJar(@Path("version") version: String): Response<ResponseBody>
}

suspend fun getJar(version: String): Response<ResponseBody>? {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://github.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service = retrofit.create(GitHubApiService::class.java)
    val response = service.getJar(version)

    val file = File("EmptyTerminal-${version}.jar")
    try {
        file.createNewFile()
        Files.write(Path(file.path), response.body()!!.bytes())
    } catch(e: IOException) {
        e.printStackTrace()
    }

    if (!response.isSuccessful) {
        println("Erreur: ${response.code()}")
        return null
    }

    return response
}

suspend fun getLatestRelease(): String? {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.github.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service = retrofit.create(GitHubApiService::class.java)
    val response = service.getReleases()

    if (!response.isSuccessful) {
        println("Erreur: ${response.code()}")
        return null
    }

    val releases = response.body() ?: return null
    val nonDraftReleases = releases.filter { !it.draft }

    if (nonDraftReleases.isEmpty()) {
        println("Aucune release publiée trouvée.")
        return null
    }

    // Trouver la release avec la date de publication la plus récente
    val latestRelease = nonDraftReleases.maxByOrNull { Instant.parse(it.published_at) }

    return latestRelease?.tag_name
}