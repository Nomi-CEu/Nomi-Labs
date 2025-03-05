package com.nomiceu.nomilabs.mixin.gregtech;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import gregtech.api.gui.GuiTextures;
import gregtech.api.gui.IUIHolder;
import gregtech.api.gui.ModularUI;
import gregtech.api.gui.widgets.ImageWidget;
import gregtech.api.gui.widgets.SimpleTextWidget;
import gregtech.common.metatileentities.multi.multiblockpart.MetaTileEntityMufflerHatch;
import gregtech.common.metatileentities.multi.multiblockpart.MetaTileEntityMultiblockPart;

@Mixin(value = MetaTileEntityMufflerHatch.class, remap = false)
public abstract class MetaTileEntityMufflerHatchMixin extends MetaTileEntityMultiblockPart {

    // The mixin must provide a constructor matching the superclass.
    protected MetaTileEntityMufflerHatchMixin(ResourceLocation metaTileEntityId, int tier) {
        super(metaTileEntityId, tier);
    }

    /**
     * @reason Overwrite createUI to display a custom message.
     * @author D-Alessian
     */
    @Overwrite
    protected ModularUI createUI(EntityPlayer entityPlayer) {
        // Directly call getHolder() inherited from MetaTileEntityMultiblockPart
        IUIHolder holder = this.getHolder();
        ModularUI.Builder builder = ModularUI.builder(GuiTextures.BORDERED_BACKGROUND, 200, 100)
                .widget(new ImageWidget(92, 16, 16, 16, GuiTextures.INFO_ICON))
                .widget(
                        new SimpleTextWidget(100, 60, "gregtech.gui.muffler.notify_change", 0x555555, () -> "")
                                .setWidth(180));
        return builder.build(holder, entityPlayer);
    }
}
