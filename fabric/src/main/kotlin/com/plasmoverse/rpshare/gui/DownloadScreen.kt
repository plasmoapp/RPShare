package com.plasmoverse.rpshare.gui

import com.plasmoverse.rpshare.DownloadTask
import com.plasmoverse.rpshare.model.DownloadData
import io.wispforest.owo.ui.base.BaseOwoScreen
import io.wispforest.owo.ui.component.Components
import io.wispforest.owo.ui.component.LabelComponent
import io.wispforest.owo.ui.container.Containers
import io.wispforest.owo.ui.container.FlowLayout
import io.wispforest.owo.ui.core.*
import net.minecraft.text.Text

class DownloadScreen(
        private val data: DownloadData,
        private val task: DownloadTask,
): BaseOwoScreen<FlowLayout>() {

    override fun createAdapter(): OwoUIAdapter<FlowLayout> {
        return OwoUIAdapter.create(this, Containers::verticalFlow)
    }

    override fun tick() {
        val inMegaBytes = String.format("%.2f", task.fileSize.toDouble() / 1000000)
        val component = Text.translatable("resourcepack.progress", inMegaBytes).append(
            Text.literal(" ${task.percent}%")
        )
        component(LabelComponent::class.java, "progress")?.text(component)
    }

    override fun close() {
        task.cancel()
        super.close()
    }

    override fun build(rootComponent: FlowLayout) {

        StyleSheet.applyRoot(rootComponent)

        val container = StyleSheet.container

        val title = Components.label(Text.translatable("resourcepack.downloading"))
            .maxWidth(StyleSheet.width)
            .horizontalTextAlignment(HorizontalAlignment.CENTER)
            .margins(Insets.bottom(StyleSheet.margin))

        container.child(title)

        val progress = Components.label(Text.literal(task.percent.toString())).apply {
            maxWidth(StyleSheet.width)
            horizontalTextAlignment(HorizontalAlignment.CENTER)
            id("progress")
        }

        container.child(progress)

        val cancel = Components.button(Text.translatable("gui.cancel")) {
            close()
        }.also {
            it.horizontalSizing(Sizing.fill(100))
        }.margins(Insets.top(StyleSheet.buttonPadding))

        container.child(cancel)

        rootComponent.child(container)
    }

    companion object {
        fun andStartTask(data: DownloadData) =
            DownloadScreen(data, DownloadTask(data).also { it.start() })
    }
}