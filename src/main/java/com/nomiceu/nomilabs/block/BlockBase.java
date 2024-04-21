package com.nomiceu.nomilabs.block;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.nomiceu.nomilabs.integration.top.TOPInfoProvider;

import gregtech.api.items.toolitem.ToolClasses;

public class BlockBase extends Block implements TOPInfoProvider {

    private final String[] description;

    public BlockBase(ResourceLocation rl, CreativeTabs tab, Material material, SoundType sound) {
        this(rl, tab, material, sound, new String[0]);
    }

    public BlockBase(ResourceLocation rl, CreativeTabs tab, Material material, SoundType sound, String... description) {
        this(rl, tab, material, sound, ToolClasses.PICKAXE, 1, description);
    }

    /**
     * Make a block.
     *
     * @param rl           Resource Location
     * @param tab          Creative Tab
     * @param material     Material
     * @param sound        Sound
     * @param tool         Tool Type. Use {@link ToolClasses}
     * @param harvestLevel Harvest Level
     * @param description  Description. Map of translation keys to formatting keys. Is of string to string so we can use
     *                     GTFormatCodes
     */
    public BlockBase(ResourceLocation rl, CreativeTabs tab, Material material, SoundType sound, String tool,
                     int harvestLevel, String... description) {
        super(material);

        this.setRegistryName(rl);
        this.setHardness(2.0F);
        this.setResistance(10.0F);
        this.setSoundType(sound);
        this.setCreativeTab(tab);
        this.setHarvestLevel(tool, harvestLevel);
        this.description = description;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(@NotNull ItemStack stack, @Nullable World world, @NotNull List<String> tooltip,
                               @NotNull ITooltipFlag flagIn) {
        Collections.addAll(tooltip, description);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public List<String> getTOPMessage(IBlockState state) {
        return new ArrayList<>(Arrays.asList(description));
    }
}
