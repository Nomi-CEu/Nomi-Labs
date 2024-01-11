package com.nomiceu.nomilabs.remap;

import com.nomiceu.nomilabs.LabsValues;
import com.nomiceu.nomilabs.NomiLabs;
import com.nomiceu.nomilabs.config.LabsConfig;
import com.nomiceu.nomilabs.remap.datafixer.DataFixerHandler;
import com.nomiceu.nomilabs.util.LabsNames;
import gregtech.api.util.GTUtility;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.StartupQuery;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.spongepowered.include.com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import static com.nomiceu.nomilabs.LabsValues.*;
import static com.nomiceu.nomilabs.remap.Remapper.RemapTypes;

public class LabsRemappers {
    public static Map<RemapTypes, List<? extends Remapper>> remappers;
    public static Map<RemapTypes, List<Pattern>> ignorePtns;

    private static Map<ResourceLocation, ResourceLocation> deprecationRemap;
    private static Remapper metaBlockRemapper;

    public static boolean checked = false;

    public static void preInit() {
        helpersInit();
        initRemappers();
        parseIgnoreConfigs();
    }

    private static void initRemappers() {
        remappers = new Object2ObjectOpenHashMap<>();

        List<Remapper> itemRemappers = new ObjectArrayList<>();
        /* It is too big to use ImmutableList.of(). Add each manually. */
        itemRemappers.add(
                // Remap Deprecated Items
                new Remapper(rl -> (deprecationRemap.containsKey(rl)),
                        rl -> deprecationRemap.get(rl))
        );
        itemRemappers.add(
                // Remap Content Tweaker Items
                new Remapper(rl -> (rl.getNamespace().equals(CONTENTTWEAKER_MODID)),
                        rl -> LabsNames.makeLabsName(rl.getPath()))
        );
        itemRemappers.add(
                /*
                 * Remap old DevTech perfect gem to new perfect gem.
                 * DevTech did this badly, and created a MetaPrefixItem with GT's material registry but their
                 * Mod ID, so we need to map the DevTech metaitem to the GT metaitem.
                 */
                new Remapper(rl -> (rl.getNamespace().equals(DEVTECH_MODID) && rl.getPath().equals("meta_gem_perfect")),
                        rl -> GTUtility.gregtechId(rl.getPath()))
        );
        itemRemappers.add(
                /*
                 * Remap old Meta Blocks. This remaps all meta blocks from old crafttweaker materials, to new nomi labs ones.
                 * Because the old id was + 32000, and the base id (the number after `meta_block_.*_`) is id / 16,
                 * we can just decrease 2000 from that to get the new base id.
                 *
                 * The meta does not need to be changed, as id % 16 stays the same (32000 is thankfully divisible by 16)
                 */
                metaBlockRemapper
        );
        remappers.put(RemapTypes.ITEM, itemRemappers);

        remappers.put(RemapTypes.BLOCK, ImmutableList.of(
                // Remap Content Tweaker Blocks
                new Remapper(rl -> (rl.getNamespace().equals(LabsValues.CONTENTTWEAKER_MODID)),
                        rl -> LabsNames.makeLabsName(rl.getPath())),

                /*
                 * Remap old Meta Blocks. This remaps all meta blocks from old crafttweaker materials, to new nomi labs ones.
                 * Because the old id was + 32000, and the base id (the number after `meta_block_.*_`) is id / 16,
                 * we can just decrease 2000 from that to get the new base id.
                 *
                 * The meta does not need to be changed, as id % 16 stays the same (32000 is thankfully divisible by 16)
                 */
                metaBlockRemapper
        ));
    }

    private static void parseIgnoreConfigs() {
        ignorePtns = new Object2ObjectOpenHashMap<>();
        ignorePtns.put(RemapTypes.ITEM, parseIgnoreConfig(LabsConfig.advanced.ignoreItems, RemapTypes.ITEM));
        ignorePtns.put(RemapTypes.BLOCK, parseIgnoreConfig(LabsConfig.advanced.ignoreBlocks, RemapTypes.BLOCK));
        ignorePtns.put(RemapTypes.ENTITY, parseIgnoreConfig(LabsConfig.advanced.ignoreEntities, RemapTypes.ENTITY));
        ignorePtns.put(RemapTypes.BIOME, parseIgnoreConfig(LabsConfig.advanced.ignoreBiomes, RemapTypes.BIOME));
    }

    private static List<Pattern> parseIgnoreConfig(String[] patterns, RemapTypes type) {
        List<Pattern> ignored = new ArrayList<>();
        for (var pattern : patterns) {
            try {
                ignored.add(Pattern.compile(pattern));
            } catch (PatternSyntaxException e) {
                NomiLabs.LOGGER.error("Bad Syntax for Pattern: {} (Type {})", pattern, type);
                NomiLabs.LOGGER.throwing(e);
            }
        }
        return ignored;
    }

