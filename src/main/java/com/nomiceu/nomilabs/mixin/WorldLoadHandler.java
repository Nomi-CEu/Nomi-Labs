package com.nomiceu.nomilabs.mixin;

import com.nomiceu.nomilabs.NomiLabs;
import com.nomiceu.nomilabs.remap.datafixer.DataFixerHandler;
import com.nomiceu.nomilabs.remap.datafixer.types.LabsFixTypes;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.storage.SaveFormatOld;
import net.minecraft.world.storage.SaveHandler;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
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
    @Inject(method = "loadAndFix(Ljava/io/File;Lnet/minecraft/util/datafix/DataFixer;Lnet/minecraft/world/storage/SaveHandler;)Lnet/minecraft/world/storage/WorldInfo;", at = @At("RETURN"), remap = false)
    private static void loadDataFixers(File file, DataFixer fixer, SaveHandler save, CallbackInfoReturnable<WorldInfo> cir) {
        NomiLabs.LOGGER.info("mixin world load");
        NomiLabs.LOGGER.info(FMLCommonHandler.instance().getEffectiveSide());
        try {
            // Init World Data Fixer
            DataFixerHandler.onWorldLoad(save);

            if (FMLCommonHandler.instance().getEffectiveSide().isClient()) {
                return;
            }

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
                } catch (IOException e) {
                    throw new IllegalStateException("Failed to write Ender Storage save data!", e);
                }
            }
        }
        catch (Exception ignored) {}
    }
}
