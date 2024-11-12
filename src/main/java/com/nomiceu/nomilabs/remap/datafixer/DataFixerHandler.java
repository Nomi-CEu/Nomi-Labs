package com.nomiceu.nomilabs.remap.datafixer;

import static com.nomiceu.nomilabs.remap.LabsMessageHelper.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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

import org.apache.groovy.util.Arrays;

import com.nomiceu.nomilabs.LabsValues;
import com.nomiceu.nomilabs.NomiLabs;
import com.nomiceu.nomilabs.config.LabsVersionConfig;
import com.nomiceu.nomilabs.mixinhelper.RemappableForgeRegistry;
import com.nomiceu.nomilabs.remap.LabsRemapHelper;
import com.nomiceu.nomilabs.remap.datafixer.fixes.BlockFixer;
import com.nomiceu.nomilabs.remap.datafixer.fixes.ItemFixer;
import com.nomiceu.nomilabs.remap.datafixer.fixes.TileEntityFixer;
import com.nomiceu.nomilabs.remap.datafixer.types.LabsFixTypes;
import com.nomiceu.nomilabs.remap.datafixer.walker.BlockEntityWalker;
import com.nomiceu.nomilabs.remap.datafixer.walker.ChunkWalker;
import com.nomiceu.nomilabs.remap.datafixer.walker.ItemStackWalker;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * The main handler for all Data Fixes.
 * <p>
 * Values and Fixes are defined in {@link LabsFixes}.
 */
public class DataFixerHandler {

    public static LabsWorldFixData worldSavedData = null;
    public static boolean checked = false;

    /* Fixes that should be logged */
    public static Map<IFixType, List<DataFix<?>>> neededNewFixes;

    /* Whether fix is available */
    private static boolean fixAvailable = false;

    private static String savedLabsVersion;

    /*
     * Must be split up so that idToBlockMap has remapped info
     * (Remapped Info from ForgeRegistry & Snapshot & Game Data Mixins)
     * Remapped contains the old id mapped to the new resource location (as specified by LabsRemappers)
     */
    private static Map<Integer, ResourceLocation> idToBlockMap;
    private static Map<ResourceLocation, Integer> blockToIdMap;

    public static void preInit() {
        CompoundDataFixer fmlFixer = FMLCommonHandler.instance().getDataFixer();

        var itemWalker = new ItemStackWalker();
        fmlFixer.registerVanillaWalker(FixTypes.ENTITY, itemWalker);
        fmlFixer.registerVanillaWalker(FixTypes.PLAYER, itemWalker);
        fmlFixer.registerVanillaWalker(LabsFixTypes.WalkerTypes.ENDER_STORAGE, itemWalker);

        fmlFixer.registerVanillaWalker(FixTypes.BLOCK_ENTITY, new BlockEntityWalker(itemWalker));

        fmlFixer.registerVanillaWalker(FixTypes.CHUNK, new ChunkWalker());

        // Fixers
        ModFixs fixs = fmlFixer.init(LabsValues.LABS_MODID, LabsRemapHelper.getReportedVersion());
        fixs.registerFix(LabsFixTypes.FixerTypes.ITEM, new ItemFixer());
        fixs.registerFix(LabsFixTypes.FixerTypes.CHUNK, new BlockFixer());
        fixs.registerFix(LabsFixTypes.FixerTypes.TILE_ENTITY, new TileEntityFixer());
    }

