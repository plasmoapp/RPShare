package com.plasmoverse.rpshare.model

import java.net.URL

data class DownloadData(
    val author: String,
    val url: URL,
    val description: String?,
)