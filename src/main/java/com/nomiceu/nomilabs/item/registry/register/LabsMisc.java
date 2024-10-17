package com.nomiceu.nomilabs.item.registry.register;

import static com.nomiceu.nomilabs.item.registry.LabsItems.*;
import static com.nomiceu.nomilabs.util.LabsNames.makeLabsName;

import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumRarity;
import net.minecraft.potion.Potion;

import com.nomiceu.nomilabs.creativetab.registry.LabsCreativeTabs;
import com.nomiceu.nomilabs.item.ItemBase;
import com.nomiceu.nomilabs.item.ItemInfo;
import com.nomiceu.nomilabs.item.ItemSmore;
import com.nomiceu.nomilabs.item.ItemTinyCoke;

public class LabsMisc {

    public static void initMisc() {
        GRAINS_OF_INNOCENCE = createItem(
                new ItemBase(makeLabsName("grainsofinnocence"), LabsCreativeTabs.TAB_NOMI_LABS));

        RADIUM_SALT = createItem(new ItemBase(makeLabsName("radiumsalt"), LabsCreativeTabs.TAB_NOMI_LABS));
        MOON_DUST = createItem(new ItemBase(makeLabsName("moondust"), LabsCreativeTabs.TAB_NOMI_LABS));

        REDSTONE_ARMOR_PLATE = createItem(
                new ItemBase(makeLabsName("redstonearmorplate"), LabsCreativeTabs.TAB_NOMI_LABS));
        CARBON_ARMOR_PLATE = createItem(new ItemBase(makeLabsName("carbonarmorplate"), LabsCreativeTabs.TAB_NOMI_LABS));
        LAPIS_ARMOR_PLATE = createItem(new ItemBase(makeLabsName("lapisarmorplate"), LabsCreativeTabs.TAB_NOMI_LABS));

        // Core and North are part of the Crafting Nether Star mod.
        NETHER_STAR_SOUTH = createItem(new ItemBase(makeLabsName("netherstarsouth"), LabsCreativeTabs.TAB_NOMI_LABS));
        NETHER_STAR_EAST = createItem(new ItemBase(makeLabsName("netherstareast"), LabsCreativeTabs.TAB_NOMI_LABS));
        NETHER_STAR_WEST = createItem(new ItemBase(makeLabsName("netherstarwest"), LabsCreativeTabs.TAB_NOMI_LABS));

        // Hydrogen is part of Solidified Items section, initialized in LabsSolidified.
        DENSE_HYDROGEN = createItem(new ItemBase(makeLabsName("densehydrogen"), LabsCreativeTabs.TAB_NOMI_LABS));
        ULTRA_DENSE_HYDROGEN = createItem(
                new ItemBase(makeLabsName("ultradensehydrogen"), LabsCreativeTabs.TAB_NOMI_LABS));

        INDUSTRIAL_REBREATHER_KIT = createItem(new ItemBase(makeLabsName("industrial_rebreather_kit"),
                LabsCreativeTabs.TAB_NOMI_LABS, EnumRarity.EPIC));

        MAGNETRON = createItem(new ItemBase(makeLabsName("magnetron"), LabsCreativeTabs.TAB_NOMI_LABS));

        TINY_COKE = createItem(new ItemTinyCoke(makeLabsName("tiny_coke"), LabsCreativeTabs.TAB_NOMI_LABS));

        // Custom model handling is in LabsItems
        INFO_ITEM = createItemWithoutModelHandling(new ItemInfo(makeLabsName("info_item"), EnumRarity.EPIC));

        PULSATING_DUST = createItem(new ItemBase(makeLabsName("pulsatingdust"), LabsCreativeTabs.TAB_NOMI_LABS));
        PULSATING_MESH = createItem(new ItemBase(makeLabsName("pulsatingmesh"), LabsCreativeTabs.TAB_NOMI_LABS));
    }

    public static void initCustomBehavior() {
        SMORES = new ItemSmore[4];
        createSmores();
    }

    private static void createSmores() {
        String[] smores = new String[] {
                "eightsmore",
                "sixteensmore",
                "thirtytwosmore",
                "sixtyfoursmore"
        };
        Potion[] potions = new Potion[] {
                MobEffects.ABSORPTION,
                MobEffects.SPEED,
                MobEffects.JUMP_BOOST,
                MobEffects.HASTE,
                MobEffects.SATURATION,
                MobEffects.HEALTH_BOOST,
                MobEffects.REGENERATION,
                MobEffects.STRENGTH
        };
        EnumRarity[] rarities = EnumRarity.values();

        int heal = 44;
        float saturation = 8.6f;
        int potionDuration = 1200;
        int potionAmplifier = 0;
        int index = 0;

        ItemSmore smore;

        for (String smoreName : smores) {
            heal *= 2;
            heal += 4;

            saturation *= 2;
            saturation++;

            potionDuration *= 2;

            smore = new ItemSmore(heal, saturation, makeLabsName(smoreName), LabsCreativeTabs.TAB_NOMI_LABS)
                    .setRarity(rarities[index]);

            for (Potion potion : potions)
                smore.addPotionEffect(potion, potionDuration, potionAmplifier);

            SMORES[index] = smore;
            createItem(smore);

            potionAmplifier++;
            index++;
        }
    }
}