    private static void helpersInit() {
        deprecationRemap = new Object2ObjectOpenHashMap<>();
        /* It is too big to use ImmutableMap.of(). Add each manually. */
        deprecationRemap.put(
                new ResourceLocation(LabsValues.CONTENTTWEAKER_MODID, "omnicoin"),
                LabsNames.makeLabsName("nomicoin")
        );
        deprecationRemap.put(
                new ResourceLocation(LabsValues.CONTENTTWEAKER_MODID, "omnicoin5"),
                LabsNames.makeLabsName("nomicoin5")
        );
        deprecationRemap.put(
                new ResourceLocation(LabsValues.CONTENTTWEAKER_MODID, "omnicoin25"),
                LabsNames.makeLabsName("nomicoin25")
        );
        deprecationRemap.put(
                new ResourceLocation(LabsValues.CONTENTTWEAKER_MODID, "omnicoin100"),
                LabsNames.makeLabsName("nomicoin100")
        );
        deprecationRemap.put(
                new ResourceLocation(LabsValues.CONTENTTWEAKER_MODID,"blazepowder"),
                new ResourceLocation(MC_MODID, "blaze_powder")
        );
        deprecationRemap.put(
                new ResourceLocation(LabsValues.CONTENTTWEAKER_MODID, "dark_red_coal"),
                new ResourceLocation(XU2_MODID, "ingredients")
        );

        metaBlockRemapper = new Remapper(
                (rl) -> getMetaBlockID(rl) >= LabsRemapHelper.MIN_META_BLOCK_BASE_ID,
                (rl) -> {
                    var split = rl.getPath().split("_");
                    split[split.length - 1] = String.valueOf(getMetaBlockID(rl) - LabsRemapHelper.MIN_META_BLOCK_BASE_ID);
                    return LabsNames.makeLabsName(String.join("_", split));
                }
        );
    }

    private static int getMetaBlockID(ResourceLocation rl) {
        if (!LabsRemapHelper.META_BLOCK_MATCHER.matcher(rl.getPath()).matches())
            return 0; // Return no id, as it is not a meta block
        var split = rl.getPath().split("_");
        return Integer.parseInt(split[split.length - 1]);
    }

    public static <T extends IForgeRegistryEntry<T>> void remapAndIgnoreEntries(RegistryEvent.MissingMappings<T> event, RemapTypes type) {
        var mappings = event.getAllMappings();
        var remap = remappers.get(type);
        var ptns = ignorePtns.get(type);

        // How this works:
        // This is an array with size of all the mappings. Each index in the mappings list corresponds to an index in this array.
        // If the value of the index in this array is false, the entry is ignored. If it is true, the entry is not ignored.
        // Not using `getAction()`, as that is marked for internal use.
        // By default, all values in the array are false (default array value)
        boolean[] ignores = new boolean[mappings.size()];

        var needsRemap = false;

        if (remap == null && ptns == null) return;

        // Handle Ignore
        for (int i = 0; i < mappings.size(); i++) {
            var entry = mappings.get(i);
            if (ptns != null) {
                for (var pattern : ptns) {
                    if (!pattern.matcher(entry.key.toString()).matches()) continue;

                    entry.ignore();
                    NomiLabs.LOGGER.debug("Ignoring Resource Location {} (Type {})", entry.key, type);
                    ignores[i] = true;
                    break;
                }
            }
            if (ignores[i] || needsRemap || remap == null) continue; // Only check if remap is needed if it is not already set to true
            for (var remapper : remap) {
                if (remapper.shouldNotRemap(entry.key)) continue;

                needsRemap = true;
                break;
            }
        }

        if (!needsRemap) return; // Don't set checked to true, even if it is false, other remappers might detect needed remaps. Also returns if remap is null (because needRemap is never changed if remap is null)

        // Check with user
        if (!checked && !DataFixerHandler.checked) {
            var message = new StringBuilder("This world must be remapped.\n\n")
                    .append(TextFormatting.BOLD).append("A Backup will be made.\n")
                    .append("Pressing 'No' will cancel world loading.\n\n")
                    .append(TextFormatting.RED)
                    .append("Note that after the world is loaded with this, you CANNOT undo this!\n")
                    .append("You WILL have to load from the backup in order to load in a previous version!");

            if (!StartupQuery.confirm(message.toString())) {
                LabsRemapHelper.abort();
            }

            LabsRemapHelper.createWorldBackup();

            checked = true;
        }

        // Remap
        for (int i = 0; i < mappings.size(); i++) {
            if (ignores[i]) continue;
            var entry = mappings.get(i);
            for (var remapper : remap) {
                if (remapper.shouldNotRemap(entry.key)) continue;
                var oldRl = entry.key;
                NomiLabs.LOGGER.debug("Mapping Resource Location {}... (Type {})", oldRl, type);
                var newRl = remapper.remapEntry(entry, type);
                if (newRl == null)
                    NomiLabs.LOGGER.error("Failed to Map Resource Location {}! (Type {})", oldRl, type);
                else
                    NomiLabs.LOGGER.debug("Mapped Resource Location {} to {}! (Type {})", oldRl, newRl, type);
                break;
            }
        }
    }
}
