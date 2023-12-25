package com.nomiceu.nomilabs.remap.datafixer;

import com.nomiceu.nomilabs.LabsValues;
import com.nomiceu.nomilabs.config.LabsConfig;
import com.nomiceu.nomilabs.remap.datafixer.types.LabsFixTypes;
import com.nomiceu.nomilabs.util.LabsModeHelper;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.shorts.Short2ShortLinkedOpenHashMap;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.nomiceu.nomilabs.LabsValues.*;
import static com.nomiceu.nomilabs.util.LabsNames.*;

/**
 * Definitions for all values, and all data fixes.
 */
public class LabsFixes {
    /**
     * The name used to store fix data. Do not change this, although changing it will FORCE all fixes to be applied.
     * Do this via another way.
     */
    public static final String DATA_NAME = LABS_MODID + ".fix_data";

    /**
     * In which nbt key the data is stored.Do not change this, although changing it will FORCE all fixes to be applied.
     * Do this via another way. (This is better than changing the data name)
     */
    public static final String DATA_KEY = "LabsFixer";

    /**
     * The current data format version. Increment this when breaking changes are made and the
     * data mixer must be applied. If this is not incremented, nothing will be applied.
     */
    public static final int FIX_VERSION = 3;

    /**
     * Default version, used if save doesn't have a fix version, or if something goes wrong
     */
    public static final int DEFAULT_VERSION = 0;


    public static List<DataFix.ItemFix> itemFixes;

    public static List<DataFix.TileEntityFix> tileEntityFixes;

    public static Map<LabsFixTypes.FixerTypes, List<? extends DataFix<?>>> fixes;

    public static Map<ResourceLocation, ResourceLocation> multiblockMetaIdRemap;
    public static Map<Short, Short> multiblockMetaRemap;

    public static void init() {
        helperMapsInit();

        itemFixes = new ObjectArrayList<>();

        itemFixes.add(
                new DataFix.ItemFix("Dark Red Coal Remap",
                        "Correctly remaps Content Tweaker Dark Red Coal to XU2 Red Coal.",
                        false,
                        (version) -> version <= DEFAULT_VERSION,
                        (compound) -> true,
                        (stack) -> stack.rl.equals(new ResourceLocation(CONTENTTWEAKER_MODID, "dark_red_coal")),
                        (stack) -> stack.setRl(new ResourceLocation(XU2_MODID, "ingredients")).setMeta((short) 4))
        );

        if (LabsConfig.modIntegration.enableExtraUtils2Integration)
            itemFixes.add(
                    new DataFix.ItemFix("XU2 Frequency Removal",
                            "Removes Frequency from XU2 Ingredients.",
                            false,
                            (version) -> version <= DEFAULT_VERSION,
                            (compound) -> true,
                            (stack) -> stack.rl.equals(new ResourceLocation(XU2_MODID, "ingredients"))
                                    && stack.tag != null && stack.tag.hasKey("Freq"),
                            (stack) -> {
                                var tag = Objects.requireNonNull(stack.tag);
                                tag.removeTag("Freq");
                                stack.setTag(tag.isEmpty() ? null : tag);
                            })
            );

        itemFixes.add(
                new DataFix.ItemFix("Old Multiblock Metadata Remap",
                        "Remaps old Multiblock Metadata to the new format.",
                        true,
                        (version) -> version <= DEFAULT_VERSION,
                        (compound) -> true,
                        (stack) -> stack.rl.equals(new ResourceLocation("gregtech:machine")) && multiblockMetaRemap.containsKey(stack.meta),
                        (stack) -> stack.meta = multiblockMetaRemap.get(stack.meta))
        );

        tileEntityFixes = new ObjectArrayList<>();
        tileEntityFixes.add(
                new DataFix.TileEntityFix("Multiblock Tile Entity MetaId Remap",
                        "Remaps old Multiblock Tile Entity Names to the new format.",
                        false,
                        (version) -> version <= DEFAULT_VERSION,
                        (compound) -> true,
                        (compound) -> compound.hasKey("MetaId", Constants.NBT.TAG_STRING) && compound.hasKey("id", Constants.NBT.TAG_STRING) &&
                                compound.getString("id").equals(new ResourceLocation(LabsValues.GREGTECH_MODID, LabsValues.GT_MACHINE_PATH).toString()) &&
                                multiblockMetaIdRemap.containsKey(new ResourceLocation(compound.getString("MetaId"))),
                        (compound -> compound.setString("MetaId", multiblockMetaIdRemap.get(new ResourceLocation(compound.getString("MetaId"))).toString())))
        );

        fixes = new Object2ObjectOpenHashMap<>();
        fixes.put(LabsFixTypes.FixerTypes.ITEM, itemFixes);
        fixes.put(LabsFixTypes.FixerTypes.TILE_ENTITY, tileEntityFixes);
    }

