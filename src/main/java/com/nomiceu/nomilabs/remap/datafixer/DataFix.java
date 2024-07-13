package com.nomiceu.nomilabs.remap.datafixer;

import java.util.function.Consumer;
import java.util.function.Function;

import javax.annotation.Nullable;

import net.minecraft.nbt.NBTTagCompound;

import com.nomiceu.nomilabs.remap.datafixer.storage.BlockStateLike;
import com.nomiceu.nomilabs.remap.datafixer.storage.ItemStackLike;

public abstract class DataFix<T> {

    public final String name;
    public final String description;
    public final boolean needsMode;
    public final Function<Integer, Boolean> validVersion;
    public final Function<T, Boolean> validEntry;
    public final Consumer<T> transform;

    public DataFix(String name, String description, boolean needsMode, Function<Integer, Boolean> validVersion,
                   Function<T, Boolean> validEntry, Consumer<T> transform) {
        this.name = name;
        this.description = description;
        this.needsMode = needsMode;
        this.validVersion = validVersion;
        this.validEntry = validEntry;
        this.transform = transform;
    }

    public static class ItemFix extends DataFix<ItemStackLike> {

        public ItemFix(String name, String description, boolean needsMode, Function<Integer, Boolean> validVersion,
                       Function<ItemStackLike, Boolean> validEntry, Consumer<ItemStackLike> transform) {
            super(name, description, needsMode, validVersion, validEntry, transform);
        }
    }

    public static class BlockFix extends DataFix<BlockStateLike> {

        public final boolean teNeeded;
        @Nullable
        public final Function<BlockStateLike, Boolean> secondaryValidEntry;

        public BlockFix(String name, String description, boolean needsMode, Function<Integer, Boolean> validVersion,
                        boolean teNeeded, Function<BlockStateLike, Boolean> validEntry,
                        @Nullable Function<BlockStateLike, Boolean> secondaryValidEntry,
                        Consumer<BlockStateLike> blockTransform) {
            super(name, description, needsMode, validVersion, validEntry, blockTransform);
            this.secondaryValidEntry = secondaryValidEntry;
            this.teNeeded = teNeeded;
        }
    }

    public static class TileEntityFix extends DataFix<NBTTagCompound> {

        public TileEntityFix(String name, String description, boolean needsMode,
                             Function<Integer, Boolean> validVersion,
                             Function<NBTTagCompound, Boolean> validEntry, Consumer<NBTTagCompound> transform) {
            super(name, description, needsMode, validVersion, validEntry, transform);
        }
    }
}
