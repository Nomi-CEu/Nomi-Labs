package com.nomiceu.nomilabs.mixin.ae2;

import net.minecraft.entity.player.InventoryPlayer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.nomiceu.nomilabs.integration.ae2.AccessibleContainerInterface;
import com.nomiceu.nomilabs.integration.ae2.LabsImplGuiLockedLabel;

import appeng.api.config.LockCraftingMode;
import appeng.api.implementations.IUpgradeableHost;
import appeng.client.gui.implementations.GuiInterface;
import appeng.client.gui.implementations.GuiUpgradeable;
import appeng.client.gui.widgets.GuiImgButton;
import appeng.core.localization.GuiText;

/**
 * Implements <a href="https://github.com/AE2-UEL/Applied-Energistics-2/pull/507">AE2 #507</a> to v0.56.5.
 */
@Mixin(value = GuiInterface.class, remap = false)
public class GuiInterfaceMixin extends GuiUpgradeable {

    @Shadow
    private GuiImgButton UnlockMode;

    /**
     * Mandatory Ignored Constructor
     */
    private GuiInterfaceMixin(InventoryPlayer inventoryPlayer, IUpgradeableHost te) {
        super(inventoryPlayer, te);
    }

    @Unique
    private LabsImplGuiLockedLabel labs$lockReason;

    @Override
    public void initGui() {
        super.initGui();

        if (labs$lockReason != null) {
            labelList.remove(labs$lockReason);
        }

        labs$lockReason = new LabsImplGuiLockedLabel(fontRenderer,
                guiLeft + 8 + fontRenderer.getStringWidth(GuiText.Interface.getLocal()) + 1, guiTop + 1,
                LockCraftingMode.NONE);
        labelList.add(labs$lockReason);
    }

    @Inject(method = "drawFG", at = @At("HEAD"))
    private void updateLockReason(int offsetX, int offsetY, int mouseX, int mouseY, CallbackInfo ci) {
        if (labs$lockReason != null && UnlockMode != null) {
            labs$lockReason.set(((AccessibleContainerInterface) cvb).labs$getLockingMode());
        }
    }
}
