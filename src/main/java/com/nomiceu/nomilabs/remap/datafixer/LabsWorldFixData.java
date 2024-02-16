package com.nomiceu.nomilabs.remap.datafixer;

import com.nomiceu.nomilabs.LabsValues;
import com.nomiceu.nomilabs.NomiLabs;
import com.nomiceu.nomilabs.config.LabsConfig;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

public class LabsWorldFixData extends WorldSavedData {
    // Fix Version stored in this world.
    public int savedVersion;

    // Base Data Key
    public static final String BASE_DATA_KEY = "data";

    // Key used to store fix version
    public static final String VERSION_KEY = "Version";

    public LabsWorldFixData() {
        super(LabsFixes.DATA_NAME);
        if (LabsConfig.advanced.enableNomiCEuDataFixes) savedVersion = LabsFixes.DEFAULT_NOMI_CEU;
        else savedVersion = LabsFixes.DEFAULT;
    }

    public void processModList(Map<String, String> mods) {
        if (!mods.containsKey(LabsValues.LABS_MODID) && savedVersion == LabsFixes.DEFAULT)
            savedVersion = LabsFixes.NEW;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        if (nbt.hasKey(VERSION_KEY, Constants.NBT.TAG_ANY_NUMERIC)) {
            savedVersion = nbt.getInteger(VERSION_KEY);
            NomiLabs.LOGGER.info("This world was previously loaded with Fix Version {}.", savedVersion);
        } else
            NomiLabs.LOGGER.info("This world was previously loaded without a saved Fix Version, possibly due to corruption, defaulting to {}.", savedVersion);
    }

    @Override
    public @NotNull NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setInteger(VERSION_KEY, LabsFixes.CURRENT); // Save the current fix version, not the saved version
        return compound;
    }

    public static LabsWorldFixData load(File mapStorageFile) {
        var tag = getDataCompoundOrNew(mapStorageFile);

        var data = new LabsWorldFixData();
        data.readFromNBT(tag.getCompoundTag(LabsFixes.DATA_KEY));
        return data;
    }

    public static void save(File mapStorageFile, LabsWorldFixData data) {
        var tag = new NBTTagCompound();
        NBTTagCompound dataTag;
        if (mapStorageFile.isFile()) dataTag = getDataCompoundOrNew(mapStorageFile);
        else dataTag = new NBTTagCompound();

        dataTag.setTag(LabsFixes.DATA_KEY, data.writeToNBT(new NBTTagCompound()));
        tag.setTag(BASE_DATA_KEY, dataTag);

        try (FileOutputStream mapStorageOut = new FileOutputStream(mapStorageFile)) {
            CompressedStreamTools.writeCompressed(tag, mapStorageOut);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to write Nomi Labs save fix data!", e);
        }
    }

    @NotNull
    private static NBTTagCompound getDataCompoundOrNew(File mapStorageFile) {
        try (FileInputStream mapStorageIn = new FileInputStream(mapStorageFile)) {
            var tag = CompressedStreamTools.readCompressed(mapStorageIn).getCompoundTag(BASE_DATA_KEY);
            if (tag.hasKey(LabsFixes.DATA_KEY, Constants.NBT.TAG_COMPOUND)) {
                return tag;
            }
            return new NBTTagCompound();

        } catch (IOException e) {
            throw new IllegalStateException("Failed to read Nomi Labs saved fix data!", e);
        }
    }
}