    public static void onWorldLoad(SaveHandler save) {
        checked = false;
        neededNewFixes = null;
        savedLabsVersion = null;
        fixAvailable = true;
        idToBlockMap = null;
        blockToIdMap = null;

        NomiLabs.LOGGER.info("Checking Data Fixers...");

        getInfoFromSave(save);

        LabsFixes.init();

        var mapFile = save.getMapFileFromName(LabsFixes.DATA_NAME);

        if (mapFile.exists()) {
            DataFixerHandler.worldSavedData = LabsWorldFixData.load(mapFile);

            // Check if manual fix version has been decremented
            // This could break things, so throw an exception
            if (LabsVersionConfig.manualFixVersion < DataFixerHandler.worldSavedData.savedManualFixVersion) {
                NomiLabs.LOGGER.fatal("Manual Data Fix Version has been decremented. This is not supported!");
                sendMessage(MessageType.NOTIFY, Components.getInvalidManualVersion());
                LabsRemapHelper.abort();
            }

            // No need to increment version, the fix version, not the stored version, is saved
            // Still need to call as otherwise it isn't actually changed
            LabsWorldFixData.save(mapFile, DataFixerHandler.worldSavedData);

            // Shortcut: If saved version == Current Version, Exit (No NEW Fixes)
            if (DataFixerHandler.worldSavedData.savedFixVersion == LabsFixes.CURRENT) {
                NomiLabs.LOGGER.info("This world's internal data version is up to date.");

                // Check only relevant if internal fix version hasn't changed
                // Else, manual version increment doesn't matter
                if (LabsVersionConfig.manualFixVersion != DataFixerHandler.worldSavedData.savedManualFixVersion) {
                    NomiLabs.LOGGER.info(
                            "The Manual Data Fix Version has been increased. New Version: {}. Enabling Data Fixers...",
                            LabsVersionConfig.manualFixVersion);
                }

                DataFixerHandler.worldSavedData = null;
                return;
            }
            NomiLabs.LOGGER.info("This world's internal data version needs updating. New Version: {}.",
                    LabsFixes.CURRENT);
        } else {
            DataFixerHandler.worldSavedData = new LabsWorldFixData();
            NomiLabs.LOGGER.info("This world was saved without a data version info. New Version: {}.",
                    LabsFixes.CURRENT);

            LabsWorldFixData.save(mapFile, DataFixerHandler.worldSavedData);
        }

        determineNeededFixesAndLog();

        if (neededNewFixes.isEmpty()) {
            NomiLabs.LOGGER.info(
                    "This world does not need any new data fixers, but it has no saved version, it is old, or this is a new world.");
            return;
        }

        sendMessage(
                MessageType.CONFIRM,
                Arrays.concat(Components.getIntro(), Components.getIntroAddition()));

        sendMessage(
                MessageType.NOTIFY,
                Components.getDoNotExit());

        checked = true;

        LabsRemapHelper.createWorldBackup();
    }

    private static void getInfoFromSave(SaveHandler save) {
        File levelDat = new File(save.getWorldDirectory(), "level.dat");

        // If level.dat file does not exist, return.
        // This normally means it is a new world.
        // Sometimes the level.dat file is created first, but usually this runs after it is created.
        // If the level.dat file is created first, its mod list is equal to the current one.
        if (!levelDat.exists())
            return;

        try {
            NBTTagCompound nbt = CompressedStreamTools.readCompressed(new FileInputStream(levelDat));
            if (!nbt.hasKey("FML", Constants.NBT.TAG_COMPOUND)) return;
            NBTTagCompound fml = nbt.getCompoundTag("FML");

            if (fml.hasKey("ModList", Constants.NBT.TAG_LIST)) {
                NBTTagList modList = fml.getTagList("ModList", Constants.NBT.TAG_COMPOUND);
                for (var mod : modList) {
                    if (!(mod instanceof NBTTagCompound compound)) continue;
                    if (!compound.hasKey("ModId", Constants.NBT.TAG_STRING) ||
                            !compound.hasKey("ModVersion", Constants.NBT.TAG_STRING))
                        continue;
                    if (!LabsValues.LABS_MODID.equals(compound.getString("ModId")))
                        continue;

                    savedLabsVersion = compound.getString("ModVersion");
                    return;
                }
            }
        } catch (IOException e) {
            NomiLabs.LOGGER.fatal("Failed to read level.dat.", e);
        }
    }

