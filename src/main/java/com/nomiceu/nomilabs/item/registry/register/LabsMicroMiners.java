package com.nomiceu.nomilabs.item.registry.register;

import com.google.common.collect.ImmutableList;
import com.nomiceu.nomilabs.creativetab.registry.LabsCreativeTabs;
import com.nomiceu.nomilabs.item.ItemBase;
import net.minecraft.item.EnumRarity;
import net.minecraft.util.text.TextFormatting;

import java.util.List;

import static com.nomiceu.nomilabs.util.LabsNames.makeLabsName;
import static com.nomiceu.nomilabs.item.registry.LabsItems.*;
import static com.nomiceu.nomilabs.tooltip.LabsTooltipHelper.Tooltip;

public class LabsMicroMiners {
    public static void initMicroMinerItems() {
        QUANTUM_FLUX = createItem(new ItemBase(makeLabsName("quantumflux"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC));
        GEM_SENSOR = createItem(new ItemBase(makeLabsName("gemsensor"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC));
        WARP_ENGINE = createItem(new ItemBase(makeLabsName("warpengine"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC));
        UNIVERSAL_NAVIGATOR = createItem(new ItemBase(makeLabsName("universalnavigator"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 64,
                ImmutableList.of(Tooltip.of("tooltip.nomilabs.universalnavigator.description").withFormat(TextFormatting.BLUE))));
        QUANTUM_FLUXED_ETERNIUM_PLATING = createItem(new ItemBase(makeLabsName("quantumfluxedeterniumplating"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC));
        T1_GUIDANCE = createItem(new ItemBase(makeLabsName("t1guidance"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.UNCOMMON));
        T2_GUIDANCE = createItem(new ItemBase(makeLabsName("t2guidance"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.RARE));
        T1_LASER = createItem(new ItemBase(makeLabsName("t1laser"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.UNCOMMON));
        T2_LASER = createItem(new ItemBase(makeLabsName("t2laser"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.RARE));
        T3_LASER = createItem(new ItemBase(makeLabsName("t3laser"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC));
    }

    public static void initMicroMiners() {
        T1_SHIP = createItem(new ItemBase(makeLabsName("tieroneship"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.UNCOMMON, 16,
                formatMicroMinerDesc("tooltip.nomilabs.tieroneship.description")));
        T2_SHIP = createItem(new ItemBase(makeLabsName("tiertwoship"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.UNCOMMON, 16,
                formatMicroMinerDesc("tooltip.nomilabs.tiertwoship.description")));
        T3_SHIP = createItem(new ItemBase(makeLabsName("tierthreeship"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.UNCOMMON, 16,
                formatMicroMinerDesc("tooltip.nomilabs.tierthreeship.description")));
        T4_SHIP = createItem(new ItemBase(makeLabsName("tierfourship"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.UNCOMMON, 16,
                formatMicroMinerDesc("tooltip.nomilabs.tierfourship.description")));
        T4_HALF_SHIP = createItem(new ItemBase(makeLabsName("tierfourandhalfship"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.UNCOMMON, 16,
                formatMicroMinerDesc("tooltip.nomilabs.tierfourandhalfship.description")));
        T5_SHIP = createItem(new ItemBase(makeLabsName("tierfiveship"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.RARE, 16,
                formatMicroMinerDesc("tooltip.nomilabs.tierfiveship.description")));
        T6_SHIP = createItem(new ItemBase(makeLabsName("tiersixship"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.RARE, 16,
                formatMicroMinerDesc("tooltip.nomilabs.tiersixship.description")));
        T7_SHIP = createItem(new ItemBase(makeLabsName("tiersevenship"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.RARE, 16,
                formatMicroMinerDesc("tooltip.nomilabs.tiersevenship.description")));
        T8_SHIP = createItem(new ItemBase(makeLabsName("tiereightship"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16,
                formatMicroMinerDesc("tooltip.nomilabs.tiereightship.description")));
        T8_HALF_SHIP = createItem(new ItemBase(makeLabsName("tiereightandhalfship"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16,
                formatMicroMinerDesc("tooltip.nomilabs.tiereightandhalfship.description")));
        T9_SHIP = createItem(new ItemBase(makeLabsName("tiernineship"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16,
                formatMicroMinerDesc("tooltip.nomilabs.tiernineship.description")));
        T10_SHIP = createItem(new ItemBase(makeLabsName("tiertenship"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16,
                ImmutableList.of(Tooltip.of("tooltip.nomilabs.tiertenship.description1").withFormat(getFormat()),
                        Tooltip.of("tooltip.nomilabs.tiertenship.description2").withFormat(getFormat()))));
    }

    public static void initStabilizedMicroMiners() {
        T1_STABILIZED_SHIP = createItem(new ItemBase(makeLabsName("tieroneship_stabilized"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16,
                formatStabilizedMinerDesc()));
        T2_STABILIZED_SHIP = createItem(new ItemBase(makeLabsName("tiertwoship_stabilized"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16,
                formatStabilizedMinerDesc()));
        T3_STABILIZED_SHIP = createItem(new ItemBase(makeLabsName("tierthreeship_stabilized"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16,
                formatStabilizedMinerDesc()));
        T4_STABILIZED_SHIP = createItem(new ItemBase(makeLabsName("tierfourship_stabilized"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16,
                formatStabilizedMinerDesc()));
        T4_HALF_STABILIZED_SHIP = createItem(new ItemBase(makeLabsName("tierfourandhalfship_stabilized"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16,
                formatStabilizedMinerDesc()));
        T5_STABILIZED_SHIP = createItem(new ItemBase(makeLabsName("tierfiveship_stabilized"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16,
                formatStabilizedMinerDesc()));
        T6_STABILIZED_SHIP = createItem(new ItemBase(makeLabsName("tiersixship_stabilized"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16,
                formatStabilizedMinerDesc()));
        T7_STABILIZED_SHIP = createItem(new ItemBase(makeLabsName("tiersevenship_stabilized"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16,
                formatStabilizedMinerDesc()));
        T8_STABILIZED_SHIP = createItem(new ItemBase(makeLabsName("tiereightship_stabilized"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16,
                formatStabilizedMinerDesc()));
    }

    public static void initStabilizedMatter() {
        T1_STABILIZED_MATTER= createItem(new ItemBase(makeLabsName("tieroneship_stabilized_matter"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16,
                formatStabilizedMatterDesc()));
        T2_STABILIZED_MATTER = createItem(new ItemBase(makeLabsName("tiertwoship_stabilized_matter"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16,
                formatStabilizedMatterDesc()));
        T3_STABILIZED_MATTER = createItem(new ItemBase(makeLabsName("tierthreeship_stabilized_matter"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16,
                formatStabilizedMatterDesc()));
        T4_STABILIZED_MATTER = createItem(new ItemBase(makeLabsName("tierfourship_stabilized_matter"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16,
                formatStabilizedMatterDesc()));
        T4_HALF_STABILIZED_MATTER = createItem(new ItemBase(makeLabsName("tierfourandhalfship_stabilized_matter"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16,
                formatStabilizedMatterDesc()));
        T5_STABILIZED_MATTER = createItem(new ItemBase(makeLabsName("tierfiveship_stabilized_matter"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16,
                formatStabilizedMatterDesc()));
        T6_STABILIZED_MATTER = createItem(new ItemBase(makeLabsName("tiersixship_stabilized_matter"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16,
                formatStabilizedMatterDesc()));
        T7_STABILIZED_MATTER = createItem(new ItemBase(makeLabsName("tiersevenship_stabilized_matter"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16,
                formatStabilizedMatterDesc()));
        T8_STABILIZED_MATTER = createItem(new ItemBase(makeLabsName("tiereightship_stabilized_matter"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16,
                formatStabilizedMatterDesc()));
    }

    private static List<Tooltip> formatMicroMinerDesc(String key) {
        return ImmutableList.of(Tooltip.of(key).withFormat(getFormat()));
    }

    private static List<Tooltip> formatStabilizedMinerDesc() {
        return ImmutableList.of(
                Tooltip.of("tooltip.stabilized_miners.description1").withFormat(getFormat()),
                Tooltip.of("tooltip.stabilized_miners.description2").withFormat(getFormat()),
                Tooltip.of("tooltip.stabilized_miners.description3").withFormat(getFormat())
        );
    }

    private static List<Tooltip> formatStabilizedMatterDesc() {
        return ImmutableList.of(
                Tooltip.of("tooltip.stabilized_matters.description").withFormat(getFormat())
        );
    }

    private static String getFormat() {
        return "" + TextFormatting.WHITE + TextFormatting.ITALIC;
    }
}
