package com.plasmoverse.rpshare.extension

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer

fun Component.asPlainText() = PlainTextComponentSerializer.plainText().serialize(this)