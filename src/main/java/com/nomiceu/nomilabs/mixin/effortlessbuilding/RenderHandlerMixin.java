package com.nomiceu.nomilabs.mixin.effortlessbuilding;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import nl.requios.effortlessbuilding.gui.buildmode.RadialMenu;
import nl.requios.effortlessbuilding.gui.buildmodifier.ModifierSettingsGui;
import nl.requios.effortlessbuilding.render.RenderHandler;

/**
 * Fixes Shader being applied on Pause Screens, Inventory, and on the Main Menu.
 */
@Mixin(value = RenderHandler.class, remap = false)
public class RenderHandlerMixin {

    @Unique
    private static boolean labs$wasVisible = false;

    @Inject(method = "onRenderGameOverlay", at = @At("HEAD"))
    private static void saveWasVisible(RenderGameOverlayEvent.Post event, CallbackInfo ci) {
        labs$wasVisible = RadialMenu.instance.isVisible();
    }

    @Inject(method = "onRender", at = @At("HEAD"), cancellable = true)
    private static void cancelRenderIfNeeded(RenderWorldLastEvent event, CallbackInfo ci) {
        // Cancels when game is not in focus. Does not cancel at the beginning of radial menu use (last condition),
        // during, or at the end (second-last condition), or when modifier menu is open.
        if ((!Minecraft.getMinecraft().inGameHasFocus) &&
                !(Minecraft.getMinecraft().currentScreen instanceof ModifierSettingsGui) && !labs$wasVisible &&
                !RadialMenu.instance.isVisible())
            ci.cancel();
    }
}
