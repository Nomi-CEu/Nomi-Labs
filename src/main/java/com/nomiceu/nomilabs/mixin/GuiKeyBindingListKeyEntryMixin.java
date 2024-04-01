package com.nomiceu.nomilabs.mixin;

import com.nomiceu.nomilabs.groovy.KeyBindingHelper;
import net.minecraft.client.gui.GuiKeyBindingList;
import net.minecraft.client.settings.KeyBinding;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Allows a tooltip over each keybind, stating the category and id.
 */
@Mixin(GuiKeyBindingList.KeyEntry.class)
public abstract class GuiKeyBindingListKeyEntryMixin {
    @Shadow(aliases = "this$0")
    @Final
    GuiKeyBindingList this$0;

    @Shadow
    @Final
    private KeyBinding keybinding;

    @Inject(method = "drawEntry", at = @At("TAIL"))
    public void drawTooltips(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected, float partialTicks, CallbackInfo ci) {
        if (mouseY >= y && mouseY <= y + slotHeight) {
            var fontRenderer = ((GuiKeyBindingListAccessor) this$0).getMc().fontRenderer;

            KeyBindingHelper.drawKeybindingTooltip(mouseX, mouseY, fontRenderer, keybinding);
        }
    }
}
