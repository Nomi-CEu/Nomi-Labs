package com.nomiceu.nomilabs.remap;

import com.nomiceu.nomilabs.LabsValues;
import com.nomiceu.nomilabs.config.LabsConfig;
import com.nomiceu.nomilabs.remap.remapper.*;
import com.nomiceu.nomilabs.util.LabsBackup;
import com.nomiceu.nomilabs.util.LabsNames;
import gregtech.api.util.GTUtility;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.StartupQuery;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.spongepowered.include.com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

import static com.nomiceu.nomilabs.LabsValues.*;

public class LabsRemappers {
    public static List<ItemRemapper> ITEM_REMAPPERS;
    public static List<BlockRemapper> BLOCK_REMAPPERS;

    public static boolean checked = false;

    public static final String NEWEST_PRE_LABS_VERSION = "0.1.1";
    public static final String NEWEST_PRE_NOMI_VERSION = "1.7-alpha-2a";

    public static void preInit() {
        ITEM_REMAPPERS = ImmutableList.of(
                // Remap Deprecated Items
                new DeprecatedItemRemapper(),

                // Remap Content Tweaker Items
                new ItemRemapper(rl -> (LabsConfig.content.customContent.remap && rl.getNamespace().equals(CONTENTTWEAKER_MODID)),
                        rl -> LabsNames.makeLabsName(rl.getPath())),

                /*
                 * Remap old DevTech perfect gem to new perfect gem.
                 * DevTech did this badly, and created a MetaPrefixItem with GT's material registry but their
                 * Mod ID, so we need to map the DevTech metaitem to the GT metaitem in missing mappings.
                 */
                new ItemRemapper(rl -> (LabsConfig.content.gtCustomContent.remapPerfectGems &&
                        rl.getNamespace().equals(DEVTECH_MODID) && rl.getPath().equals(PERFECT_GEM_META)),
                        rl -> GTUtility.gregtechId(PERFECT_GEM_META))
        );
        BLOCK_REMAPPERS = ImmutableList.of(
                // Remap Content Tweaker Blocks
                new BlockRemapper(rl -> (LabsConfig.content.customContent.remap && rl.getNamespace().equals(LabsValues.CONTENTTWEAKER_MODID)),
                        rl -> LabsNames.makeLabsName(rl.getPath()))
        );
    }

    public static void remapItems(RegistryEvent.MissingMappings<Item> event) {
        remapEntries(event, ITEM_REMAPPERS);
    }

    public static void remapBlocks(RegistryEvent.MissingMappings<Block> event) {
        remapEntries(event, BLOCK_REMAPPERS);
    }

    private static <T extends IForgeRegistryEntry<T>> void remapEntries(RegistryEvent.MissingMappings<T> event, List<? extends Remapper<T>> remappers) {
        if (!checked && (LabsConfig.content.customContent.remap || LabsConfig.content.gtCustomContent.remapPerfectGems || LabsConfig.modIntegration.enableExtraUtils2Integration)) {
            // Init Remapping
            StringBuilder warn = new StringBuilder();

            if (LabsConfig.content.customContent.remap || LabsConfig.content.gtCustomContent.remapPerfectGems) {
                warn.append(TextFormatting.BOLD).append("This world must have its entries remapped.\n").append(TextFormatting.RESET);

                if (LabsConfig.content.customContent.remap) {
                    if (LabsConfig.content.gtCustomContent.remapPerfectGems)
                        warn.append("This maps old Content Tweaker and Perfect Gems Items to the New Ones.\n");
                    else
                        warn.append("This maps old Content Tweaker Items to the New Ones.\n");
                }
                else
                    warn.append("This maps old Perfect Gems to the New Ones.\n");

                warn.append(TextFormatting.BOLD).append("A Backup will be made. Pressing 'No' Will Cancel World Loading.\n\n")
                        .append(TextFormatting.RED)
                        .append("Note that after the world is loaded with this, you CANNOT undo this!\n")
                        .append(TextFormatting.RESET)
                        .append("You will have to load from the generated backup.\n\n");
            }

            if (LabsConfig.content.customContent.remap || LabsConfig.modIntegration.enableExtraUtils2Integration) {
                List<String> remove = new ArrayList<>();
                warn.append(TextFormatting.BOLD).append("Data Fixers will be Activated.\n").append(TextFormatting.RESET);
                if (LabsConfig.content.customContent.remap) {
                    remove.add("Dark Red Coal");
                    warn.append("This properly transforms old Dark Red Coal.\n");
                }
                if (LabsConfig.modIntegration.enableExtraUtils2Integration) {
                    remove.add("Red Coal");
                    remove.add("Redstone Coils");
                    warn.append("This modifies Red Coal and Redstone Coil (XU2 Items),\n")
                            .append("to not have Frequency NBT, which is removed.\n\n");
                }
                warn.append(TextFormatting.YELLOW).append("Please Remove:\n")
                        .append(String.join(",\n", remove.subList(0, remove.size() - 1)))
                        .append(",\nand ").append(remove.get(remove.size() - 1)).append(",\n")
                        .append("from all Ender Storage Ender Chests, in an older instance.\n")
                        .append(TextFormatting.GREEN).append("(Nomi-CEu ").append(NEWEST_PRE_NOMI_VERSION)
                        .append("-, Nomi Labs ").append(NEWEST_PRE_LABS_VERSION)
                        .append("-)\n\n").append(TextFormatting.RESET);
            }

            boolean confirmed = StartupQuery.confirm(warn.toString());
            if (!confirmed)
                StartupQuery.abort();
            LabsBackup.createWorldBackup();
            checked = true;
        }
        for (var entry : event.getAllMappings()) {
            for (var remapper : remappers) {
                var ha = remapper.shouldRemap(entry.key);
                if (!ha) continue;
                remapper.remapEntry(entry);
                break;
            }
        }
    }
}
