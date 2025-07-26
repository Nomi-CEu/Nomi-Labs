package com.nomiceu.nomilabs.mixin.ae2;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.nomiceu.nomilabs.integration.ae2.InclNonConsumeSettable;
import com.nomiceu.nomilabs.util.LabsSide;

import appeng.api.implementations.guiobjects.IGuiItemObject;
import appeng.container.guisync.GuiSync;
import appeng.container.implementations.ContainerPatternEncoder;
import appeng.parts.reporting.AbstractPartEncoder;

/**
 * Allows saving import/not import non-consume items into patterns.
 */
@Mixin(value = ContainerPatternEncoder.class, remap = false)
public abstract class ContainerPatternEncoderMixin implements InclNonConsumeSettable {

    @Shadow
    public abstract AbstractPartEncoder getPart();

    @Shadow
    protected IGuiItemObject iGuiItemObject;

    @GuiSync(320001)
    @Unique
    public boolean labs$inclNonConsume = true;

    @Inject(method = "detectAndSendChanges", at = @At("RETURN"), remap = true)
    private void syncNonConsume(CallbackInfo ci) {
        if (!LabsSide.isServer()) return;
        if (getPart() != null && (getPart() instanceof InclNonConsumeSettable set)) {
            labs$inclNonConsume = set.labs$inclNonConsume();
        } else if (iGuiItemObject != null) {
            NBTTagCompound compound = iGuiItemObject.getItemStack().getTagCompound();
            if (compound != null && compound.hasKey("labs$inclNonConsume", Constants.NBT.TAG_BYTE)) {
                labs$inclNonConsume = compound.getBoolean("labs$inclNonConsume");
            }
        }
    }

    @Override
    public boolean labs$inclNonConsume() {
        return labs$inclNonConsume;
    }

    @Override
    public void labs$setInclNonConsume(boolean inclNonConsume) {
        labs$inclNonConsume = inclNonConsume;
        if (getPart() != null && (getPart() instanceof InclNonConsumeSettable set)) {
            set.labs$setInclNonConsume(inclNonConsume);
        } else if (iGuiItemObject != null) {
            NBTTagCompound compound = iGuiItemObject.getItemStack().getTagCompound();
            if (compound == null) compound = new NBTTagCompound();

            compound.setBoolean("labs$inclNonConsume", inclNonConsume);
            iGuiItemObject.getItemStack().setTagCompound(compound);
        }
    }
}
