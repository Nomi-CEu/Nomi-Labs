package com.nomiceu.nomilabs.remap.datafixer;

import com.nomiceu.nomilabs.LabsValues;
import com.nomiceu.nomilabs.remap.datafixer.fixes.MultiblockFixer;
import com.nomiceu.nomilabs.remap.datafixer.fixes.ItemFixer;
import com.nomiceu.nomilabs.remap.datafixer.types.LabsFixTypes;
import com.nomiceu.nomilabs.remap.datafixer.walker.ItemStackWalker;
import com.nomiceu.nomilabs.mixin.WorldLoadHandler;
import net.minecraft.util.datafix.FixTypes;
import net.minecraftforge.common.util.CompoundDataFixer;
import net.minecraftforge.common.util.ModFixs;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.StartupQuery;

/**
 * The main handler for all Data Fixes.
 * <p>
 * World Saved Data is initialized in {@link WorldLoadHandler}, as
 * {@link StartupQuery#abort()} breaks sometimes if called here.
 * <p>
 * Values and Fixes are defined in {@link LabsFixes}.
 */
public class DataFixerHandler {
    public static LabsWorldFixData worldSavedData = null;

    public static void init() {
        LabsFixes.init();
        CompoundDataFixer fmlFixer = FMLCommonHandler.instance().getDataFixer();

        // Item Stack Walker
        // itemStackWalker = new ItemStackWalker();
        fmlFixer.registerVanillaWalker(FixTypes.BLOCK_ENTITY, new ItemStackWalker(FixTypes.BLOCK_ENTITY.name()));
        fmlFixer.registerVanillaWalker(FixTypes.ENTITY, new ItemStackWalker(FixTypes.ENTITY.name()));
        fmlFixer.registerVanillaWalker(FixTypes.PLAYER, new ItemStackWalker(FixTypes.PLAYER.name()));
        fmlFixer.registerVanillaWalker(LabsFixTypes.WalkerTypes.ENDER_STORAGE, new ItemStackWalker(LabsFixTypes.WalkerTypes.ENDER_STORAGE.name()));

        // Fixers
        ModFixs fixs = fmlFixer.init(LabsValues.LABS_MODID, LabsFixes.FIX_VERSION);
        fixs.registerFix(LabsFixTypes.FixerTypes.ITEM, new ItemFixer());
        fixs.registerFix(FixTypes.BLOCK_ENTITY, new MultiblockFixer());
    }

    public static boolean fixNotAvailable() {
        return worldSavedData == null;
    }

    public static void close() {
        worldSavedData = null;
    }
}
