package com.nomiceu.nomilabs.gregtech.block.registry;

import static com.nomiceu.nomilabs.util.LabsNames.makeLabsName;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import net.minecraft.util.IStringSerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.nomiceu.nomilabs.block.registry.LabsBlocks;
import com.nomiceu.nomilabs.gregtech.block.BlockUniqueCasing;
import com.nomiceu.nomilabs.item.registry.LabsItems;

import gregtech.api.block.VariantActiveBlock;
import gregtech.api.block.VariantItemBlock;

public class LabsMetaBlocks extends LabsBlocks {

    private static final List<VariantActiveBlock<?>> META_BLOCKS = new ArrayList<>();
    public static BlockUniqueCasing UNIQUE_CASING;

    public static void preInit() {
        UNIQUE_CASING = createMetaBlock(new BlockUniqueCasing(makeLabsName("unique_casing")));
    }

    public static <T extends VariantActiveBlock<V>,
            V extends Enum<V> & IStringSerializable> T createMetaBlock(T block) {
        META_BLOCKS.add(block);

        return createBlockWithRegisteredItem(block, registeredBlock -> {
            var item = new VariantItemBlock<>(block);
            item.setRegistryName(Objects.requireNonNull(block.getRegistryName()));
            return LabsItems.createItemWithoutModelHandling(item);
        });
    }

    @SideOnly(Side.CLIENT)
    public static void registerModels() {
        for (var block : META_BLOCKS)
            block.onModelRegister();
    }
}
