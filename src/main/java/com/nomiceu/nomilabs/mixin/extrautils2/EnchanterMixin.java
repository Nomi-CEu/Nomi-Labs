package com.nomiceu.nomilabs.mixin.extrautils2;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.rwtema.extrautils2.api.machine.MachineSlotFluid;
import com.rwtema.extrautils2.api.machine.MachineSlotItem;
import com.rwtema.extrautils2.machine.MechEnchantmentRecipe;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.tuple.Pair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mixin(MechEnchantmentRecipe.class)
public class MixinMechEnchantmentRecipe {

    @Inject(method = "getJEIInputItemExamples", at = @At("RETURN"), cancellable = true)
    private void modifyJEIInputItemExamples(CallbackInfoReturnable<List<Pair<Map<MachineSlotItem, List<ItemStack>>, Map<MachineSlotFluid, List<FluidStack>>>>> cir) {
        List<Pair<Map<MachineSlotItem, List<ItemStack>>, Map<MachineSlotFluid, List<FluidStack>>>> originalList = cir.getReturnValue();

        // Get all items
        List<ItemStack> matchingItems = GameRegistry.findRegistry(Item.class).getValues().stream()
                .filter(item -> item.getRegistryName().getPath().startsWith("item.gt.tool")) 
                .map(ItemStack::new)
                .collect(Collectors.toList());
       
                
        
                

        // Modify the ImmutableMap here
        List<Pair<Map<MachineSlotItem, List<ItemStack>>, Map<MachineSlotFluid, List<FluidStack>>>> modifiedList = originalList.stream().map(pair -> {
            ImmutableMap<MachineSlotItem, List<ItemStack>> modifiedMap = new ImmutableMap.Builder<MachineSlotItem, List<ItemStack>>()
                    .putAll(pair.getLeft())
                    .put(XUMachineEnchanter.INPUT, ImmutableList.<ItemStack>builder()
                            .addAll(pair.getLeft().getOrDefault(XUMachineEnchanter.INPUT, ImmutableList.of()))
                            .addAll(matchingItems) // Add all tools.
                            .build())
                    .build();

            return Pair.of(modifiedMap, pair.getRight());
        }).collect(Collectors.toList());

        cir.setReturnValue(modifiedList);
    }
}
