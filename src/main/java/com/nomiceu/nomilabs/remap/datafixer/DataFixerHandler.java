package com.nomiceu.nomilabs.remap.datafixer;

import com.nomiceu.nomilabs.LabsValues;
import com.nomiceu.nomilabs.NomiLabs;
import com.nomiceu.nomilabs.util.LabsBackup;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ModFixs;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.StartupQuery;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import java.io.File;
import java.io.FileInputStream;

public class DataFixerHandler {
    public static final String MAX_BAD_VERSION = "0.1.1";

    public static boolean worldLoaded = false;
    public static DataFixInfoWorldData worldSavedData;

    public static void init() {
        ModFixs modFixs = FMLCommonHandler.instance().getDataFixer().init(LabsValues.LABS_MODID, 12);
        modFixs.registerFix(FixTypes.ENTITY, new RedCoalFixer());

        modFixs.registerFix(FixTypes.BLOCK_ENTITY, new RedCoalFixer());

        modFixs.registerFix(FixTypes.PLAYER, new RedCoalFixer());

        modFixs.registerFix(FixTypes.LEVEL, new RedCoalFixer());

        modFixs.registerFix(FixTypes.ITEM_INSTANCE, new RedCoalFixer());
    }

    public static void onWorldLoad(WorldEvent.Load event) {
        if (!event.getWorld().isRemote && !worldLoaded) {
            worldLoaded = true;

            // Check if this world was previously started being fixed
            worldSavedData = DataFixInfoWorldData.load(event.getWorld());
            if (worldSavedData != null) return;

            worldSavedData = new DataFixInfoWorldData();

            // Any code from this point should only ever run at most once for a world
            try {
                File saveDir = event.getWorld().getSaveHandler().getWorldDirectory();
                if (!saveDir.exists()) return;
                File levelDat = new File(saveDir, "level.dat");
                if (!levelDat.exists()) {
                    DataFixInfoWorldData.save(event.getWorld(), worldSavedData);
                    return;
                }
                NBTTagCompound nbt = CompressedStreamTools.readCompressed(new FileInputStream(levelDat));
                NBTTagCompound fmlTag = nbt.getCompoundTag("FML");

                if (fmlTag.hasKey("ModList")) {
                    NBTTagList modList = fmlTag.getTagList("ModList", (byte) 10);
                    for (int i = 0; i < modList.tagCount(); i++) {
                        NBTTagCompound mod = modList.getCompoundTagAt(i);
                        if (!mod.getString("ModId").equals(LabsValues.LABS_MODID)) continue;
                        String versionInSave = mod.getString("ModVersion");


                        if (compareVersions(versionInSave, MAX_BAD_VERSION) <= 0)
                            initDataFixes(event.getWorld());

                        NomiLabs.LOGGER.debug("Mod version in save is {}, {} data fixes.", versionInSave, worldSavedData.dataFixes ? "enabling" : "disabling");
                        break;
                    }
                    // World loaded without coremod
                    NomiLabs.LOGGER.debug("Nomi Labs was previously not used in this save, {} data fixes.", worldSavedData.dataFixes ? "enabling" : "disabling");
                    initDataFixes(event.getWorld());

                    DataFixInfoWorldData.save(event.getWorld(), worldSavedData);

                } else {
                    NomiLabs.LOGGER.error("NBT doesnt have ModList key. Initialising DataFixers In Case...");
                    initDataFixes(event.getWorld());

                    DataFixInfoWorldData.save(event.getWorld(), worldSavedData);
                }
            } catch (Exception e) {
                StartupQuery.abort();
                NomiLabs.LOGGER.error("Error on World Loading. Closing Server...");
                NomiLabs.LOGGER.throwing(e);
            }
        }
    }

    public static void playerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        // If data fixing is disabled for the current world, skip
        if (!worldSavedData.dataFixes) return;

        EntityPlayer player = event.player;

        RedCoalFixer redCoalFixer = new RedCoalFixer();

        // The first time this world is loaded in singleplayer, fixHostPlayerInventory
        // is going to be set to false, and never run again.
        // Itemfixing here only happens for the host player of this singleplayer world,
        // any other case (players joining a DedicatedServer or a LAN IntegratedServer)
        // is covered by the Player Datafixer.

        // Check if this is an IntegratedServer
        if (!FMLCommonHandler.instance().getMinecraftServerInstance().isDedicatedServer()) {
            // Fix items in player's inventory
            for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
                NBTTagCompound slotNBT = player.inventory.getStackInSlot(i).serializeNBT();
                if (worldSavedData.fixHostPlayerInventory) {
                    redCoalFixer.fixItemsInCompound(slotNBT);
                    player.inventory.setInventorySlotContents(i, new ItemStack(slotNBT));
                }
            }

            // Fix items in player's EnderChest
            for (int i = 0; i < player.getInventoryEnderChest().getSizeInventory(); i++) {
                NBTTagCompound slotNBT = player.getInventoryEnderChest().getStackInSlot(i).serializeNBT();
                if (worldSavedData.fixHostPlayerInventory) {
                    redCoalFixer.fixItemsInCompound(slotNBT);
                    player.getInventoryEnderChest().setInventorySlotContents(i, new ItemStack(slotNBT));
                }
            }

            worldSavedData.fixHostPlayerInventory = false;
            DataFixInfoWorldData.save(event.player.getEntityWorld(), worldSavedData);
        } else
            NomiLabs.LOGGER.debug("Server is dedicated, skipping PlayerLoggedIn fixes.");
    }

    public static void initDataFixes(World world) {
        LabsBackup.createWorldBackup();

        worldSavedData.dataFixes = true;
        worldSavedData.fixHostPlayerInventory = true;

        DataFixInfoWorldData.save(world, worldSavedData);
    }

    /**
     * Returns positive if v1 > v2, 0 if equal, negative if v2 > v1
     */
    public static int compareVersions(String version1, String version2) {
        String[] v1 = version1.split("\\.");
        String[] v2 = version2.split("\\.");
        int v1Num, v2Num;
        try {
            v1Num = transformStringListToInt(v1);
            v2Num = transformStringListToInt(v2);
        } catch (NumberFormatException e) {
            NomiLabs.LOGGER.fatal("Failed to parse versions {} and/or {}. See error below. Saying versions are the same...", version1, version2);
            return 0;
        }
        NomiLabs.LOGGER.info("This world was previously loaded with Nomi Labs Version {}, which has been transformed into {}", v1, v1Num);
        return v2Num - v1Num;
    }

    private static int transformStringListToInt(String[] list) {
        var output = new int[list.length];
        for (int i = 0; i < list.length; i++) {
            output[i] = Integer.parseInt(list[i]);
        }
        var currentMulti = 1;
        var multiIncrease = 10;
        var num = 0;
        for (var number : output) {
            num += number * currentMulti;
            currentMulti *= multiIncrease;
        }
        return num;
    }

    public static void close() {
        worldLoaded = false;
        worldSavedData = null;
    }
}
