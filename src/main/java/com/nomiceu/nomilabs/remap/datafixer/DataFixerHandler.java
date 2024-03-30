package com.nomiceu.nomilabs.remap.datafixer;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.nomiceu.nomilabs.LabsValues;
import com.nomiceu.nomilabs.NomiLabs;
import com.nomiceu.nomilabs.remap.LabsRemapHelper;
import com.nomiceu.nomilabs.remap.datafixer.fixes.BlockFixer;
import com.nomiceu.nomilabs.remap.datafixer.fixes.ItemFixer;
import com.nomiceu.nomilabs.remap.datafixer.fixes.TileEntityFixer;
import com.nomiceu.nomilabs.remap.datafixer.types.LabsFixTypes;
import com.nomiceu.nomilabs.remap.datafixer.walker.BlockEntityWalker;
import com.nomiceu.nomilabs.remap.datafixer.walker.ChunkWalker;
import com.nomiceu.nomilabs.remap.datafixer.walker.ItemStackWalker;
import io.sommers.packmode.PMConfig;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.block.Block;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IFixType;
import net.minecraft.world.storage.SaveHandler;
import net.minecraftforge.common.util.CompoundDataFixer;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.ModFixs;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;
import org.apache.commons.lang3.StringUtils;
import org.apache.groovy.util.Arrays;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.nomiceu.nomilabs.remap.LabsMessageHelper.*;

/**
 * The main handler for all Data Fixes.
 * <p>
 * Values and Fixes are defined in {@link LabsFixes}.
 */
public class DataFixerHandler {
    public static LabsWorldFixData worldSavedData = null;
    public static boolean checked = false;

    /* Fixes that should be applied */
    public static Map<IFixType, List<DataFix<?>>> neededFixes;

    /* Fixes that should be logged */
    public static Map<IFixType, List<DataFix<?>>> neededNewFixes;

    /* Whether Mode is needed for New Fixes */
    public static boolean modeNeeded = false;

    private static Map<String, String> mods;
    private static BiMap<Integer, ResourceLocation> blockHelperMap;

    public static void preInit() {
        CompoundDataFixer fmlFixer = FMLCommonHandler.instance().getDataFixer();

        var itemWalker = new ItemStackWalker();
        fmlFixer.registerVanillaWalker(FixTypes.ENTITY, itemWalker);
        fmlFixer.registerVanillaWalker(FixTypes.PLAYER, itemWalker);
        fmlFixer.registerVanillaWalker(LabsFixTypes.WalkerTypes.ENDER_STORAGE, itemWalker);

        fmlFixer.registerVanillaWalker(FixTypes.BLOCK_ENTITY, new BlockEntityWalker(itemWalker));

        fmlFixer.registerVanillaWalker(FixTypes.CHUNK, new ChunkWalker());

        // Fixers
        ModFixs fixs = fmlFixer.init(LabsValues.LABS_MODID, LabsFixes.CURRENT);
        fixs.registerFix(LabsFixTypes.FixerTypes.ITEM, new ItemFixer());
        fixs.registerFix(LabsFixTypes.FixerTypes.CHUNK, new BlockFixer());
        fixs.registerFix(LabsFixTypes.FixerTypes.TILE_ENTITY, new TileEntityFixer());
    }