    private static void determineNeededFixesAndLog() {
        neededNewFixes = new Object2ObjectOpenHashMap<>();

        // If Nomi Labs Version is same as current version, exit.
        // This normally means it is a new world.
        // Sometimes the level.dat file is created first, but usually this runs after it is created.
        // If the level.dat file is created first, its mod list is equal to the current one.
        // Regardless, NEW fixes are not required.
        if (LabsValues.LABS_VERSION.equals(savedLabsVersion))
            return;

        DataFixerHandler.worldSavedData.processSavedLabsVersion(savedLabsVersion);

        NomiLabs.LOGGER.info("NEEDED DATA FIXES: ----------------------------------------");
        for (var fixType : LabsFixes.fixes.keySet()) {
            NomiLabs.LOGGER.info("SECTION: {} -------------------------------------------", fixType);
            var fixes = LabsFixes.fixes.get(fixType);
            for (var fix : fixes) {
                if (fix.validVersion.apply(DataFixerHandler.worldSavedData.savedFixVersion)) {
                    neededNewFixes.computeIfAbsent(fixType, (key) -> new ObjectArrayList<>()).add(fix);
                    NomiLabs.LOGGER.info("- {}: {}", fix.name, fix.description);
                }
            }
            NomiLabs.LOGGER.info("END SECTION: {}. SEE ABOVE. ^^^^^^^^^^^^^^^^^^^^^^^^^^^", fixType);
        }
        NomiLabs.LOGGER.info("END NEEDED DATA FIXES. SEE ABOVE. ^^^^^^^^^^^^^^^^^^^^^^^^^^");
    }

    /**
     * The Id to Block Map also includes Remapped IDs of Blocks.<br>
     * This means they are loaded after block id mismatch fixes, so the ids are correct to the world.
     */
    public static Map<Integer, ResourceLocation> getIdToBlockMap() {
        if (idToBlockMap != null) return idToBlockMap;

        ForgeRegistry<Block> registry = (ForgeRegistry<Block>) ForgeRegistries.BLOCKS;
        idToBlockMap = registry.getKeys().stream()
                .collect(Collectors.toMap(registry::getID, Function.identity()));
        var remReg = (RemappableForgeRegistry) registry;
        if (!remReg.getRemapped().isEmpty()) {
            NomiLabs.LOGGER.debug("Map Before Adding Remapped IDs:");
            NomiLabs.LOGGER.debug(idToBlockMap);
            NomiLabs.LOGGER.debug("Adding Block Remapped IDs:");
            NomiLabs.LOGGER.debug(remReg.getRemapped());
            idToBlockMap.putAll(remReg.getRemapped());
        }

        NomiLabs.LOGGER.debug("Generated Id to Block Map!");
        NomiLabs.LOGGER.debug(idToBlockMap);
        return idToBlockMap;
    }

    /**
     * The Block to Id Map is produced once needed, instead of in World Load or Post Init.<br>
     * This means they are loaded after block id mismatch fixes, so the ids are correct to the world.
     */
    public static Map<ResourceLocation, Integer> getBlockToIdMap() {
        if (blockToIdMap != null) return blockToIdMap;

        ForgeRegistry<Block> registry = (ForgeRegistry<Block>) ForgeRegistries.BLOCKS;
        blockToIdMap = registry.getKeys().stream()
                .collect(Collectors.toMap(Function.identity(), registry::getID));

        NomiLabs.LOGGER.debug("Generated Block To Id Map!");
        NomiLabs.LOGGER.debug(blockToIdMap);
        return blockToIdMap;
    }

    public static void processEnderStorageInfo(DataFixer fixer, SaveHandler save) {
        NomiLabs.LOGGER.info("Processing Ender Storage Info...");

        // Ender Storage Fixes
        // Ender Storage Stores Data in data1.dat and data2.dat. Sometimes its only data1.dat.
        // lock.dat stores a byte, which has weird bitwise stuff done to it, to determine which file (data1.dat or
        // data2.dat) to read.
        var enderStorageDir = new File(save.getWorldDirectory(), "EnderStorage");
        String[] processFiles = new String[] { "data1.dat", "data2.dat" };
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
        return !fixAvailable;
    }

    public static void close() {
        worldSavedData = null;
        checked = false;
        fixAvailable = false;
        neededNewFixes = null;
        idToBlockMap = null;
        blockToIdMap = null;
    }
}
