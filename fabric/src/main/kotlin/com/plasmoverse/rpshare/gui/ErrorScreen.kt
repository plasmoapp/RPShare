package com.plasmoverse.rpshare.gui

import com.plasmoverse.rpshare.model.DownloadData
import io.wispforest.owo.ui.base.BaseOwoScreen
import io.wispforest.owo.ui.component.Components
import io.wispforest.owo.ui.container.Containers
import io.wispforest.owo.ui.container.FlowLayout
import io.wispforest.owo.ui.core.HorizontalAlignment
import io.wispforest.owo.ui.core.Insets
import io.wispforest.owo.ui.core.OwoUIAdapter
import net.minecraft.client.MinecraftClient
import net.minecraft.text.Text

class ErrorScreen(
        private val data: DownloadData,
        private val exception: Throwable,
): BaseOwoScreen<FlowLayout>() {

    override fun createAdapter(): OwoUIAdapter<FlowLayout> {
        return OwoUIAdapter.create(this, Containers::verticalFlow)
    }

    override fun build(rootComponent: FlowLayout) {

        StyleSheet.applyRoot(rootComponent)

        val container = StyleSheet.container

        val title = Components.label(Text.translatable("mco.download.failed"))
            .maxWidth(StyleSheet.width)
            .horizontalTextAlignment(HorizontalAlignment.CENTER)
            .margins(Insets.bottom(StyleSheet.margin))

        container.child(title)

        val message = Components.label(Text.literal(exception.message))
            .maxWidth(StyleSheet.width)
            .horizontalTextAlignment(HorizontalAlignment.CENTER)

        container.child(message)

        val buttonContainer = StyleSheet.horizontalButtonContainer

        val retry = Components.button(
            Text.translatable("mco.brokenworld.download")
        ) {
            MinecraftClient.getInstance().setScreen(
                    DownloadScreen.andStartTask(data)
            )
        }.horizontalSizing(StyleSheet.horizontalButtonSizing)

        buttonContainer.child(retry, 0, 0)

        val cancel = Components.button(
            Text.literal("Cancel")
        ) {
            close()
        }.horizontalSizing(StyleSheet.horizontalButtonSizing)

        buttonContainer.child(cancel, 0, 1)

        container.child(buttonContainer)

        rootComponent.child(container)
    }

}