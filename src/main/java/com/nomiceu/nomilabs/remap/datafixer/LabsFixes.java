package com.nomiceu.nomilabs.remap.datafixer;

import com.nomiceu.nomilabs.config.LabsConfig;
import com.nomiceu.nomilabs.remap.datafixer.storage.ItemStackLike;
import io.sommers.packmode.PMConfig;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.shorts.Short2ShortLinkedOpenHashMap;
import net.minecraft.util.ResourceLocation;

import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

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
     * data mixer must be applied.
     */
    public static final int FIX_VERSION = 2;

    /**
     * Default version, used if save doesn't have a fix version, or if something goes wrong
     */
    public static final int DEFAULT_VERSION = 0;

    // TODO Min Version Needed
    public static Map<Function<ItemStackLike, Boolean>, Consumer<ItemStackLike>> itemFixes;

    public static Map<Function<ResourceLocation, Boolean>, Supplier<ResourceLocation>> multiblockFixes;

    public static Map<Short, Short> multiblockMetaRemap;

    public static void init() {
        multiblockMetaRemap = new Short2ShortLinkedOpenHashMap();
        multiblockMetaRemap.put((short) 32000, (short) 32100); // Microverse 1
        multiblockMetaRemap.put((short) 32001, (short) 32101); // Microverse 2
        multiblockMetaRemap.put((short) 32002, (short) 32102); // Microverse 3
        if (PMConfig.getPackMode().equals(NORMAL_MODE)) {
            multiblockMetaRemap.put((short) 32003, (short) 32103); // Creative Tank Provider
            multiblockMetaRemap.put((short) 32004, (short) 32104); // Naq Reactor 1
            multiblockMetaRemap.put((short) 32005, (short) 32105); // Naq Reactor 2
            multiblockMetaRemap.put((short) 3100, (short) 32108); // DME Sim Chamber
        }
        // In case it is some other mode, check if it is expert
        if (PMConfig.getPackMode().equals(EXPERT_MODE)) {
            multiblockMetaRemap.put((short) 32003, (short) 32104); // Naq Reactor 1
            multiblockMetaRemap.put((short) 32004, (short) 32105); // Naq Reactor 2
            multiblockMetaRemap.put((short) 32005, (short) 32106); // Actualization Chamber
            multiblockMetaRemap.put((short) 32006, (short) 32107); // Universal Crystallizer
        }

        itemFixes = new Object2ObjectLinkedOpenHashMap<>();

        itemFixes.put(
                (stack) -> stack.rl.equals(new ResourceLocation(CONTENTTWEAKER_MODID, "dark_red_coal")),
                (stack) -> stack.setRl(new ResourceLocation(XU2_MODID, "ingredients")).setMeta((short) 4));

        if (LabsConfig.modIntegration.enableExtraUtils2Integration)
            itemFixes.put(
                    (stack) -> stack.rl.equals(new ResourceLocation(XU2_MODID, "ingredients"))
                            && stack.tag != null && stack.tag.hasKey("Freq"),
                    (stack) -> {
                        var tag = Objects.requireNonNull(stack.tag);
                        tag.removeTag("Freq");
                        stack.setTag(tag.isEmpty() ? null : tag);
                    }
            );

        itemFixes.put(
                (stack) -> stack.rl.equals(new ResourceLocation("gregtech:machine")) && multiblockMetaRemap.containsKey(stack.meta),
                (stack) -> stack.meta = multiblockMetaRemap.get(stack.meta)
        );

        multiblockFixes = new Object2ObjectLinkedOpenHashMap<>();

        multiblockFixes.put(
                (id) -> id.equals(new ResourceLocation(MBT_MODID, "microverse_projector_basic")), () -> makeLabsName("microverse_projector_1")
        );
        multiblockFixes.put(
                (id) -> id.equals(new ResourceLocation(MBT_MODID, "microverse_projector_advanced")), () -> makeLabsName("microverse_projector_2")
        );
        multiblockFixes.put(
                (id) -> id.equals(new ResourceLocation(MBT_MODID, "microverse_projector_advanced_ii")), () -> makeLabsName("microverse_projector_3")
        );
        multiblockFixes.put(
                (id) -> id.equals(new ResourceLocation(MBT_MODID, "creative_tank_provider")), () -> makeLabsName("creative_tank_provider")
        );
        multiblockFixes.put(
                (id) -> id.equals(new ResourceLocation(MULTIBLOCK_TWEAKER_MODID, "naquadah_reactor_1")), () -> makeLabsName("naquadah_reactor_1")
        );
        multiblockFixes.put(
                (id) -> id.equals(new ResourceLocation(MULTIBLOCK_TWEAKER_MODID, "naquadah_reactor_2")), () -> makeLabsName("naquadah_reactor_2")
        );
        multiblockFixes.put(
                (id) -> id.equals(new ResourceLocation(MULTIBLOCK_TWEAKER_MODID, "actualization_chamber")), () -> makeLabsName("actualization_chamber")
        );
        multiblockFixes.put(
                (id) -> id.equals(new ResourceLocation(MULTIBLOCK_TWEAKER_MODID, "universal_crystallizer")), () -> makeLabsName("universal_crystallizer")
        );
        multiblockFixes.put(
                (id) -> id.equals(new ResourceLocation(MULTIBLOCK_TWEAKER_MODID, "dml_sim_chamber")), () -> makeLabsName("dme_sim_chamber")
        );
    }
}
