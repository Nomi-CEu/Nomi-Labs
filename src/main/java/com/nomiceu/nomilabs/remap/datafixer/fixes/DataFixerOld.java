package com.nomiceu.nomilabs.remap.datafixer.fixes;

/*
import com.nomiceu.nomilabs.LabsValues;
import com.nomiceu.nomilabs.NomiLabs;
import com.nomiceu.nomilabs.config.LabsConfig;
import com.nomiceu.nomilabs.remap.datafixer.DataFixerHandler;
import com.nomiceu.nomilabs.remap.datafixer.LabsFixes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.IFixableData;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class DataFixerOld implements IFixableData {
    public static final int SHORT_ID = 2;
    public static final int STRING_ID = 8;
    public static final int COMPOUND_LIST_ID = 9;
    public static final int COMPOUND_ID = 10;

    public static final String ID_KEY = "id";
    public static final String DAMAGE_KEY = "Damage";
    public static final String TAG_KEY = "tag";

    public static Map<Function<NBTTagCompound, Boolean>, Consumer<NBTTagCompound>> lowImpactFixes;

    public DataFixerOld() {
        lowImpactFixes = new HashMap<>();
        lowImpactFixes.put(
                (tag) -> tag.getString(ID_KEY).equals(
                        new ResourceLocation(LabsValues.CONTENTTWEAKER_MODID, "dark_red_coal").toString()),
                (tag) -> {
                    tag.setString(ID_KEY, new ResourceLocation(LabsValues.XU2_MODID, "ingredients").toString());
                    tag.setShort(DAMAGE_KEY, (short) 4);
                }
        );
        if (LabsConfig.modIntegration.enableExtraUtils2Integration)
            lowImpactFixes.put(
                    // Remove frequency from ALL XU2 Ingredients
                    (tag) -> tag.getString(ID_KEY).equals(
                            new ResourceLocation(LabsValues.XU2_MODID, "ingredients").toString()) &&
                            tag.hasKey(TAG_KEY, COMPOUND_ID) &&
                            tag.getCompoundTag(TAG_KEY).hasKey("Freq"),
                    (tag) -> {
                        var stackTag = tag.getCompoundTag("tag");
                        stackTag.removeTag("Freq");
                        if (stackTag.isEmpty()) {
                            tag.removeTag(TAG_KEY);
                        } else {
                            tag.setTag(TAG_KEY, stackTag);
                        }
                    }
            );
    }

    @Override
    public int getFixVersion() {
        return LabsFixes.FIX_VERSION;
    }

    @Override
    public @NotNull NBTTagCompound fixTagCompound(@NotNull NBTTagCompound compound) {
        fixItemsInCompound(compound);
        return compound;
    }

    // Recursively travel through the entire NBT tree
    private void fixItemsInCompound(NBTTagCompound compound) {
        // If data fixing is disabled for the current world, skip
        if (DataFixerHandler.worldSavedData != null && !DataFixerHandler.worldSavedData.dataFixes) return;

        // If tag has both id of type string and Damage of type short
        // This is the safest and fastest but some rare mods might use a different name for id and/or Damage
        // if they do not use or extend the vanilla ItemStack class for writing NBT data,
        // in which case compound.toString().contains() and replace() could be used instead, but this would run
        // for all visited tree elements on all depths and it is probably too expensive
        if (compound.hasKey(ID_KEY, STRING_ID) && compound.hasKey(DAMAGE_KEY, SHORT_ID)) {
            var oldCompound = compound.copy();
            // Make replacements
            for (var fixDef : lowImpactFixes.keySet()) {
                if (fixDef.apply(compound)) {
                    var fix = lowImpactFixes.get(fixDef);
                    fix.accept(compound);
                    NomiLabs.LOGGER.info("[Data Fixer] Replaced {} with: {}", oldCompound, compound);
                    break;
                }
            }
        }

        // Recursive travel
        if (!compound.getKeySet().isEmpty()) {
            // If this compound has any elements, go through all of its compounds and compound lists
            compound.getKeySet().forEach(key -> {
                // Check if this element is an NBTCompound
                if (compound.hasKey(key, COMPOUND_ID)) {
                    fixItemsInCompound(compound.getCompoundTag(key));
                }

                // Check if this element is an NBTCompound List
                else if (compound.hasKey(key, COMPOUND_LIST_ID)) {
                    NBTTagList compoundList = compound.getTagList(key, COMPOUND_ID);
                    for (int i = 0; i < compoundList.tagCount(); i++) {
                        fixItemsInCompound(compoundList.getCompoundTagAt(i));
                    }
                }
            });
        }
    }
}

 */
