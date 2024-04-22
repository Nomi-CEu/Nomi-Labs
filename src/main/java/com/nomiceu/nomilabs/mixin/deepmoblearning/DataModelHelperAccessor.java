package com.nomiceu.nomilabs.mixin.deepmoblearning;

import net.minecraft.item.ItemStack;

import org.apache.commons.lang3.NotImplementedException;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import mustapelto.deepmoblearning.common.util.DataModelHelper;

@Mixin(value = DataModelHelper.class, remap = false)
public interface DataModelHelperAccessor {

    @Invoker("tryIncreaseTier")
    static boolean tryIncreaseTier(ItemStack stack) {
        throw new NotImplementedException("DataModelHelper Accessor failed to apply!");
    }
}
