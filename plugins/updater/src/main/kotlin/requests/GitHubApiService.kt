package requests

import fr.valentinjdt.plugin.requests.Release
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.time.Instant

interface GitHubApiService {
    @GET("repos/ValentinJDT/EmptyTerminal-App/releases")
    suspend fun getReleases(): Response<List<Release>>
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