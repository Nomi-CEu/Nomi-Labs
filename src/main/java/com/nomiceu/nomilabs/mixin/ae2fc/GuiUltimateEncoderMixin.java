package com.nomiceu.nomilabs.mixin.ae2fc;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.inventory.Container;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.glodblock.github.client.GuiUltimateEncoder;
import com.nomiceu.nomilabs.integration.ae2.InclNonConsumableButton;
import com.nomiceu.nomilabs.integration.ae2.InclNonConsumeButtonDisplay;
import com.nomiceu.nomilabs.integration.ae2.InclNonConsumeSettable;

import appeng.client.gui.AEBaseGui;

/**
 * Adds the incl non consume button to ultimate encoder.
 */
@Mixin(value = GuiUltimateEncoder.class, remap = false)
public abstract class GuiUltimateEncoderMixin extends AEBaseGui implements InclNonConsumeButtonDisplay {

    @Unique
    private InclNonConsumableButton labs$inclNonConsume;

    /**
     * Mandatory Ignored Constructor
     */
    private GuiUltimateEncoderMixin(Container container) {
        super(container);
    }

    @Inject(method = "initGui", at = @At("RETURN"), remap = true)
    private void initCustomButton(CallbackInfo ci) {
        labs$inclNonConsume = new InclNonConsumableButton(guiLeft + 120, guiTop + 154);
        labs$inclNonConsume.setAe2Fc();
        labs$inclNonConsume.visible = true;
        buttonList.add(labs$inclNonConsume);
    }

    @Inject(method = "actionPerformed",
            at = @At(value = "HEAD"),
            require = 1,
            remap = true)
    private void checkCustomButton(GuiButton btn, CallbackInfo ci) {
        labs$inclButtonPress(btn, (InclNonConsumeSettable) inventorySlots);
    }

    @Inject(method = "drawFG", at = @At("HEAD"))
    private void syncNonConsume(int offsetX, int offsetY, int mouseX, int mouseY, CallbackInfo ci) {
        labs$inclNonConsume.setInclNonConsume(((InclNonConsumeSettable) inventorySlots).labs$inclNonConsume());
    }

    @Unique
    @Override
    public InclNonConsumableButton labs$inclNonConsumeButton() {
        return labs$inclNonConsume;
    }
}
