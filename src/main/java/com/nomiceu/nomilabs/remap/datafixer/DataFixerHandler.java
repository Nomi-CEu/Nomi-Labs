package com.nomiceu.nomilabs.remap.datafixer;

import com.nomiceu.nomilabs.LabsValues;
import com.nomiceu.nomilabs.NomiLabs;
import com.nomiceu.nomilabs.config.LabsConfig;
import com.nomiceu.nomilabs.remap.LabsRemapHelper;
import com.nomiceu.nomilabs.remap.datafixer.fixes.BlockFixer;
import com.nomiceu.nomilabs.remap.datafixer.fixes.TileEntityFixer;
import com.nomiceu.nomilabs.remap.datafixer.fixes.ItemFixer;
import com.nomiceu.nomilabs.remap.datafixer.types.LabsFixTypes;
import com.nomiceu.nomilabs.remap.datafixer.walker.BlockEntityWalker;
import com.nomiceu.nomilabs.remap.datafixer.walker.ChunkWalker;
import com.nomiceu.nomilabs.remap.datafixer.walker.ItemStackWalker;
import io.sommers.packmode.PMConfig;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IFixType;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.storage.SaveHandler;
import net.minecraftforge.common.util.CompoundDataFixer;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.ModFixs;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.StartupQuery;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The main handler for all Data Fixes.
 * <p>
 * Values and Fixes are defined in {@link LabsFixes}.
 */
public class DataFixerHandler {
    public static LabsWorldFixData worldSavedData = null;
    public static boolean checked = false;

    public static Map<IFixType, List<DataFix<?>>> neededFixes;
    public static boolean modeNeeded = false;

    public static Map<Integer, ResourceLocation> blockIdToRlMap;

    public static void preInit() {
        CompoundDataFixer fmlFixer = FMLCommonHandler.instance().getDataFixer();

        var itemWalker = new ItemStackWalker();
        fmlFixer.registerVanillaWalker(FixTypes.ENTITY, itemWalker);
        fmlFixer.registerVanillaWalker(FixTypes.PLAYER, itemWalker);
        fmlFixer.registerVanillaWalker(LabsFixTypes.WalkerTypes.ENDER_STORAGE, itemWalker);

        fmlFixer.registerVanillaWalker(FixTypes.BLOCK_ENTITY, new BlockEntityWalker(itemWalker));

        fmlFixer.registerVanillaWalker(FixTypes.CHUNK, new ChunkWalker());

        // Fixers
        ModFixs fixs = fmlFixer.init(LabsValues.LABS_MODID, LabsFixes.FIX_VERSION);
        fixs.registerFix(LabsFixTypes.FixerTypes.ITEM, new ItemFixer());
        fixs.registerFix(LabsFixTypes.FixerTypes.BLOCK, new BlockFixer());
        fixs.registerFix(LabsFixTypes.FixerTypes.TILE_ENTITY, new TileEntityFixer());
    }

