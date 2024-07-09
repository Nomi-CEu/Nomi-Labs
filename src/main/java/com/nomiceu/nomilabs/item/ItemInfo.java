package com.nomiceu.nomilabs.item;

import static com.nomiceu.nomilabs.util.LabsTranslate.*;

import java.util.*;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.IRarity;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * An item specifically designed to show information, via the tooltip.
 * <p>
 * Note that this item is not in any creative tabs or JEI!
 */
public class ItemInfo extends Item {

    private final EnumRarity rarity;
    private static final Map<Integer, Translatable[]> TOOLTIP_MAP;

    public static final int AE2_STUFF_REMAP_INFO = 1;

    public ItemInfo(ResourceLocation rl, EnumRarity rarity) {
        setRegistryName(rl);
        setMaxStackSize(64);
        setHasSubtypes(true);

        this.rarity = rarity;
    }

    /**
     * Does not include the item with no meta.
     */
    public Set<Integer> getSubMetas() {
        return TOOLTIP_MAP.keySet();
    }

    @Override
    protected boolean isInCreativeTab(@NotNull CreativeTabs targetTab) {
        return false;
    }

    @Override
    public @NotNull IRarity getForgeRarity(@NotNull ItemStack stack) {
        return rarity;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, @NotNull List<String> tooltip,
                               @NotNull ITooltipFlag flagIn) {
        if (!TOOLTIP_MAP.containsKey(stack.getMetadata())) return;

        var tooltipList = TOOLTIP_MAP.get(stack.getMetadata());
        for (var line : tooltipList) {
            tooltip.add(line.translate());
        }
    }

    static {
        TOOLTIP_MAP = new HashMap<>();
        // Leave Meta 0 as Nothing, so its almost a normal item
        TOOLTIP_MAP.put(AE2_STUFF_REMAP_INFO, new Translatable[] {
                translatable("tooltip.nomilabs.info.ae2-stuff.1"),
                translatableLiteral(""),
                translatable("tooltip.nomilabs.info.ae2-stuff.2"),
                translatableLiteral(""),
                translatable("tooltip.nomilabs.info.ae2-stuff.3"),
                translatable("tooltip.nomilabs.info.ae2-stuff.4"),
                translatable("tooltip.nomilabs.info.ae2-stuff.5")
        });
    }
}
