import java.net.URL

object RPShareCommon {
    fun parseUrl(page: String) = page.trim()
        .takeIf { it.startsWith("#RPShare") }
        ?.split("\\s+".toRegex())
        ?.lastOrNull()
        ?.runCatching { URL(this) }
        ?.getOrNull()
}