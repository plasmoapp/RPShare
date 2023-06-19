package com.plasmoverse.rpshare.gui

import com.plasmoverse.rpshare.model.DownloadData
import io.wispforest.owo.ui.base.BaseOwoScreen
import io.wispforest.owo.ui.component.Components
import io.wispforest.owo.ui.container.Containers
import io.wispforest.owo.ui.container.FlowLayout
import io.wispforest.owo.ui.core.*
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.screen.ConfirmLinkScreen
import net.minecraft.text.Style
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Util

class DownloadPromptScreen(private val data: DownloadData): BaseOwoScreen<FlowLayout>() {

    override fun createAdapter(): OwoUIAdapter<FlowLayout> {
        return OwoUIAdapter.create(this, Containers::verticalFlow)
    }

    override fun build(rootComponent: FlowLayout) {

        StyleSheet.applyRoot(rootComponent)

        val container = StyleSheet.container
        
        val title = Components.label(Text.translatable("resourcepack.downloading"))
            .maxWidth(StyleSheet.width)
            .horizontalTextAlignment(HorizontalAlignment.CENTER)

        container.child(title)
        
        val authorLabel = Components.label(
            Text.translatable("book.byAuthor", data.author)
                .setStyle(Style.EMPTY.withFormatting(Formatting.GRAY))
        )
            .maxWidth(StyleSheet.width)
            .horizontalTextAlignment(HorizontalAlignment.CENTER)
            .margins(Insets.vertical(StyleSheet.margin))
        
        container.child(authorLabel)
        
        val urlLabel = Components.label(
            Text.literal(data.url.toString())
                .setStyle(Style.EMPTY.withFormatting(Formatting.UNDERLINE))
        ).textClickHandler {
            close()
            MinecraftClient.getInstance().setScreen(
                ConfirmLinkScreen({ ok ->
                    if (ok) {
                        Util.getOperatingSystem().open(data.url)
                    }
                    close()
                }, data.url.toString(), false)
            )
            true
        }
            .maxWidth(StyleSheet.width)
            .horizontalTextAlignment(HorizontalAlignment.CENTER)

        container.child(urlLabel)
        
        if (data.description != null) {
            val descriptionLabel = Components.label(Text.of(data.description))
                .maxWidth(StyleSheet.width)
                .horizontalTextAlignment(HorizontalAlignment.CENTER)
                .margins(Insets.top(StyleSheet.margin))
            container.child(descriptionLabel)
        }
//        
        val buttons = StyleSheet.horizontalButtonContainer
        
        val download = Components.button(
            Text.translatable("mco.brokenworld.download")
        ) {
            MinecraftClient.getInstance().setScreen(
                    DownloadScreen.andStartTask(data)
            )
        }.horizontalSizing(StyleSheet.horizontalButtonSizing)
        
        buttons.child(download, 0, 0)
        
        val cancel = Components.button(
            Text.translatable("gui.cancel")
        ) {
            close()
        }.horizontalSizing(StyleSheet.horizontalButtonSizing)
        
        buttons.child(cancel, 0, 1)
        
        container.child(buttons)
        
        rootComponent.child(container)
    }

}