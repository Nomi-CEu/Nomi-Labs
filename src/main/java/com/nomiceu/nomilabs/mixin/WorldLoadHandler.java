package com.nomiceu.nomilabs.mixin;

import codechicken.enderstorage.manager.EnderStorageManager;
import com.nomiceu.nomilabs.NomiLabs;
import com.nomiceu.nomilabs.remap.LabsRemapHelper;
import com.nomiceu.nomilabs.remap.datafixer.DataFixerHandler;
import com.nomiceu.nomilabs.remap.datafixer.LabsFixes;
import com.nomiceu.nomilabs.remap.datafixer.LabsWorldFixData;
import com.nomiceu.nomilabs.remap.datafixer.types.LabsFixTypes;
import io.sommers.packmode.PMConfig;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.storage.SaveFormatOld;
import net.minecraft.world.storage.SaveHandler;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.StartupQuery;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.commons.lang3.StringUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

@Mixin(value = SaveFormatOld.class)
public class WorldLoadHandler {
    // No need for remap, forge method
    @Inject(method = "loadAndFix(Ljava/io/File;Lnet/minecraft/util/datafix/DataFixer;Lnet/minecraft/world/storage/SaveHandler;)Lnet/minecraft/world/storage/WorldInfo;", at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/CompressedStreamTools;readCompressed(Ljava/io/InputStream;)Lnet/minecraft/nbt/NBTTagCompound;"), remap = false)
    private static void loadDataFixers(File file, DataFixer fixer, SaveHandler save, CallbackInfoReturnable<WorldInfo> cir) {
        NomiLabs.LOGGER.info("mixin world load");
        NomiLabs.LOGGER.info(FMLCommonHandler.instance().getEffectiveSide());

        if (FMLCommonHandler.instance().getEffectiveSide() != Side.SERVER)
            return;

        var mapFile = save.getMapFileFromName(LabsFixes.DATA_NAME);

        if (mapFile.exists()) {
            DataFixerHandler.worldSavedData = LabsWorldFixData.load(mapFile);

            // Shortcut: If saved version == Current Version, Exit
            if (DataFixerHandler.worldSavedData.savedVersion == LabsFixes.FIX_VERSION) {
                DataFixerHandler.worldSavedData = null;
                return;
            }
        } else {
            DataFixerHandler.worldSavedData = new LabsWorldFixData();
            NomiLabs.LOGGER.info("This world was saved without a data version.");
        }

        // Separate into two messages
        // One asking for confirmation
        // Other asking if mode is correct, only asking if needed
        var message = "TEST HI?";

        if (!StartupQuery.confirm(message)) {
            LabsRemapHelper.abort();
        }

        var modeMessage = new StringBuilder("Are you sure you previously loaded this world with the pack mode '")
                .append(TextFormatting.YELLOW).append(StringUtils.capitalize(PMConfig.getPackMode())).append(TextFormatting.RESET).append("' ?\n\n")
                .append(TextFormatting.RED).append("Launching with the wrong mode ")
                .append(TextFormatting.UNDERLINE).append("WILL").append(TextFormatting.RESET)
                .append(" void items and/or blocks!\n\n")
                .append("Press 'No' to cancel world loading.");

        if (!StartupQuery.confirm(modeMessage.toString())) {
            LabsRemapHelper.abort();
        }

        LabsRemapHelper.createWorldBackup();

        // No need to increment version, the fix version, not the stored version, is saved
        // Still need to call as otherwise it isn't actually changed
        LabsWorldFixData.save(mapFile, DataFixerHandler.worldSavedData);

        if (FMLCommonHandler.instance().getEffectiveSide().isClient()) {
            EnderStorageManager.reloadManager(true);
        }
        try {
            NomiLabs.LOGGER.info("Processing Ender Storage Info");

            // Ender Storage Fixes
            // Ender Storage Stores Data in data1.dat and data2.dat. Sometimes its only data1.dat.
            // lock.dat stores a byte, which has weird bitwise stuff done to it, to determine which file (data1.dat or data2.dat) to read.
            var enderStorageDir = new File(save.getWorldDirectory(), "EnderStorage");
            String[] processFiles = new String[]{"data1.dat", "data2.dat"};
            for (var toProcess : processFiles) {
                File processFile = new File(enderStorageDir, toProcess);
                if (!processFile.isFile()) continue; // Also checks if it exists
                var fileNbt = CompressedStreamTools.readCompressed(new FileInputStream(processFile));
                var newNbt = fixer.process(LabsFixTypes.WalkerTypes.ENDER_STORAGE, fileNbt);

                try (FileOutputStream fileOut = new FileOutputStream(processFile)) {
                    CompressedStreamTools.writeCompressed(newNbt, fileOut);
                    NomiLabs.LOGGER.info("Successfully wrote {} Ender Storage Save Data!", toProcess);
                } catch (IOException e) {
                    throw new IllegalStateException("Failed to write Ender Storage save data!", e);
                }
            }
            EnderStorageManager.reloadManager(false);
        }
        catch (Exception ignored) {}
    }
}