    public static void onWorldLoad(SaveHandler save) {
        checked = false;
        modeNeeded = false;
        neededFixes = null;
        neededNewFixes = null;
        NomiLabs.LOGGER.info("Checking Data Fixers...");

        getModList(save);
        if (mods.isEmpty()) return;

        LabsFixes.init();
        neededFixes = new Object2ObjectOpenHashMap<>();
        for (var fixType : LabsFixes.fixes.keySet()) {
            for (var fix : LabsFixes.fixes.get(fixType)) {
                if (fix.validModList.apply(mods)) {
                    if (!neededFixes.containsKey(fixType)) neededFixes.put(fixType, new ObjectArrayList<>());
                    neededFixes.get(fixType).add(fix);
                }
            }
        }

        var mapFile = save.getMapFileFromName(LabsFixes.DATA_NAME);

        if (mapFile.exists()) {
            DataFixerHandler.worldSavedData = LabsWorldFixData.load(mapFile);

            // Shortcut: If saved version == Current Version, Exit
            if (DataFixerHandler.worldSavedData.savedVersion == LabsFixes.CURRENT) {
                DataFixerHandler.worldSavedData = null;
                NomiLabs.LOGGER.info("This world's data version is up to date.");
                return;
            }
            NomiLabs.LOGGER.info("This world's data version needs updating. New Version: {}.", LabsFixes.CURRENT);
        } else {
            DataFixerHandler.worldSavedData = new LabsWorldFixData();
            NomiLabs.LOGGER.info("This world was saved without a data version. New Version: {}.", LabsFixes.CURRENT);
        }

        determineNeededFixesAndLog();

        // Clear Block Helper Map, the ids are different for some saves
        blockHelperMap = null;

        if (neededNewFixes.isEmpty()) {
            NomiLabs.LOGGER.info("This world does not need any new data fixers, but it has no saved version, it is old, or this is a new world.");
            LabsWorldFixData.save(mapFile, DataFixerHandler.worldSavedData);
            return;
        }

        sendMessage(
                MessageType.CONFIRM,
                Arrays.concat(Components.getIntro(), Components.getIntroAddition())
        );

        if (modeNeeded) {
            sendMessage(
                    MessageType.CONFIRM,
                    Components.getModeCheck(StringUtils.capitalize(PMConfig.getPackMode()))
            );
        }

        sendMessage(
                MessageType.NOTIFY,
                Components.getDoNotExit()
        );

        checked = true;

        LabsRemapHelper.createWorldBackup();

        // No need to increment version, the fix version, not the stored version, is saved
        // Still need to call as otherwise it isn't actually changed
        LabsWorldFixData.save(mapFile, DataFixerHandler.worldSavedData);
    }

    private static void getModList(SaveHandler save) {
        mods = new HashMap<>();

        File levelDat = new File(save.getWorldDirectory(), "level.dat");

        // If level.dat file does not exist, return.
        // This normally means it is a new world.
        // Sometimes the level.dat file is created first, but usually this runs after it is created.
        // If the level.dat file is created first, its mod list is equal to the current one.
        if (!levelDat.exists())
            return;

        NBTTagList modList;
        try {
            NBTTagCompound nbt = CompressedStreamTools.readCompressed(new FileInputStream(levelDat));
            if (!nbt.hasKey("FML", Constants.NBT.TAG_COMPOUND)) return;
            NBTTagCompound fml = nbt.getCompoundTag("FML");
            if (!fml.hasKey("ModList", Constants.NBT.TAG_LIST)) return;
            modList = fml.getTagList("ModList", Constants.NBT.TAG_COMPOUND);
        } catch (IOException e) {
            NomiLabs.LOGGER.fatal("Failed to read level.dat.", e);
            return;
        }
        for (var mod : modList) {
            if (!(mod instanceof NBTTagCompound compound)) continue;
            if (!compound.hasKey("ModId", Constants.NBT.TAG_STRING) || !compound.hasKey("ModVersion", Constants.NBT.TAG_STRING))
                continue;
            mods.put(compound.getString("ModId"), compound.getString("ModVersion"));
        }
    }

