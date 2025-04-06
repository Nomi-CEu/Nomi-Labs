package com.nomiceu.nomilabs.mixin.vanilla;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiIngame;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.nomiceu.nomilabs.mixinhelper.AccessibleEntityPlayerSP;

@Mixin(EntityPlayerSP.class)
public class EntityPlayerSPMixin implements AccessibleEntityPlayerSP {

    @Shadow
    protected Minecraft mc;

    @Override
    public GuiIngame labs$getGuiIngame() {
        return mc.ingameGUI;
    }
}
