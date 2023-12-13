package com.nomiceu.nomilabs.mixin.extrautils2;

import com.rwtema.extrautils2.items.ItemIngredients;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = ItemIngredients.class, remap = false)
public abstract class ItemIngredientsMixin {
    @Shadow
    public abstract ItemIngredients.Type getType(ItemStack stack);

    /**
     * Removes information for red coal and redstone coil items. (No more freq)
     * No Remap + Method Reference here, this is forge added
     */
    @Inject(method = "addInformation", at = @At("HEAD"), cancellable = true)
    public void removeInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced, CallbackInfo ci) {
        var type = getType(stack);
        if (type.equals(ItemIngredients.Type.RED_COAL) || type.equals(ItemIngredients.Type.REDSTONE_COIL)) ci.cancel();
    }

    /**
     * Removes Frequency from Redstone Coil (its added every tick in onUpdate)
     * Must be method reference + remap as this is originally mc function
     */
    @Inject(method = "onUpdate(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/entity/Entity;IZ)V", at = @At("HEAD"), cancellable = true, remap = true)
    public void stopFreq(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected, CallbackInfo ci) {
        if (getType(stack).equals(ItemIngredients.Type.REDSTONE_COIL)) {
            ci.cancel();
        }
    }
}
