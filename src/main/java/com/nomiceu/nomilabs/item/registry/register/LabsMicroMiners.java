package com.nomiceu.nomilabs.item.registry.register;

import com.nomiceu.nomilabs.creativetab.registry.LabsCreativeTabs;
import com.nomiceu.nomilabs.item.ItemBase;
import net.minecraft.item.EnumRarity;

import static com.nomiceu.nomilabs.util.LabsNames.makeCTName;
import static com.nomiceu.nomilabs.item.registry.LabsItems.*;

public class LabsMicroMiners {
    public static void initMicroMinerItems() {
        QUANTUM_FLUX = createItem(new ItemBase(makeCTName("quantumflux"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC));
        GEM_SENSOR = createItem(new ItemBase(makeCTName("gemsensor"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC));
        WARP_ENGINE = createItem(new ItemBase(makeCTName("warpengine"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC));
        UNIVERSAL_NAVIGATOR = createItem(new ItemBase(makeCTName("universalnavigator"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC));
        QUANTUM_FLUXED_ETERNIUM_PLATING = createItem(new ItemBase(makeCTName("quantumfluxedeterniumplating"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC));
        T1_GUIDANCE = createItem(new ItemBase(makeCTName("t1guidance"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.UNCOMMON));
        T2_GUIDANCE = createItem(new ItemBase(makeCTName("t2guidance"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.RARE));
        T1_LASER = createItem(new ItemBase(makeCTName("t1laser"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.UNCOMMON));
        T2_LASER = createItem(new ItemBase(makeCTName("t2laser"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.RARE));
        T3_LASER = createItem(new ItemBase(makeCTName("t3laser"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC));
    }

    public static void initMicroMiners() {
        T1_SHIP = createItem(new ItemBase(makeCTName("tieroneship"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.UNCOMMON, 16));
        T2_SHIP = createItem(new ItemBase(makeCTName("tiertwoship"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.UNCOMMON, 16));
        T3_SHIP = createItem(new ItemBase(makeCTName("tierthreeship"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.UNCOMMON, 16));
        T4_SHIP = createItem(new ItemBase(makeCTName("tierfourship"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.UNCOMMON, 16));
        T4_HALF_SHIP = createItem(new ItemBase(makeCTName("tierfourandhalfship"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.UNCOMMON, 16));
        T5_SHIP = createItem(new ItemBase(makeCTName("tierfiveship"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.RARE, 16));
        T6_SHIP = createItem(new ItemBase(makeCTName("tiersixship"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.RARE, 16));
        T7_SHIP = createItem(new ItemBase(makeCTName("tiersevenship"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.RARE, 16));
        T8_SHIP = createItem(new ItemBase(makeCTName("tiereightship"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16));
        T8_HALF_SHIP = createItem(new ItemBase(makeCTName("tiereightandhalfship"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16));
        T9_SHIP = createItem(new ItemBase(makeCTName("tiernineship"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16));
        T10_SHIP = createItem(new ItemBase(makeCTName("tiertenship"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16));
    }

    public static void initStabilizedMicroMiners() {
        T1_STABILIZED_SHIP = createItem(new ItemBase(makeCTName("tieroneship_stabilized"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16));
        T2_STABILIZED_SHIP = createItem(new ItemBase(makeCTName("tiertwoship_stabilized"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16));
        T3_STABILIZED_SHIP = createItem(new ItemBase(makeCTName("tierthreeship_stabilized"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16));
        T4_STABILIZED_SHIP = createItem(new ItemBase(makeCTName("tierfourship_stabilized"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16));
        T4_HALF_STABILIZED_SHIP = createItem(new ItemBase(makeCTName("tierfourandhalfship_stabilized"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16));
        T5_STABILIZED_SHIP = createItem(new ItemBase(makeCTName("tierfiveship_stabilized"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16));
        T6_STABILIZED_SHIP = createItem(new ItemBase(makeCTName("tiersixship_stabilized"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16));
        T7_STABILIZED_SHIP = createItem(new ItemBase(makeCTName("tiersevenship_stabilized"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16));
        T8_STABILIZED_SHIP = createItem(new ItemBase(makeCTName("tiereightship_stabilized"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16));
    }

    public static void initStabilizedMatter() {
        T1_STABILIZED_MATTER= createItem(new ItemBase(makeCTName("tieroneship_stabilized_matter"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16));
        T2_STABILIZED_MATTER = createItem(new ItemBase(makeCTName("tiertwoship_stabilized_matter"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16));
        T3_STABILIZED_MATTER = createItem(new ItemBase(makeCTName("tierthreeship_stabilized_matter"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16));
        T4_STABILIZED_MATTER = createItem(new ItemBase(makeCTName("tierfourship_stabilized_matter"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16));
        T4_HALF_STABILIZED_MATTER = createItem(new ItemBase(makeCTName("tierfourandhalfship_stabilized_matter"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16));
        T5_STABILIZED_MATTER = createItem(new ItemBase(makeCTName("tierfiveship_stabilized_matter"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16));
        T6_STABILIZED_MATTER = createItem(new ItemBase(makeCTName("tiersixship_stabilized_matter"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16));
        T7_STABILIZED_MATTER = createItem(new ItemBase(makeCTName("tiersevenship_stabilized_matter"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16));
        T8_STABILIZED_MATTER = createItem(new ItemBase(makeCTName("tiereightship_stabilized_matter"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16));
    }
}
