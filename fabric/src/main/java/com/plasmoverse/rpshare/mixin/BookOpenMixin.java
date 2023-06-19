package com.plasmoverse.rpshare.mixin;

import com.plasmoverse.rpshare.ktmixin.KtBookOpenMixin;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.InventoryS2CPacket;
import net.minecraft.network.packet.s2c.play.OpenWrittenBookS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class BookOpenMixin {

    @Inject(at = @At("HEAD"), method = "onInventory", cancellable = true)
    private void onInventory(InventoryS2CPacket packet, CallbackInfo ci) {
        KtBookOpenMixin.INSTANCE.onInventory(packet, ci);
    }

    @Inject(at = @At("HEAD"), method = "onOpenWrittenBook", cancellable = true)
    private void onOpenWrittenBook(OpenWrittenBookS2CPacket packet, CallbackInfo ci) {
        KtBookOpenMixin.INSTANCE.onOpenWrittenBook(packet, ci);
    }
}