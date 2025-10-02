package com.nomiceu.nomilabs.mixin.vanilla;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.cleanroommc.groovyscript.api.GroovyBlacklist;
import com.cleanroommc.groovyscript.compat.vanilla.VanillaModule;
import com.cleanroommc.groovyscript.helper.ingredient.OreDictIngredient;
import com.nomiceu.nomilabs.config.LabsConfig;

/**
 * This mixin makes it so that the repair cost returned from a given itemstack is always 0.
 */
@Mixin(ItemStack.class)
public class ItemStackMixin {

    @Inject(method = "getRepairCost()I", at = @At("HEAD"), cancellable = true)
    private void getRepairCost(CallbackInfoReturnable<Integer> cir) {
        if (LabsConfig.advanced.disableXpScaling)
            cir.setReturnValue(0);
    }

    @SuppressWarnings({ "AddedMixinMembersNamePattern", "unused" })
    @Unique
    public List<OreDictIngredient> getAllOreDicts() {
        return labs$getOreDictNames().stream()
                .map(OreDictIngredient::new)
                .collect(Collectors.toList());
    }

    @SuppressWarnings({ "AddedMixinMembersNamePattern", "unused" })
    @Unique
    public void removeAllOreDicts() {
        var stack = (ItemStack) (Object) this;
        labs$getOreDictNames().forEach((name) -> VanillaModule.oreDict.remove(name, stack));
    }

    @Unique
    @GroovyBlacklist
    private List<String> labs$getOreDictNames() {
        var stack = (ItemStack) (Object) this;
        var ids = OreDictionary.getOreIDs(stack);
        return Arrays.stream(ids)
                .mapToObj(OreDictionary::getOreName)
                .collect(Collectors.toList());
    }
}
