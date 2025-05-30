package com.nomiceu.nomilabs.mixin.ae2;

import net.minecraft.inventory.Container;

import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import appeng.client.gui.AEBaseMEGui;
import appeng.client.gui.widgets.MEGuiTextField;
import appeng.fluids.client.gui.GuiMEPortableFluidCell;

/**
 * Auto focuses the search field, and enables repeat keyboard events.
 */
@Mixin(value = GuiMEPortableFluidCell.class, remap = false)
public abstract class GuiMEPortableFluidCellMixin extends AEBaseMEGui {

    @Shadow
    private MEGuiTextField searchField;

    /**
     * Mandatory Ignored Constructor
     */
    private GuiMEPortableFluidCellMixin(Container container) {
        super(container);
    }

    @Inject(method = "initGui", at = @At("RETURN"), remap = true)
    private void handleInit(CallbackInfo ci) {
        Keyboard.enableRepeatEvents(true);
        searchField.setFocused(true);
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }
}
