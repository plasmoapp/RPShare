package com.plasmoverse.rpshare

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import net.fabricmc.api.ClientModInitializer
import net.minecraft.client.MinecraftClient
import net.minecraft.item.ItemStack
import net.minecraft.text.Text

fun ItemStack.getPageAsString(page: Int): String? = nbt
	?.getList("pages", 8)
	?.getOrNull(page)
	?.asString()
	?.runCatching { Text.Serializer.fromJson(this) }
	?.getOrNull()
	?.string

object ResourcePackShareClient : ClientModInitializer {

	val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())
	
	override fun onInitializeClient() {

	}
}