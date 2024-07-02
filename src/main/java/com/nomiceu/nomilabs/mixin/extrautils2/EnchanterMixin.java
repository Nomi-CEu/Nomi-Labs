package com.yourmod.mixin;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.rwtema.extrautils2.machine.MechEnchantmentRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import net.minecraftforge.fluids.FluidStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mixin(MechEnchantmentRecipe.class)
public class MixinMechEnchantmentRecipe {

    @Inject(method = "getJEIInputItemExamples", at = @At("RETURN"), cancellable = true)
    private void modifyJEIInputItemExamples(CallbackInfoReturnable<List<Pair<Map<MachineSlotItem, List<ItemStack>>, Map<MachineSlotFluid, List<FluidStack>>>>> cir) {
        List<Pair<Map<MachineSlotItem, List<ItemStack>>, Map<MachineSlotFluid, List<FluidStack>>>> originalList = cir.getReturnValue();

        // Modify the ImmutableMap here
        List<Pair<Map<MachineSlotItem, List<ItemStack>>, Map<MachineSlotFluid, List<FluidStack>>>> modifiedList = originalList.stream().map(pair -> {
            Map<MachineSlotItem, List<ItemStack>> modifiedMap = new ImmutableMap.Builder<MachineSlotItem, List<ItemStack>>()
                    .putAll(pair.getLeft())
                    .put(XUMachineEnchanter.INPUT, ImmutableList.of(
                            Items.WOODEN_PICKAXE, Items.IRON_PICKAXE, Items.GOLDEN_PICKAXE, Items.DIAMOND_PICKAXE
                    ).stream().map(ItemStack::new).collect(Collectors.toList()))
                    .build();

            return Pair.of(modifiedMap, pair.getRight());
        }).collect(Collectors.toList());

        cir.setReturnValue(modifiedList);
    }
}

