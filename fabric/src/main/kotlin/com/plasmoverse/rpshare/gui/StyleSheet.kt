package com.plasmoverse.rpshare.gui

import io.wispforest.owo.ui.container.Containers
import io.wispforest.owo.ui.container.FlowLayout
import io.wispforest.owo.ui.core.*

object StyleSheet {

    const val width = 300
    const val margin = 12
    const val buttonPadding = 16

    val container: FlowLayout get() =
        Containers.verticalFlow(Sizing.fixed(width), Sizing.content()).apply {
            horizontalAlignment(HorizontalAlignment.CENTER)
            verticalAlignment(VerticalAlignment.CENTER)
        }

    fun applyRoot(root: FlowLayout) {
        root.apply {
            surface(Surface.VANILLA_TRANSLUCENT)
            horizontalAlignment(HorizontalAlignment.CENTER)
            verticalAlignment(VerticalAlignment.CENTER)
        }
    }

    val horizontalButtonContainer get() = Containers.grid(
        Sizing.fill(100),
        Sizing.content(),
        1,
        2
    ).apply {
        margins(Insets.top(buttonPadding))
    }

    val horizontalButtonSizing = Sizing.fixed((width - buttonPadding) / 2)
}