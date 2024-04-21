package com.nomiceu.nomilabs.remap.datafixer.types;

import net.minecraft.util.datafix.IFixType;

public class LabsFixTypes {

    public enum FixerTypes implements IFixType {
        /**
         * Any compound tag that looks like an item stack.
         * That is to say, it has the fields {@code string id}, {@code byte Count}, and {@code short Damage}.
         */
        ITEM,

        /**
         * A compound tag belonging to a Tile Entity.
         */
        TILE_ENTITY,

        /**
         * Chunk Data.
         */
        CHUNK
    }

    public enum WalkerTypes implements IFixType {
        /**
         * Fix Type used by ItemStack Walker to fix Ender Storage Items
         */
        ENDER_STORAGE
    }
}
