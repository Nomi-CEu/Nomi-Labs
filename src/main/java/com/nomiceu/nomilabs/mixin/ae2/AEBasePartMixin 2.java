package com.nomiceu.nomilabs.mixin.ae2;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import appeng.parts.AEBasePart;
import appeng.parts.misc.PartOreDicStorageBus;
import appeng.util.SettingsFrom;

/**
 * Applies <a href=
 * "https://github.com/AE2-UEL/Applied-Energistics-2/commit/26bb5986c636e9bdde62559b0d1c2bbc48c4b9e3">26bb598</a> for
 * v0.56.5.
 */
@Mixin(value = AEBasePart.class, remap = false)
public class AEBasePartMixin {

    @Inject(method = "uploadSettings", at = @At("HEAD"))
    private void readOreStorageBus(SettingsFrom from, NBTTagCompound compound, EntityPlayer player, CallbackInfo ci) {
        // noinspection ConstantValue
        if (compound != null && (Object) this instanceof PartOreDicStorageBus oreDicStorageBus) {
            oreDicStorageBus.saveOreMatch(compound.getString("oreMatch"));
        }
    }

    @Inject(method = "downloadSettings", at = @At("RETURN"), cancellable = true)
    private void writeOreStorageBus(SettingsFrom from, CallbackInfoReturnable<NBTTagCompound> cir) {
        if ((Object) this instanceof PartOreDicStorageBus oreDicStorageBus) {
            NBTTagCompound output = cir.getReturnValue();
            if (output == null)
                output = new NBTTagCompound();

            output.setString("oreMatch", oreDicStorageBus.getOreExp());
            cir.setReturnValue(output);
        }
    }
}
