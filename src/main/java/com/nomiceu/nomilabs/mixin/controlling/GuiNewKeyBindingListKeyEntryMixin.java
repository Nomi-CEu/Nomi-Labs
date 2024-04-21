package com.nomiceu.nomilabs.mixin.controlling;

import net.minecraft.client.settings.KeyBinding;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.nomiceu.nomilabs.groovy.KeyBindingHelper;

import us.getfluxed.controlsearch.client.gui.GuiNewKeyBindingList;

/**
 * Allows a tooltip over each keybind, stating the id.
 * <p>
 * Also fixes tooltips not rendering correctly.
 */
@Mixin(value = GuiNewKeyBindingList.KeyEntry.class, remap = false)
public class GuiNewKeyBindingListKeyEntryMixin {

    @Shadow
    @Final
    private KeyBinding keybinding;

    @Shadow
    @Final
    GuiNewKeyBindingList this$0;

    // This injects after the second call to DrawButton in the method. (Just before drawing tooltips)
    @Inject(method = "drawEntry",
            at = @At(value = "INVOKE",
                     target = "Lnet/minecraft/client/gui/GuiButton;drawButton(Lnet/minecraft/client/Minecraft;IIF)V",
                     shift = At.Shift.AFTER,
                     ordinal = 1),
            cancellable = true,
            require = 1,
            remap = true)
    public void addTooltip(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY,
                           boolean isSelected, float p_192634_9_, CallbackInfo ci) {
        int wrapped = GuiNewKeyBindingListAccessor.getWrapped() + 1; // Wrapped is -1 less than the amount of slot
                                                                     // heights, so we need wrapped + 1.
        var fontRenderer = ((GuiNewKeyBindingListAccessor) this$0).getMc().fontRenderer;
        if (mouseY >= y && mouseY <= y + slotHeight * wrapped) {
            KeyBindingHelper.drawKeybindingTooltip(mouseX, mouseY, fontRenderer, keybinding);
        }
        ci.cancel();
    }
}
