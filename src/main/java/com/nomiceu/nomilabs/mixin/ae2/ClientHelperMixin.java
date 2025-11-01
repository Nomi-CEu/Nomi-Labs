package com.nomiceu.nomilabs.mixin.ae2;

import net.minecraftforge.client.event.GuiScreenEvent;

import org.spongepowered.asm.mixin.Mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;

import appeng.client.ClientHelper;

/**
 * Stops AE2 from cancelling all interactions on bookmarks.
 */
@Mixin(value = ClientHelper.class, remap = false)
public class ClientHelperMixin {

    @WrapMethod(method = "MouseClickEvent")
    private void cancelMouseClickHandling(GuiScreenEvent.MouseInputEvent.Pre me, Operation<Void> original) {}
}
