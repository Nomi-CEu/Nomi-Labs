package com.nomiceu.nomilabs.remap.datafixer;

import com.nomiceu.nomilabs.LabsValues;
import com.nomiceu.nomilabs.NomiLabs;
import com.nomiceu.nomilabs.remap.LabsRemapHelper;
import com.nomiceu.nomilabs.remap.datafixer.fixes.MultiblockFixer;
import com.nomiceu.nomilabs.remap.datafixer.fixes.ItemFixer;
import com.nomiceu.nomilabs.remap.datafixer.types.LabsFixTypes;
import com.nomiceu.nomilabs.remap.datafixer.walker.ItemStackWalker;
import io.sommers.packmode.PMConfig;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.storage.SaveHandler;
import net.minecraftforge.common.util.CompoundDataFixer;
import net.minecraftforge.common.util.ModFixs;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.StartupQuery;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * The main handler for all Data Fixes.
 * <p>
 * Values and Fixes are defined in {@link LabsFixes}.
 */
public class DataFixerHandler {
    public static LabsWorldFixData worldSavedData = null;

    public static void init() {
        LabsFixes.init();
        CompoundDataFixer fmlFixer = FMLCommonHandler.instance().getDataFixer();

        // Item Stack Walker
        fmlFixer.registerVanillaWalker(FixTypes.BLOCK_ENTITY, new ItemStackWalker(FixTypes.BLOCK_ENTITY.name()));
        fmlFixer.registerVanillaWalker(FixTypes.ENTITY, new ItemStackWalker(FixTypes.ENTITY.name()));
        fmlFixer.registerVanillaWalker(FixTypes.PLAYER, new ItemStackWalker(FixTypes.PLAYER.name()));
        fmlFixer.registerVanillaWalker(LabsFixTypes.WalkerTypes.ENDER_STORAGE, new ItemStackWalker(LabsFixTypes.WalkerTypes.ENDER_STORAGE.name()));

        // Fixers
        ModFixs fixs = fmlFixer.init(LabsValues.LABS_MODID, LabsFixes.FIX_VERSION);
        fixs.registerFix(LabsFixTypes.FixerTypes.ITEM, new ItemFixer());
        fixs.registerFix(FixTypes.BLOCK_ENTITY, new MultiblockFixer());
    }

    public static void onWorldLoad(SaveHandler save) {
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
            NomiLabs.LOGGER.info("This world's data version needs updating.");
        } else {
            DataFixerHandler.worldSavedData = new LabsWorldFixData();
            NomiLabs.LOGGER.info("This world was saved without a data version.");
        }

        // Separate into two messages
        // One asking for confirmation
        // Other asking if mode is correct, only asking if needed
        var message = new StringBuilder("This world must be remapped.\n\n")
                .append(TextFormatting.BOLD).append("A Backup will be made.\n")
                .append("Pressing 'No' will cancel world loading.\n\n")
                .append(TextFormatting.RED)
                .append("Note that after the world is loaded with this, you CANNOT undo this!\n")
                .append("You WILL have to load from the backup in order to load in a previous version!");

        if (!StartupQuery.confirm(message.toString())) {
            LabsRemapHelper.abort();
        }

        var modeMessage = new StringBuilder("Are you sure you previously loaded this world with the pack mode '")
                .append(TextFormatting.YELLOW).append(StringUtils.capitalize(PMConfig.getPackMode())).append(TextFormatting.RESET).append("' ?\n\n")
                .append(TextFormatting.RED).append("Launching with the wrong mode ")
                .append(TextFormatting.UNDERLINE).append("WILL").append(TextFormatting.RESET).append(TextFormatting.RED)
                .append(" void items and/or blocks!\n\n")
                .append("Press 'No' if you are not sure! (It will cancel world loading)");

        if (!StartupQuery.confirm(modeMessage.toString())) {
            LabsRemapHelper.abort();
        }

        LabsRemapHelper.createWorldBackup();

        // No need to increment version, the fix version, not the stored version, is saved
        // Still need to call as otherwise it isn't actually changed
        LabsWorldFixData.save(mapFile, DataFixerHandler.worldSavedData);
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
    }
}
