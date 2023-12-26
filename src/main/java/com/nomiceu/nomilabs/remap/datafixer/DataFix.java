package com.nomiceu.nomilabs.remap.datafixer;

import com.nomiceu.nomilabs.remap.datafixer.storage.BlockStateLike;
import com.nomiceu.nomilabs.remap.datafixer.storage.ItemStackLike;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class DataFix<T> {
    public final String name;
    public final String description;
    public final boolean needsMode;
    public final Function<Integer, Boolean> validVersion;
    public final Function<Map<String, String>, Boolean> validModList;
    public final Function<T, Boolean> validEntry;
    public final Consumer<T> transform;

    public DataFix(String name, String description, boolean needsMode, Function<Integer, Boolean> validVersion, Function<Map<String, String>, Boolean> validModList, Function<T, Boolean> validEntry, Consumer<T> transform) {
        this.name = name;
        this.description = description;
        this.needsMode = needsMode;
        this.validVersion = validVersion;
        this.validModList = validModList;
        this.validEntry = validEntry;
        this.transform = transform;
    }

    public static class ItemFix extends DataFix<ItemStackLike> {
        public ItemFix(String name, String description, boolean needsMode, Function<Integer, Boolean> validVersion, Function<Map<String, String>, Boolean> validModList, Function<ItemStackLike, Boolean> validEntry, Consumer<ItemStackLike> transform) {
            super(name, description, needsMode, validVersion, validModList, validEntry, transform);
        }
    }

    public static class BlockFix extends DataFix<BlockStateLike> {
        public final ResourceLocation[] neededLocations;
        public BlockFix(String name, String description, boolean needsMode, Function<Integer, Boolean> validVersion, Function<Map<String, String>, Boolean> validModList, Function<BlockStateLike, Boolean> validEntry, Consumer<BlockStateLike> transform, ResourceLocation... neededLocations) {
            super(name, description, needsMode, validVersion, validModList, validEntry, transform);
            this.neededLocations = neededLocations;
        }
    }

    public static class TileEntityFix extends DataFix<NBTTagCompound> {
        public TileEntityFix(String name, String description, boolean needsMode, Function<Integer, Boolean> validVersion, Function<Map<String, String>, Boolean> validModList, Function<NBTTagCompound, Boolean> validEntry, Consumer<NBTTagCompound> transform) {
            super(name, description, needsMode, validVersion, validModList, validEntry, transform);
        }
    }
}
