package com.nomiceu.nomilabs.item.registry.register;

import com.nomiceu.nomilabs.creativetab.registry.LabsCreativeTabs;
import com.nomiceu.nomilabs.item.ItemBase;
import net.minecraft.item.EnumRarity;

import static com.nomiceu.nomilabs.util.LabsNames.makeLabsName;
import static com.nomiceu.nomilabs.item.registry.LabsItems.*;

public class LabsMicroMiners {
    public static void initMicroMinerItems() {
        QUANTUM_FLUX = createItem(new ItemBase(makeLabsName("quantumflux"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC));
        GEM_SENSOR = createItem(new ItemBase(makeLabsName("gemsensor"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC));
        WARP_ENGINE = createItem(new ItemBase(makeLabsName("warpengine"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC));
        UNIVERSAL_NAVIGATOR = createItem(new ItemBase(makeLabsName("universalnavigator"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC));
        QUANTUM_FLUXED_ETERNIUM_PLATING = createItem(new ItemBase(makeLabsName("quantumfluxedeterniumplating"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC));
        T1_GUIDANCE = createItem(new ItemBase(makeLabsName("t1guidance"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.UNCOMMON));
        T2_GUIDANCE = createItem(new ItemBase(makeLabsName("t2guidance"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.RARE));
        T1_LASER = createItem(new ItemBase(makeLabsName("t1laser"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.UNCOMMON));
        T2_LASER = createItem(new ItemBase(makeLabsName("t2laser"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.RARE));
        T3_LASER = createItem(new ItemBase(makeLabsName("t3laser"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC));
    }

    public static void initMicroMiners() {
        T1_SHIP = createItem(new ItemBase(makeLabsName("tieroneship"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.UNCOMMON, 16));
        T2_SHIP = createItem(new ItemBase(makeLabsName("tiertwoship"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.UNCOMMON, 16));
        T3_SHIP = createItem(new ItemBase(makeLabsName("tierthreeship"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.UNCOMMON, 16));
        T4_SHIP = createItem(new ItemBase(makeLabsName("tierfourship"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.UNCOMMON, 16));
        T4_HALF_SHIP = createItem(new ItemBase(makeLabsName("tierfourandhalfship"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.UNCOMMON, 16));
        T5_SHIP = createItem(new ItemBase(makeLabsName("tierfiveship"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.RARE, 16));
        T6_SHIP = createItem(new ItemBase(makeLabsName("tiersixship"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.RARE, 16));
        T7_SHIP = createItem(new ItemBase(makeLabsName("tiersevenship"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.RARE, 16));
        T8_SHIP = createItem(new ItemBase(makeLabsName("tiereightship"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16));
        T8_HALF_SHIP = createItem(new ItemBase(makeLabsName("tiereightandhalfship"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16));
        T9_SHIP = createItem(new ItemBase(makeLabsName("tiernineship"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16));
        T10_SHIP = createItem(new ItemBase(makeLabsName("tiertenship"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16));
    }

    public static void initStabilizedMicroMiners() {
        T1_STABILIZED_SHIP = createItem(new ItemBase(makeLabsName("tieroneship_stabilized"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16));
        T2_STABILIZED_SHIP = createItem(new ItemBase(makeLabsName("tiertwoship_stabilized"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16));
        T3_STABILIZED_SHIP = createItem(new ItemBase(makeLabsName("tierthreeship_stabilized"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16));
        T4_STABILIZED_SHIP = createItem(new ItemBase(makeLabsName("tierfourship_stabilized"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16));
        T4_HALF_STABILIZED_SHIP = createItem(new ItemBase(makeLabsName("tierfourandhalfship_stabilized"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16));
        T5_STABILIZED_SHIP = createItem(new ItemBase(makeLabsName("tierfiveship_stabilized"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16));
        T6_STABILIZED_SHIP = createItem(new ItemBase(makeLabsName("tiersixship_stabilized"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16));
        T7_STABILIZED_SHIP = createItem(new ItemBase(makeLabsName("tiersevenship_stabilized"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16));
        T8_STABILIZED_SHIP = createItem(new ItemBase(makeLabsName("tiereightship_stabilized"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16));
    }

    public static void initStabilizedMatter() {
        T1_STABILIZED_MATTER= createItem(new ItemBase(makeLabsName("tieroneship_stabilized_matter"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16));
        T2_STABILIZED_MATTER = createItem(new ItemBase(makeLabsName("tiertwoship_stabilized_matter"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16));
        T3_STABILIZED_MATTER = createItem(new ItemBase(makeLabsName("tierthreeship_stabilized_matter"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16));
        T4_STABILIZED_MATTER = createItem(new ItemBase(makeLabsName("tierfourship_stabilized_matter"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16));
        T4_HALF_STABILIZED_MATTER = createItem(new ItemBase(makeLabsName("tierfourandhalfship_stabilized_matter"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16));
        T5_STABILIZED_MATTER = createItem(new ItemBase(makeLabsName("tierfiveship_stabilized_matter"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16));
        T6_STABILIZED_MATTER = createItem(new ItemBase(makeLabsName("tiersixship_stabilized_matter"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16));
        T7_STABILIZED_MATTER = createItem(new ItemBase(makeLabsName("tiersevenship_stabilized_matter"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16));
        T8_STABILIZED_MATTER = createItem(new ItemBase(makeLabsName("tiereightship_stabilized_matter"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16));
    }
}