    private static void determineNeededFixesAndLog() {
        neededNewFixes = new Object2ObjectOpenHashMap<>();

        if (mods.isEmpty()) return;

        // If Nomi Labs Version is same as current version, exit.
        // This normally means it is a new world.
        // Sometimes the level.dat file is created first, but usually this runs after it is created.
        // If the level.dat file is created first, its mod list is equal to the current one.
        if (mods.containsKey(LabsValues.LABS_MODID) && mods.get(LabsValues.LABS_MODID).equals(LabsValues.LABS_VERSION))
            return;

        DataFixerHandler.worldSavedData.processModList(mods);

        NomiLabs.LOGGER.info("NEEDED DATA FIXES: ----------------------------------------");
        for (var fixType : LabsFixes.fixes.keySet()) {
            NomiLabs.LOGGER.info("SECTION: {} -------------------------------------------", fixType);
            var fixes = LabsFixes.fixes.get(fixType);
            for (var fix : fixes) {
                if (fix.validVersion.apply(DataFixerHandler.worldSavedData.savedVersion) && fix.validModList.apply(mods)) {
                    if (!neededNewFixes.containsKey(fixType)) neededNewFixes.put(fixType, new ObjectArrayList<>());
                    neededNewFixes.get(fixType).add(fix);
                    if (fix.needsMode) modeNeeded = true;
                    NomiLabs.LOGGER.info("- {}: {}", fix.name, fix.description);
                }
            }
            NomiLabs.LOGGER.info("END SECTION: {}. SEE ABOVE. ^^^^^^^^^^^^^^^^^^^^^^^^^^^", fixType);
        }
        NomiLabs.LOGGER.info("END NEEDED DATA FIXES. SEE ABOVE. ^^^^^^^^^^^^^^^^^^^^^^^^^^");
    }

    /**
     * The Block Helper Map is produced once needed, instead of in World Load or Post Init.<br>
     * This means they are loaded after block id mismatch fixes, so the ids are correct to the world.
     */
    public static BiMap<Integer, ResourceLocation> getBlockHelperMap() {
        if (blockHelperMap != null) return blockHelperMap;

        ForgeRegistry<Block> registry = (ForgeRegistry<Block>) ForgeRegistries.BLOCKS;
        blockHelperMap = HashBiMap.create(registry.getKeys().stream()
                .collect(Collectors.toMap(registry::getID, Function.identity())));

        NomiLabs.LOGGER.debug("Generated Block Helper Map!");
        NomiLabs.LOGGER.debug(blockHelperMap);
        return blockHelperMap;
    }

    public static void processEnderStorageInfo(DataFixer fixer, SaveHandler save) {
        NomiLabs.LOGGER.info("Processing Ender Storage Info...");

        // Ender Storage Fixes
        // Ender Storage Stores Data in data1.dat and data2.dat. Sometimes its only data1.dat.
        // lock.dat stores a byte, which has weird bitwise stuff done to it, to determine which file (data1.dat or data2.dat) to read.
        var enderStorageDir = new File(save.getWorldDirectory(), "EnderStorage");
        String[] processFiles = new String[]{"data1.dat", "data2.dat"};
        for (var toProcess : processFiles) {
            File processFile = new File(enderStorageDir, toProcess);
            if (!processFile.isFile()) continue; // Also checks if it exists
            try {
                var fileNbt = CompressedStreamTools.readCompressed(new FileInputStream(processFile));
                var newNbt = fixer.process(LabsFixTypes.WalkerTypes.ENDER_STORAGE, fileNbt);

                var fileOut = new FileOutputStream(processFile);
                CompressedStreamTools.writeCompressed(newNbt, fileOut);
                NomiLabs.LOGGER.info("Successfully wrote {} Ender Storage Save Data!", toProcess);
            } catch (IOException e) {
                throw new IllegalStateException("Failed to read or write Ender Storage save data!", e);
            }
        }
        NomiLabs.LOGGER.info("Finished Processing Ender Storage Info!");
    }

    public static boolean hasNoNewFixes() {
        return worldSavedData == null || neededNewFixes == null || neededNewFixes.isEmpty();
    }

    public static boolean fixNotAvailable() {
        return neededFixes == null || neededFixes.isEmpty();
    }

    public static void close() {
        worldSavedData = null;
        checked = false;
        neededFixes = null;
        neededNewFixes = null;
    }
}
