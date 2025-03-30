package com.nomiceu.nomilabs.item.registry.register;

import static com.nomiceu.nomilabs.item.registry.LabsItems.*;
import static com.nomiceu.nomilabs.util.LabsNames.makeLabsName;
import static com.nomiceu.nomilabs.util.LabsTranslate.translatable;

import net.minecraft.item.EnumRarity;

import com.nomiceu.nomilabs.creativetab.registry.LabsCreativeTabs;
import com.nomiceu.nomilabs.item.ItemBase;
import com.nomiceu.nomilabs.util.LabsTranslate;

public class LabsMicroMiners {

    public static void initMicroMinerItems() {
        QUANTUM_FLUX = createItem(
                new ItemBase(makeLabsName("quantumflux"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC));
        GEM_SENSOR = createItem(
                new ItemBase(makeLabsName("gemsensor"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC));
        WARP_ENGINE = createItem(
                new ItemBase(makeLabsName("warpengine"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC));
        UNIVERSAL_NAVIGATOR = createItem(
                new ItemBase(makeLabsName("universalnavigator"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 64,
                        translatable("tooltip.nomilabs.universalnavigator.description")));
        QUANTUM_FLUXED_ETERNIUM_PLATING = createItem(new ItemBase(makeLabsName("quantumfluxedeterniumplating"),
                LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC));
        T1_GUIDANCE = createItem(
                new ItemBase(makeLabsName("t1guidance"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.UNCOMMON));
        T2_GUIDANCE = createItem(
                new ItemBase(makeLabsName("t2guidance"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.RARE));
        T1_LASER = createItem(
                new ItemBase(makeLabsName("t1laser"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.UNCOMMON));
        T2_LASER = createItem(new ItemBase(makeLabsName("t2laser"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.RARE));
        T3_LASER = createItem(new ItemBase(makeLabsName("t3laser"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC));
    }

    public static void initMicroMiners() {
        T1_SHIP = createItem(
                new ItemBase(makeLabsName("tieroneship"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.UNCOMMON, 16,
                        translatable("tooltip.nomilabs.tieroneship.description")));
        T2_SHIP = createItem(
                new ItemBase(makeLabsName("tiertwoship"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.UNCOMMON, 16,
                        translatable("tooltip.nomilabs.tiertwoship.description")));
        T3_SHIP = createItem(
                new ItemBase(makeLabsName("tierthreeship"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.UNCOMMON, 16,
                        translatable("tooltip.nomilabs.tierthreeship.description")));
        T4_SHIP = createItem(
                new ItemBase(makeLabsName("tierfourship"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.UNCOMMON, 16,
                        translatable("tooltip.nomilabs.tierfourship.description")));
        T4_HALF_SHIP = createItem(new ItemBase(makeLabsName("tierfourandhalfship"), LabsCreativeTabs.TAB_NOMI_LABS,
                EnumRarity.UNCOMMON, 16,
                translatable("tooltip.nomilabs.tierfourandhalfship.description")));
        T5_SHIP = createItem(
                new ItemBase(makeLabsName("tierfiveship"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.RARE, 16,
                        translatable("tooltip.nomilabs.tierfiveship.description")));
        T6_SHIP = createItem(
                new ItemBase(makeLabsName("tiersixship"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.RARE, 16,
                        translatable("tooltip.nomilabs.tiersixship.description")));
        T7_SHIP = createItem(
                new ItemBase(makeLabsName("tiersevenship"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.RARE, 16,
                        translatable("tooltip.nomilabs.tiersevenship.description")));
        T8_SHIP = createItem(
                new ItemBase(makeLabsName("tiereightship"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16,
                        translatable("tooltip.nomilabs.tiereightship.description")));
        T8_HALF_SHIP = createItem(
                new ItemBase(makeLabsName("tiereightandhalfship"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16,
                        translatable("tooltip.nomilabs.tiereightandhalfship.description")));
        T9_SHIP = createItem(
                new ItemBase(makeLabsName("tiernineship"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16,
                        translatable("tooltip.nomilabs.tiernineship.description")));
        T10_SHIP = createItem(
                new ItemBase(makeLabsName("tiertenship"), LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16,
                        translatable("tooltip.nomilabs.tiertenship.description1"),
                        translatable("tooltip.nomilabs.tiertenship.description2")));
    }

    public static void initStabilizedMicroMiners() {
        T1_STABILIZED_SHIP = createItem(new ItemBase(makeLabsName("tieroneship_stabilized"),
                LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16,
                getStabilizedMinerDesc()));
        T2_STABILIZED_SHIP = createItem(new ItemBase(makeLabsName("tiertwoship_stabilized"),
                LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16,
                getStabilizedMinerDesc()));
        T3_STABILIZED_SHIP = createItem(new ItemBase(makeLabsName("tierthreeship_stabilized"),
                LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16,
                getStabilizedMinerDesc()));
        T4_STABILIZED_SHIP = createItem(new ItemBase(makeLabsName("tierfourship_stabilized"),
                LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16,
                getStabilizedMinerDesc()));
        T4_HALF_STABILIZED_SHIP = createItem(new ItemBase(makeLabsName("tierfourandhalfship_stabilized"),
                LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16,
                getStabilizedMinerDesc()));
        T5_STABILIZED_SHIP = createItem(new ItemBase(makeLabsName("tierfiveship_stabilized"),
                LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16,
                getStabilizedMinerDesc()));
        T6_STABILIZED_SHIP = createItem(new ItemBase(makeLabsName("tiersixship_stabilized"),
                LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16,
                getStabilizedMinerDesc()));
        T7_STABILIZED_SHIP = createItem(new ItemBase(makeLabsName("tiersevenship_stabilized"),
                LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16,
                getStabilizedMinerDesc()));
        T8_STABILIZED_SHIP = createItem(new ItemBase(makeLabsName("tiereightship_stabilized"),
                LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16,
                getStabilizedMinerDesc()));
    }

    public static void initStabilizedMatter() {
        T1_STABILIZED_MATTER = createItem(new ItemBase(makeLabsName("tieroneship_stabilized_matter"),
                LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16,
                getStabilizedMatterDesc()));
        T2_STABILIZED_MATTER = createItem(new ItemBase(makeLabsName("tiertwoship_stabilized_matter"),
                LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16,
                getStabilizedMatterDesc()));
        T3_STABILIZED_MATTER = createItem(new ItemBase(makeLabsName("tierthreeship_stabilized_matter"),
                LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16,
                getStabilizedMatterDesc()));
        T4_STABILIZED_MATTER = createItem(new ItemBase(makeLabsName("tierfourship_stabilized_matter"),
                LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16,
                getStabilizedMatterDesc()));
        T4_HALF_STABILIZED_MATTER = createItem(new ItemBase(makeLabsName("tierfourandhalfship_stabilized_matter"),
                LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16,
                getStabilizedMatterDesc()));
        T5_STABILIZED_MATTER = createItem(new ItemBase(makeLabsName("tierfiveship_stabilized_matter"),
                LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16,
                getStabilizedMatterDesc()));
        T6_STABILIZED_MATTER = createItem(new ItemBase(makeLabsName("tiersixship_stabilized_matter"),
                LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16,
                getStabilizedMatterDesc()));
        T7_STABILIZED_MATTER = createItem(new ItemBase(makeLabsName("tiersevenship_stabilized_matter"),
                LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16,
                getStabilizedMatterDesc()));
        T8_STABILIZED_MATTER = createItem(new ItemBase(makeLabsName("tiereightship_stabilized_matter"),
                LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC, 16,
                getStabilizedMatterDesc()));
    }

    private static LabsTranslate.Translatable[] getStabilizedMinerDesc() {
        return new LabsTranslate.Translatable[] {
                translatable("tooltip.stabilized_miners.description1"),
                translatable("tooltip.stabilized_miners.description2")
        };
    }

    private static LabsTranslate.Translatable[] getStabilizedMatterDesc() {
        return new LabsTranslate.Translatable[] {
                translatable("tooltip.stabilized_matters.description1"),
                translatable("tooltip.stabilized_matters.description2")
        };
    }
}
