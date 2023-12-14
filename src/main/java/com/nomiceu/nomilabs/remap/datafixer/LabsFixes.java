package com.nomiceu.nomilabs.remap.datafixer;

import com.nomiceu.nomilabs.LabsValues;
import com.nomiceu.nomilabs.config.LabsConfig;
import com.nomiceu.nomilabs.remap.datafixer.storage.ItemStackLike;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.util.ResourceLocation;

import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Definitions for all values, and all data fixes.
 */
public class LabsFixes {
    /**
     * The name used to store fix data. Do not change this, although changing it will FORCE all fixes to be applied.
     * Do this via another way.
     */
    public static final String DATA_NAME = LabsValues.LABS_MODID + ".fix_data";

    /**
     * In which nbt key the data is stored.Do not change this, although changing it will FORCE all fixes to be applied.
     * Do this via another way. (This is better than changing the data name)
     */
    public static final String DATA_KEY = "LabsFixer";

    /**
     * The current data format version. Increment this when breaking changes are made and the
     * data mixer must be applied.
     */
    public static final int FIX_VERSION = 1;

    /**
     * Default version, used if save doesn't have a fix version, or if something goes wrong
     */
    public static final int DEFAULT_VERSION = 0;

    // TODO Min Version Needed
    public static Map<Function<ItemStackLike, Boolean>, Consumer<ItemStackLike>> itemFixes;
    //public static Map<Function<NBTTagCompound, Boolean>, Consumer<NBTTagCompound>> blockFixes; TODO

    public static void init() {
        itemFixes = new Object2ObjectLinkedOpenHashMap<>();

        itemFixes.put(
                (stack) -> stack.rl.equals(new ResourceLocation(LabsValues.CONTENTTWEAKER_MODID, "dark_red_coal")),
                (stack) -> stack.setRl(new ResourceLocation(LabsValues.XU2_MODID, "ingredients")).setMeta((short) 4));

        if (LabsConfig.modIntegration.enableExtraUtils2Integration)
            itemFixes.put(
                    (stack) -> stack.rl.equals(new ResourceLocation(LabsValues.XU2_MODID, "ingredients"))
                            && stack.tag != null && stack.tag.hasKey("Freq"),
                    (stack) -> {
                        var tag = Objects.requireNonNull(stack.tag);
                        tag.removeTag("Freq");
                        stack.setTag(tag.isEmpty() ? null : tag);
                    }
            );
    }
}
