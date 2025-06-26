package com.nomiceu.nomilabs.mixin.ae2;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.nomiceu.nomilabs.integration.ae2.InclNonConsumeSettable;
import com.nomiceu.nomilabs.integration.ae2.LabsInclNonConsumableButton;

import appeng.api.storage.ITerminalHost;
import appeng.client.gui.implementations.GuiExpandedProcessingPatternTerm;
import appeng.client.gui.implementations.GuiMEMonitorable;

/**
 * Adds the labs non-consume button to AE2.
 */
@Mixin(value = GuiExpandedProcessingPatternTerm.class, remap = false)
public class GuiExpandedProcessingPatternTermMixin extends GuiMEMonitorable {

    @Unique
    private LabsInclNonConsumableButton labs$inclNonConsume;

    /**
     * Mandatory Ignored Constructor
     */
    private GuiExpandedProcessingPatternTermMixin(InventoryPlayer inventoryPlayer, ITerminalHost te) {
        super(inventoryPlayer, te);
    }

    @Inject(method = "initGui", at = @At("RETURN"), remap = true)
    private void initCustomButton(CallbackInfo ci) {
        labs$inclNonConsume = new LabsInclNonConsumableButton(guiLeft + 74, guiTop + ySize - 153);
        labs$inclNonConsume.visible = true;
        buttonList.add(labs$inclNonConsume);
    }

    @Inject(method = "actionPerformed",
            at = @At(value = "INVOKE",
                     target = "Lappeng/client/gui/implementations/GuiMEMonitorable;actionPerformed(Lnet/minecraft/client/gui/GuiButton;)V",
                     shift = At.Shift.AFTER),
            require = 1,
            remap = true)
    private void checkCustomButton(GuiButton btn, CallbackInfo ci) {
        LabsInclNonConsumableButton.handleButtonPress(btn, labs$inclNonConsume,
                (InclNonConsumeSettable) inventorySlots);
    }

    @Inject(method = "drawFG", at = @At("HEAD"))
    private void syncNonConsume(int offsetX, int offsetY, int mouseX, int mouseY, CallbackInfo ci) {
        labs$inclNonConsume.setInclNonConsume(((InclNonConsumeSettable) inventorySlots).labs$inclNonConsume());
    }
}
