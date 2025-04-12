package com.nomiceu.nomilabs.mixin.gregtech;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.nomiceu.nomilabs.config.LabsConfig;

import gregtech.api.gui.GuiTextures;
import gregtech.api.gui.ModularUI;
import gregtech.api.gui.widgets.ImageWidget;
import gregtech.api.gui.widgets.SimpleTextWidget;
import gregtech.common.metatileentities.multi.multiblockpart.MetaTileEntityMufflerHatch;
import gregtech.common.metatileentities.multi.multiblockpart.MetaTileEntityMultiblockPart;

/**
 * Change the muffler GUI to clarify that it is a dummy muffler, and will not generate any ashes.
 */
@Mixin(value = MetaTileEntityMufflerHatch.class, remap = false)
public abstract class MetaTileEntityMufflerHatchMixin extends MetaTileEntityMultiblockPart {

    /**
     * Mandatory Ignored Constructor
     */
    private MetaTileEntityMufflerHatchMixin(ResourceLocation metaTileEntityId, int tier) {
        super(metaTileEntityId, tier);
    }

    @Inject(method = "createUI(Lnet/minecraft/entity/player/EntityPlayer;)Lgregtech/api/gui/ModularUI;",
            at = @At("HEAD"),
            cancellable = true)
    private void setMufflerUI(EntityPlayer entityPlayer, CallbackInfoReturnable<ModularUI> cir) {
        if (!LabsConfig.modIntegration.enableDummyMufflers) {
            return;
        }
        ModularUI.Builder builder = ModularUI.builder(GuiTextures.BORDERED_BACKGROUND, 200, 100)
                .widget(new ImageWidget(92, 16, 16, 16, GuiTextures.INFO_ICON))
                .widget(
                        new SimpleTextWidget(100, 60, "nomilabs.gui.dummy_muffler", 0x555555, () -> "")
                                .setWidth(180));
        cir.setReturnValue(builder.build(getHolder(), entityPlayer));
    }
}
