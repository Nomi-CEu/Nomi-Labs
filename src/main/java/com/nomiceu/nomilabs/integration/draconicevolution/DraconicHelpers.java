package com.nomiceu.nomilabs.integration.draconicevolution;

import com.brandon3055.draconicevolution.DEFeatures;
import com.nomiceu.nomilabs.NomiLabs;
import com.nomiceu.nomilabs.config.LabsConfig;
import com.nomiceu.nomilabs.gregtech.material.registry.LabsMaterials;
import gregtech.api.GregTechAPI;
import gregtech.api.unification.OreDictUnifier;
import gregtech.api.unification.material.Material;
import gregtech.api.unification.ore.OrePrefix;
import gregtech.common.blocks.MetaBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DraconicHelpers {
    private static List<ItemStack> gtDraconiumCache;
    private static List<ItemStack> gtAwakenedCache;

    /**
     * Static values of default Draconic Evolution items
     */
    private static final int DRACONIC_NUGGET_META = 0;
    private static final int AWAKENED_NUGGET_META = 1;

    /**
     * ItemStack count to return
     */
    private static final int COUNT = 1;

    /**
     * Returns list with [0] = block, [1] = ingot, [2] = nugget
     */
    public static List<ItemStack> getGTDraconium() {
        if (gtDraconiumCache != null)
            return gtDraconiumCache;

        List<ItemStack> materialList = getListFromMaterial(LabsMaterials.Draconium, "draconium");
        if (materialList == null) {
            // No material found
            var gtDraconium = getDefaultDraconium();
            gtDraconiumCache = gtDraconium.stream().map((ItemStack::copy)).collect(Collectors.toList());
            return gtDraconium;
        }
        gtDraconiumCache = materialList.stream().map((ItemStack::copy)).collect(Collectors.toList());
        return materialList;
    }

    /**
     * Returns list with [0] = block, [1] = ingot, [2] = nugget
     */
    public static List<ItemStack> getGTAwakenedDraconium() {
        if (gtAwakenedCache != null)
            return gtAwakenedCache;

        List<ItemStack> materialList = getListFromMaterial(LabsMaterials.AwakenedDraconium, "awakened_draconium");
        if (materialList == null) {
            // No material found
            var gtAwakenedDraconium = getDefaultAwakened();
            gtAwakenedCache = gtAwakenedDraconium.stream().map((ItemStack::copy)).collect(Collectors.toList());
            return gtAwakenedDraconium;
        }
        gtAwakenedCache = materialList.stream().map((ItemStack::copy)).collect(Collectors.toList());
        return materialList;
    }

    /**
     * Returns list with [0] = block, [1] = ingot, [2] = nugget
     */
    public static List<ItemStack> getDefaultDraconium() {
        List<ItemStack> gtDraconium = new ArrayList<>();
        gtDraconium.add(new ItemStack(DEFeatures.draconiumBlock, COUNT));
        gtDraconium.add(new ItemStack(DEFeatures.draconiumIngot, COUNT));
        gtDraconium.add(new ItemStack(DEFeatures.nugget, COUNT, DRACONIC_NUGGET_META));
        return gtDraconium;
    }

    /**
     * Returns list with [0] = block, [1] = ingot, [2] = nugget
     */
    public static List<ItemStack> getDefaultAwakened() {
        List<ItemStack> gtAwakenedDraconium = new ArrayList<>();
        gtAwakenedDraconium.add(new ItemStack(DEFeatures.draconicBlock, COUNT));
        gtAwakenedDraconium.add(new ItemStack(DEFeatures.draconicIngot, COUNT));
        gtAwakenedDraconium.add(new ItemStack(DEFeatures.nugget, COUNT, AWAKENED_NUGGET_META));
        return gtAwakenedDraconium;
    }

    /**
     * Gets the list of a gt material, with [0] = block, [1] = ingot & [2] = nugget
     * @return List of material properties, null if material not found
     */
    @Nullable
    private static List<ItemStack> getListFromMaterial(@Nullable Material nomiLabsDeclaration, @NotNull String materialName) {
        Material material;
        List<ItemStack> materialList = new ArrayList<>();
        if (LabsConfig.enableGTCustomContent && nomiLabsDeclaration != null)
            material = nomiLabsDeclaration;
        else {
            material = GregTechAPI.materialManager.getMaterial(materialName);
            if (material == null) {
                NomiLabs.LOGGER.fatal("No GT material with name '" + materialName + "' for use in Nomi Labs Draconic Evolution Integration, and custom GT content has not been enabled! Defaulting to normal Draconic Evolution Draconium...");
                return null;
            }
        }
        IBlockState gtDraconiumState = MetaBlocks.COMPRESSED.get(material).getBlock(material);

        materialList.add(new ItemStack(gtDraconiumState.getBlock(), COUNT, gtDraconiumState.getBlock().getMetaFromState(gtDraconiumState)));
        materialList.add(OreDictUnifier.get(OrePrefix.ingot, material, COUNT));
        materialList.add(OreDictUnifier.get(OrePrefix.nugget, material, COUNT));
        return materialList;
    }

    public static boolean insertItem(ItemStack toInsert, EntityPlayer player) {
        IItemHandler handler = player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        if (handler == null) return false;
        for (int i = 0; i < handler.getSlots(); i++) {
            ItemStack inSlot = handler.getStackInSlot(i);
            if (inSlot.isEmpty() || (inSlot.getItem().equals(toInsert.getItem()) && inSlot.getMetadata() == toInsert.getMetadata() && inSlot.getCount() < toInsert.getMaxStackSize())) {
                ItemStack inserted = handler.insertItem(i, toInsert, false);
                if (inserted.isEmpty() || inserted.getItem().equals(Items.AIR))
                    return true;
            }
        }
        return false;
    }

    public static boolean validState(BlockStates allowedStates, IBlockState state, boolean wildCardAir) {
        if (allowedStates.isWildcard()) {
            if (wildCardAir)
                return state.getBlock().equals(Blocks.AIR);
            return true;
        }

        if (allowedStates.getDefault().equals(state)) return true;
        if (!allowedStates.hasSubstitutes()) return false;

        for (var substitute : allowedStates.getSubstitutes()) {
            if (substitute.equals(state)) return true;
        }
        return false;
    }
}
