package com.plasmoverse.rpshare.ktmixin

import RPShareCommon
import com.plasmoverse.rpshare.getPageAsString
import com.plasmoverse.rpshare.gui.DownloadPromptScreen
import com.plasmoverse.rpshare.model.DownloadData
import net.minecraft.client.MinecraftClient
import net.minecraft.item.ItemStack
import net.minecraft.network.packet.s2c.play.InventoryS2CPacket
import net.minecraft.network.packet.s2c.play.OpenWrittenBookS2CPacket
import net.minecraft.screen.LecternScreenHandler
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

object KtBookOpenMixin {

    fun onInventory(packet: InventoryS2CPacket, ci: CallbackInfo) {
        val player = MinecraftClient.getInstance().player ?: return
        if (player.currentScreenHandler !is LecternScreenHandler) return
        val book = packet.contents.getOrNull(0) ?: return
        if (player.isSneaking) return
        MinecraftClient.getInstance().executeSync {
            val isRPShareBook = processBook(book)
            if (isRPShareBook) ci.cancel()
        }
    }

    fun onOpenWrittenBook(packet: OpenWrittenBookS2CPacket, ci: CallbackInfo) {
        val player = MinecraftClient.getInstance().player ?: return
        val book = player.getStackInHand(packet.hand) ?: return
        if (player.isSneaking) return
        MinecraftClient.getInstance().executeSync {
            val isRPShareBook = processBook(book)
            if (isRPShareBook) ci.cancel()
        }
    }

    private fun processBook(itemStack: ItemStack): Boolean {

        val url = itemStack.getPageAsString(0)
            ?.let(RPShareCommon::parseUrl)
            ?: return false

        val description = itemStack.getPageAsString(1)

        val author = itemStack.nbt?.getString("author")
            ?: return false

        val data = DownloadData(
            author,
            url,
            description,
        )

        MinecraftClient.getInstance().setScreen(
            DownloadPromptScreen(data)
        )

        return true
    }
}