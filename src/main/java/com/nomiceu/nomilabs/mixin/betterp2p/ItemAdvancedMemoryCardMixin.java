package com.nomiceu.nomilabs.mixin.betterp2p;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.nomiceu.nomilabs.integration.betterp2p.LabsBetterMemoryCardModes;
import com.projecturanus.betterp2p.item.ItemAdvancedMemoryCard;
import com.projecturanus.betterp2p.network.data.MemoryInfo;

/**
 * Changes the default mode to 'Add as Input'.
 */
@Mixin(value = ItemAdvancedMemoryCard.class, remap = false)
public class ItemAdvancedMemoryCardMixin {

    @Inject(method = "getInfo", at = @At("HEAD"))
    private void changeDefaultMode(ItemStack stack, CallbackInfoReturnable<MemoryInfo> cir) {
        if (stack.getTagCompound() == null) {
            var tag = new NBTTagCompound();
            tag.setInteger("mode", LabsBetterMemoryCardModes.ADD_AS_INPUT.ordinal());
            stack.setTagCompound(tag);
        }
    }
}
