package com.plasmoverse.rpshare

import com.plasmoverse.rpshare.ResourcePackShareClient.scope
import com.plasmoverse.rpshare.gui.ErrorScreen
import com.plasmoverse.rpshare.model.DownloadData
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import net.minecraft.client.MinecraftClient
import java.io.BufferedInputStream
import java.io.File
import java.net.URLConnection
import java.net.URLDecoder
import kotlin.Exception

class DownloadTask(private val data: DownloadData) {

    private var job: Job? = null

    var fileSize: Int = 0
        private set

    var progress: Int = 0
        private set

    val percent get() = runCatching { progress * 100 / fileSize }.getOrNull() ?: 0

    fun start() {
        job?.cancel()
        job = launchJob()
    }

    fun cancel() {
        job?.cancel()
    }

    private fun handleError(e: Throwable) {
        val instance = MinecraftClient.getInstance()
        instance.executeSync {
            instance.setScreen(ErrorScreen(data, e))
        }
    }

    private fun launchJob() = scope.launch {

        val connection = runCatching { data.url.openConnection() }
            .onFailure(::handleError)
            .getOrNull()
            ?: return@launch

        val fileName = getFileNameFromConnection(connection)
            ?.takeIf { it.isNotBlank() }
            ?: run {
                handleError(Exception("Invalid file name"))
                return@launch
            }

        val path = File(
            MinecraftClient.getInstance().resourcePackDir.toFile(),
            fileName,
        )

        try {
            fileSize = connection.contentLength
            progress = 0

            val inputStream = BufferedInputStream(connection.getInputStream())
            val outputStream = path.outputStream()

            val buffer = ByteArray(1024)
            var bytesRead = 0

            while (isActive && inputStream.read(buffer).also { bytesRead = it } != -1) {
                outputStream.write(buffer, 0, bytesRead)
                progress += bytesRead
            }

            outputStream.flush()
        } finally {
            val instance = MinecraftClient.getInstance()
            if (isActive) instance.executeSync { onComplete(fileName) }
            else if (path.exists()) path.deleteRecursively()
        }
    }

    private fun onComplete(fileName: String) {
        val instance = MinecraftClient.getInstance()
        val manager = instance.resourcePackManager
        val profile = "file/$fileName"

        manager.scanPacks()

        if (!manager.hasProfile(profile)) {
            handleError(Exception("File is not a resource pack"))
            File(instance.resourcePackDir.toFile(), fileName).also {
                if (it.exists()) it.deleteRecursively()
            }
        }

        instance.setScreen(null)

        manager.setEnabledProfiles(
            manager.enabledProfiles.map { it.name } + "file/$fileName"
        )
        instance.reloadResources()
    }

    private fun getFileNameFromConnection(connection: URLConnection): String? {
        val contentDisposition = connection.getHeaderField("Content-Disposition")
        if (contentDisposition != null) {
            val fileNameStartIndex = contentDisposition.indexOf("filename=")
            if (fileNameStartIndex >= 0) {
                val fileNameWithQuotes = contentDisposition.substring(fileNameStartIndex + 9)
                val fileName = fileNameWithQuotes.trim('\"')
                return URLDecoder.decode(fileName, "UTF-8")
            }
        }
        val path = connection.url.path
        val lastPathSegment = path.substring(path.lastIndexOf('/') + 1)
        return URLDecoder.decode(lastPathSegment, "UTF-8")
    }
}