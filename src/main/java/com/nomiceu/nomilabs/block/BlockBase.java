package com.nomiceu.nomilabs.block;

import com.google.common.collect.ImmutableList;
import com.nomiceu.nomilabs.integration.top.TOPInfoProvider;
import com.nomiceu.nomilabs.tooltip.LabsTooltipHelper;
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

import java.util.ArrayList;
import java.util.List;

public class BlockBase extends Block implements TOPInfoProvider {
    private List<LabsTooltipHelper.Tooltip> description;
    public BlockBase(ResourceLocation rl, CreativeTabs tab, Material material, SoundType sound) {
        super(material);
        initialize(rl, tab, sound, ImmutableList.of());
    }

    /**
     * Make a block.
     * @param rl Resource Location
     * @param tab Creative Tab
     * @param material Material
     * @param sound Sound
     * @param description Description. Map of translation keys to formatting keys. Is of string to string so we can use GTFormatCodes
     */
    public BlockBase(ResourceLocation rl, CreativeTabs tab, Material material, SoundType sound, List<LabsTooltipHelper.Tooltip> description) {
        super(material);
        initialize(rl, tab, sound, description);
    }

    private void initialize(ResourceLocation rl, CreativeTabs tab, SoundType sound, List<LabsTooltipHelper.Tooltip> description) {
        this.setRegistryName(rl);
        this.setHardness(2.0F);
        this.setResistance(10.0F);
        this.setSoundType(sound);
        this.setCreativeTab(tab);
        this.description = description;
    }

    /**
     * I18n formatting is done here instead of in constructor as I18n is client only
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(@NotNull ItemStack stack, @Nullable World world, @NotNull List<String> tooltip, @NotNull ITooltipFlag flagIn) {
        for (var text : description)
            tooltip.add(text.getFormattedString());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public List<String> getTOPMessage(IBlockState state) {
        List<String> tooltip = new ArrayList<>();
        for (var text : description) {
            var string = text.getTOPFormattedString();
            if (string != null) tooltip.add(string);
        }
        return tooltip;
    }
}
