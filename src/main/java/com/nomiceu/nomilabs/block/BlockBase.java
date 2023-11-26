package com.nomiceu.nomilabs.block;


import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class BlockBase extends Block {
    Map<String, String> description;
    public BlockBase(ResourceLocation rl, CreativeTabs tab, Material material, SoundType sound) {
        super(material);
        initialize(rl, tab, sound, ImmutableMap.of());
    }

    /**
     * Make a block.
     * @param rl Resource Location
     * @param tab Creative Tab
     * @param material Material
     * @param sound Sound
     * @param description Description. Map of translation keys to formatting keys. Is of string to string so we can use GTFormatCodes
     */
    public BlockBase(ResourceLocation rl, CreativeTabs tab, Material material, SoundType sound, Map<String, String> description) {
        super(material);
        initialize(rl, tab, sound, description);
    }

    private void initialize(ResourceLocation rl, CreativeTabs tab, SoundType sound, Map<String, String> description) {
        setRegistryName(rl);
        setHardness(2.0F);
        setResistance(10.0F);
        setSoundType(sound);
        setCreativeTab(tab);
        this.description = description;
    }

    /**
     * I18n formatting is done here instead of in constructor as I18n is client only
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(@NotNull ItemStack stack, @Nullable World world, @NotNull List<String> tooltip, @NotNull ITooltipFlag flagIn) {
        for (var translationKey : description.keySet())
            tooltip.add(description.get(translationKey) + I18n.format(translationKey));
    }
}
