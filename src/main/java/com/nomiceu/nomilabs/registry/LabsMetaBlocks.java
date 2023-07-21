package com.nomiceu.nomilabs.registry;

import com.nomiceu.nomilabs.block.BlockUniqueCasing;
import com.nomiceu.nomilabs.item.ItemBlockBase;
import gregtech.api.block.VariantItemBlock;
import net.minecraft.block.Block;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.function.Function;

import static com.nomiceu.nomilabs.util.RegistryNames.makeLabsName;

public class LabsMetaBlocks {
    public static BlockUniqueCasing UNIQUE_CASING;
    public static void preInit() {
        UNIQUE_CASING = new BlockUniqueCasing(makeLabsName("unique_casing"));
    }

    public static void registerItems(IForgeRegistry<Item> registry) {
        registry.register(createItemBlock(UNIQUE_CASING, VariantItemBlock::new));
    }

    public static void registerBlocks(IForgeRegistry<Block> registry) {
        registry.register(UNIQUE_CASING);
    }

    @SideOnly(Side.CLIENT)
    public static void registerModels() {
        UNIQUE_CASING.onModelRegister();
    }

    private static <T extends Block> ItemBlock createItemBlock(T block, Function<T, ItemBlock> producer) {
        ItemBlock itemBlock = producer.apply(block);
        ResourceLocation registryName = block.getRegistryName();
        if (registryName == null) {
            throw new IllegalArgumentException("Block " + block.getTranslationKey() + " has no registry name.");
        } else {
            itemBlock.setRegistryName(registryName);
            return itemBlock;
        }
    }
}
