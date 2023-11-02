package com.nomiceu.nomilabs.item.registry.register;

import com.nomiceu.nomilabs.creativetab.registry.LabsCreativeTabs;
import com.nomiceu.nomilabs.item.ItemBase;
import com.nomiceu.nomilabs.item.ItemHandFramingTool;
import com.nomiceu.nomilabs.item.ItemSmore;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumRarity;
import net.minecraft.potion.Potion;

import static com.nomiceu.nomilabs.util.LabsNames.makeCTName;
import static com.nomiceu.nomilabs.item.registry.LabsItems.*;

public class LabsMisc {
    public static void initMisc() {
        GRAINS_OF_INNOCENCE = createItem(new ItemBase(makeCTName("grainsofinnocence"), LabsCreativeTabs.TAB_NOMI_LABS));

        RADIUM_SALT = createItem(new ItemBase(makeCTName("radiumsalt"), LabsCreativeTabs.TAB_NOMI_LABS));
        MOON_DUST = createItem(new ItemBase(makeCTName("moondust"), LabsCreativeTabs.TAB_NOMI_LABS));

        REDSTONE_ARMOR_PLATE = createItem(new ItemBase(makeCTName("redstonearmorplate"), LabsCreativeTabs.TAB_NOMI_LABS));
        CARBON_ARMOR_PLATE = createItem(new ItemBase(makeCTName("carbonarmorplate"), LabsCreativeTabs.TAB_NOMI_LABS));
        LAPIS_ARMOR_PLATE = createItem(new ItemBase(makeCTName("lapisarmorplate"), LabsCreativeTabs.TAB_NOMI_LABS));

        COMPRESSED_OCTADIC_CAPACITOR = createItem(new ItemBase(makeCTName("compressedoctadiccapacitor"), LabsCreativeTabs.TAB_NOMI_LABS));
        DOUBLE_COMPRESSED_OCTADIC_CAPACITOR = createItem(new ItemBase(makeCTName("doublecompressedoctadiccapacitor"), LabsCreativeTabs.TAB_NOMI_LABS));

        // Core and North are part of the Crafting Nether Star mod.
        NETHER_STAR_SOUTH = createItem(new ItemBase(makeCTName("netherstarsouth"), LabsCreativeTabs.TAB_NOMI_LABS));
        NETHER_STAR_EAST = createItem(new ItemBase(makeCTName("netherstareast"), LabsCreativeTabs.TAB_NOMI_LABS));
        NETHER_STAR_WEST = createItem(new ItemBase(makeCTName("netherstarwest"), LabsCreativeTabs.TAB_NOMI_LABS));

        // Hydrogen is part of Solidified Items section, initialized in LabsSolidified.
        DENSE_HYDROGEN = createItem(new ItemBase(makeCTName("densehydrogen"), LabsCreativeTabs.TAB_NOMI_LABS));
        ULTRA_DENSE_HYDROGEN = createItem(new ItemBase(makeCTName("ultradensehydrogen"), LabsCreativeTabs.TAB_NOMI_LABS));

        MAGNETRON = createItem(new ItemBase(makeCTName("magnetron"), LabsCreativeTabs.TAB_NOMI_LABS));

        PULSATING_DUST = createItem(new ItemBase(makeCTName("pulsatingdust"), LabsCreativeTabs.TAB_NOMI_LABS));
        PULSATING_MESH = createItem(new ItemBase(makeCTName("pulsatingmesh"), LabsCreativeTabs.TAB_NOMI_LABS));
    }

    public static void initCustomBehavior() {
        HAND_FRAMING_TOOL = createItem(new ItemHandFramingTool(makeCTName("hand_framing_tool"), LabsCreativeTabs.TAB_NOMI_LABS));
        SMORES = new ItemSmore[4];
        createSmores();
    }

    private static void createSmores() {
        String [] smores = new String[]{
                "eightsmore",
                "sixteensmore",
                "thirtytwosmore",
                "sixtyfoursmore"
        };
        Potion[] potions = new Potion[]{
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

            smore = new ItemSmore(heal, saturation, makeCTName(smoreName), LabsCreativeTabs.TAB_NOMI_LABS)
                    .setRarity(rarities[index]);

            for (Potion potion : potions)
                smore.addPotionEffect(potion, potionDuration, potionAmplifier);

            SMORES[index] = smore;

            potionAmplifier++;
            index++;
        }
    }
}
