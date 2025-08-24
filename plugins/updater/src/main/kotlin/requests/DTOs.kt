package fr.valentinjdt.plugin.requests

data class Author(
    val login: String,
    val id: Long,
)

data class Asset(
    val name: String,
    val browser_download_url: String,
)

data class Release(
    val tag_name: String,
    val published_at: String,
    val draft: Boolean,
    val assets: List<Asset>
)