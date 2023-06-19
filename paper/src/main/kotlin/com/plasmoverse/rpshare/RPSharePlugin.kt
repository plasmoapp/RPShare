package com.plasmoverse.rpshare

import RPShareCommon
import extension.asPlainText
import net.kyori.adventure.inventory.Book
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.block.Lectern
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.meta.BookMeta
import org.bukkit.plugin.java.JavaPlugin

class RPSharePlugin(): JavaPlugin(), Listener {
    override fun onEnable() {
        server.pluginManager.registerEvents(this, this)
    }

    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent) {

        if (event.player.isSneaking) return

        val book = getBookFromHand(event)
            ?: getBookFromLectern(event)
            ?: return

        val firstPage = book.pages().getOrNull(0)?.asPlainText() ?: return

        val title = book.title ?: return
        val url = RPShareCommon.parseUrl(firstPage) ?: return
        val description = book.pages().getOrNull(1)
        val author = book.author ?: return

        event.isCancelled = true

        val page = Component.text("#RPShare")
            .append(Component.text("\n\n"))
            .append(
                Component.translatable("resourcepack.downloading")
            )
            .append(Component.text("\n\n"))
            .append(
                Component.translatable("book.byAuthor", Component.text(author))
            )
            .append(Component.text("\n\n"))
            .append(
                Component.text(url.toString())
                    .decorate(TextDecoration.UNDERLINED)
                    .color(NamedTextColor.BLUE)
                    .clickEvent(ClickEvent.openUrl(url))
            )
            .append(Component.text("\n\n"))

        val newBook = Book.builder()
            .author(Component.text(author))
            .title(Component.text(title))
            .addPage(page)
            .also {
                if (description != null) it.addPage(description)
            }

        event.player.openBook(newBook)
    }

    private fun getBookFromHand(event: PlayerInteractEvent): BookMeta? = event.item
        ?.takeIf { it.type == Material.WRITTEN_BOOK }
        ?.itemMeta as? BookMeta

    private fun getBookFromLectern(event: PlayerInteractEvent): BookMeta? = event.clickedBlock
        ?.let { it.state as? Lectern }
        ?.inventory
        ?.getItem(0)
        ?.itemMeta as? BookMeta
}