    private static void helperMapsInit() {
        multiblockMetaRemap = new Short2ShortLinkedOpenHashMap();
        multiblockMetaRemap.put((short) 32000, (short) 32100); // Microverse 1
        multiblockMetaRemap.put((short) 32001, (short) 32101); // Microverse 2
        multiblockMetaRemap.put((short) 32002, (short) 32102); // Microverse 3
        if (LabsModeHelper.isNormal()) {
            multiblockMetaRemap.put((short) 32003, (short) 32103); // Creative Tank Provider
            multiblockMetaRemap.put((short) 32004, (short) 32104); // Naq Reactor 1
            multiblockMetaRemap.put((short) 32005, (short) 32105); // Naq Reactor 2
            multiblockMetaRemap.put((short) 3100, (short) 32108); // DME Sim Chamber
        }
        // In case it is some other mode, check if it is expert. This is only done here, as this specifically modifies data.
        if (LabsModeHelper.isExpert()) {
            multiblockMetaRemap.put((short) 32003, (short) 32104); // Naq Reactor 1
            multiblockMetaRemap.put((short) 32004, (short) 32105); // Naq Reactor 2
            multiblockMetaRemap.put((short) 32005, (short) 32106); // Actualization Chamber
            multiblockMetaRemap.put((short) 32006, (short) 32107); // Universal Crystallizer
        }

        multiblockMetaIdRemap = new Object2ObjectLinkedOpenHashMap<>();
        multiblockMetaIdRemap.put(
                new ResourceLocation(MBT_MODID, "microverse_projector_basic"), makeLabsName("microverse_projector_2")
        );
        multiblockMetaIdRemap.put(
                new ResourceLocation(MBT_MODID, "microverse_projector_advanced"), makeLabsName("microverse_projector_2")
        );
        multiblockMetaIdRemap.put(
                new ResourceLocation(MBT_MODID, "microverse_projector_advanced_ii"), makeLabsName("microverse_projector_3")
        );
        multiblockMetaIdRemap.put(
                new ResourceLocation(MBT_MODID, "creative_tank_provider"), makeLabsName("creative_tank_provider")
        );
        multiblockMetaIdRemap.put(
                new ResourceLocation(MULTIBLOCK_TWEAKER_MODID, "naquadah_reactor_1"), makeLabsName("naquadah_reactor_1")
        );
        multiblockMetaIdRemap.put(
                new ResourceLocation(MULTIBLOCK_TWEAKER_MODID, "naquadah_reactor_2"), makeLabsName("naquadah_reactor_2")
        );
        multiblockMetaIdRemap.put(
                new ResourceLocation(MULTIBLOCK_TWEAKER_MODID, "actualization_chamber"), makeLabsName("actualization_chamber")
        );
        multiblockMetaIdRemap.put(
                new ResourceLocation(MULTIBLOCK_TWEAKER_MODID, "universal_crystallizer"), makeLabsName("universal_crystallizer")
        );
        multiblockMetaIdRemap.put(
                new ResourceLocation(MULTIBLOCK_TWEAKER_MODID, "dml_sim_chamber"), makeLabsName("dme_sim_chamber")
        );
    }
}