    public static void onWorldLoad(SaveHandler save) {
        if (!LabsConfig.advanced.enableDataFixes) {
            checked = true;
            modeNeeded = false;
            worldSavedData = null;
            NomiLabs.LOGGER.info("Skipping Data Fixers, due to config...");
            return;
        }
        checked = false;
        modeNeeded = false;
        NomiLabs.LOGGER.info("Checking Data Fixers...");

        var mapFile = save.getMapFileFromName(LabsFixes.DATA_NAME);

        if (mapFile.exists()) {
            DataFixerHandler.worldSavedData = LabsWorldFixData.load(mapFile);

            // Shortcut: If saved version == Current Version, Exit
            if (DataFixerHandler.worldSavedData.savedVersion == LabsFixes.FIX_VERSION) {
                DataFixerHandler.worldSavedData = null;
                NomiLabs.LOGGER.info("This world's data version is up to date.");
                return;
            }
            NomiLabs.LOGGER.info("This world's data version needs updating. New Version: {}.", LabsFixes.FIX_VERSION);
        } else {
            DataFixerHandler.worldSavedData = new LabsWorldFixData();
            NomiLabs.LOGGER.info("This world was saved without a data version.");
        }

        LabsFixes.init();
        determineNeededFixesAndLog(save);
        if (neededFixes.isEmpty()) {
            NomiLabs.LOGGER.info("This world does not need any data fixers, but it has no saved version or it is old.");
            LabsWorldFixData.save(mapFile, DataFixerHandler.worldSavedData);
            return;
        }

        var message = new StringBuilder("This world must be remapped.\n\n")
                .append(TextFormatting.BOLD).append("A Backup will be made.\n")
                .append("Pressing 'No' will cancel world loading.\n\n")
                .append(TextFormatting.RED)
                .append("Note that after the world is loaded with this, you CANNOT undo this!\n")
                .append("The changes that must be made via Data Fixers have been printed to your log.\n")
                .append("You WILL have to load from the backup in order to load in a previous version!");

        if (!StartupQuery.confirm(message.toString())) {
            LabsRemapHelper.abort();
        }

        if (modeNeeded) {
            var modeMessage = new StringBuilder("Are you sure you previously loaded this world with the pack mode '")
                    .append(TextFormatting.YELLOW).append(StringUtils.capitalize(PMConfig.getPackMode())).append(TextFormatting.RESET).append("' ?\n\n")
                    .append(TextFormatting.RED).append("Launching with the wrong mode ")
                    .append(TextFormatting.UNDERLINE).append("WILL").append(TextFormatting.RESET).append(TextFormatting.RED)
                    .append(" void items and/or blocks!\n\n")
                    .append("Press 'No' if you are not sure! (It will cancel world loading)");

            if (!StartupQuery.confirm(modeMessage.toString())) {
                LabsRemapHelper.abort();
            }
        }

        generateIDToRlMap();
        checked = true;

        LabsRemapHelper.createWorldBackup();

        // No need to increment version, the fix version, not the stored version, is saved
        // Still need to call as otherwise it isn't actually changed
        LabsWorldFixData.save(mapFile, DataFixerHandler.worldSavedData);
    }

    private static void determineNeededFixesAndLog(SaveHandler save) {
        neededFixes = new Object2ObjectOpenHashMap<>();
        File levelDat = new File(save.getWorldDirectory(), "level.dat");
        if (!levelDat.exists()) {
            return;
        }

        Map<String, String> mods = new HashMap<>();
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

        NomiLabs.LOGGER.info("NEEDED DATA FIXES: ----------------------------------------");
        for (var fixType : LabsFixes.fixes.keySet()) {
            NomiLabs.LOGGER.info("SECTION: {} -------------------------------------------", fixType);
            var fixes = LabsFixes.fixes.get(fixType);
            for (var fix : fixes) {
                if (fix.validVersion.apply(DataFixerHandler.worldSavedData.savedVersion) && fix.validModList.apply(mods)) {
                    if (!neededFixes.containsKey(fixType)) neededFixes.put(fixType, new ObjectArrayList<>());
                    neededFixes.get(fixType).add(fix);
                    if (fix.needsMode) modeNeeded = true;
                    NomiLabs.LOGGER.info("- {}, {}", fix.name, fix.description);
                }
            }
            NomiLabs.LOGGER.info("END SECTION: {}. SEE ABOVE. ^^^^^^^^^^^^^^^^^^^^^^^^^^^", fixType);
        }
        NomiLabs.LOGGER.info("END NEEDED DATA FIXES. SEE ABOVE. ^^^^^^^^^^^^^^^^^^^^^^^^^^");
    }

    public static void generateIDToRlMap() {
        if (!neededFixes.containsKey(LabsFixTypes.FixerTypes.BLOCK)) return;
        blockIdToRlMap = new Object2ObjectOpenHashMap<>();
        var registry = LabsRemapHelper.getBlockRegistry();
        neededFixes.get(LabsFixTypes.FixerTypes.BLOCK).stream()
                .map((fix) -> fix instanceof DataFix.BlockFix blockFix ? blockFix.neededLocations : new ResourceLocation[0])
                .forEach((rls) -> {
                    for (var rl : rls) {
                        var id = registry.getID(rl);
                        if (id > 0) blockIdToRlMap.put(id, rl);
                    }
                });
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
    }

    public static boolean fixNotAvailable() {
        return worldSavedData == null;
    }

    public static void close() {
        worldSavedData = null;
        checked = false;
    }
}
