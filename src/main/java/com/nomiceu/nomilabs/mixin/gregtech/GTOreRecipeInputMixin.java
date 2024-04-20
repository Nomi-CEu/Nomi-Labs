package com.nomiceu.nomilabs.mixin.gregtech;

import com.nomiceu.nomilabs.gregtech.mixinhelper.OreDictIngHelper;
import gregtech.api.recipes.ingredients.GTRecipeInput;
import gregtech.api.recipes.ingredients.GTRecipeOreInput;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = GTRecipeOreInput.class, remap = false)
public abstract class GTOreRecipeInputMixin extends GTRecipeInput {
    @Shadow
    @Final
    private int ore;

    @Shadow
    private ItemStack[] inputStacks;

    @Unique
    private long currentStandard;

    @Inject(method = "getInputStacks", at = @At("HEAD"))
    public void getUpdatedInputStacks(CallbackInfoReturnable<ItemStack[]> cir) {

        var standard = OreDictIngHelper.getStandard();

        // Regen Stacks if Needed
        if (currentStandard == standard) return;

        currentStandard = standard;
        inputStacks = OreDictionary.getOres(OreDictionary.getOreName(ore)).stream().map(is -> {
            is = is.copy();
            is.setCount(amount);
            return is;
        }).toArray(ItemStack[]::new);
    }
}
