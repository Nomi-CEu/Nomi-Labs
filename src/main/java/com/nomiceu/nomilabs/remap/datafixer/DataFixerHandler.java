package com.nomiceu.nomilabs.remap.datafixer;

import com.nomiceu.nomilabs.LabsValues;
import com.nomiceu.nomilabs.NomiLabs;
import com.nomiceu.nomilabs.remap.LabsRemapHelper;
import com.nomiceu.nomilabs.remap.datafixer.fixes.ItemFixer;
import com.nomiceu.nomilabs.remap.datafixer.types.LabsFixTypes;
import com.nomiceu.nomilabs.remap.datafixer.walker.ItemStackWalker;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.world.storage.SaveHandler;
import net.minecraftforge.common.util.CompoundDataFixer;
import net.minecraftforge.common.util.ModFixs;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.StartupQuery;
import net.minecraftforge.fml.relauncher.Side;

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
        IDataWalker itemStackWalker = new ItemStackWalker();
        fmlFixer.registerVanillaWalker(FixTypes.BLOCK_ENTITY, itemStackWalker);
        fmlFixer.registerVanillaWalker(FixTypes.ENTITY, itemStackWalker);
        fmlFixer.registerVanillaWalker(FixTypes.PLAYER, itemStackWalker);
        fmlFixer.registerVanillaWalker(LabsFixTypes.WalkerTypes.ENDER_STORAGE, itemStackWalker);

        // Chunk Walker
        //fmlFixer.registerVanillaWalker(FixTypes.CHUNK, new ChunkWalker());

        // Fixer
        ModFixs fixs = fmlFixer.init(LabsValues.LABS_MODID, LabsFixes.FIX_VERSION);
        fixs.registerFix(LabsFixTypes.FixerTypes.ITEM, new ItemFixer());
        //fixs.registerFix(LabsFixTypes.FixerTypes.BLOCK, new BlockFixer());
    }

    public static boolean fixAvailable() {
        return worldSavedData != null;
    }

    // TODO Rework
    public static void onWorldLoad(SaveHandler saveHandler) {
        /* TODO sort this out
        if (FMLCommonHandler.instance().getEffectiveSide() != Side.SERVER) {
            return;
        }

         */

        if (worldSavedData != null)
            NomiLabs.LOGGER.info("World has already been loaded.");

        var saveDir = saveHandler.getWorldDirectory();
        var mapFile = saveHandler.getMapFileFromName(LabsFixes.DATA_NAME);
        if (!saveDir.exists()) return;

        if (mapFile.exists()) {
            worldSavedData = LabsWorldFixData.load(mapFile);
        } else {
            worldSavedData = new LabsWorldFixData();
            NomiLabs.LOGGER.info("no saved");
        }

        // Shortcut: If saved version == Current Version, Exit
        if (worldSavedData.savedVersion == LabsFixes.FIX_VERSION) {
            worldSavedData = null;
            return;
        }

        if (FMLCommonHandler.instance().getEffectiveSide() != Side.SERVER) {
            boolean confirmed = StartupQuery.confirm("TEST HI?");  // TODO Warn
            if (!confirmed) LabsRemapHelper.abort();
            LabsRemapHelper.createWorldBackup();
        } else {
            // No need to increment version, the fix version, not the stored version, is saved
            // Still need to call as otherwise it isn't actually changed
            LabsWorldFixData.save(mapFile, worldSavedData);
        }
    }

    // TODO Rework
    /*
    public static void onWorldLoad(WorldEvent.Load event) {
        if (!event.getWorld().isRemote && !worldLoaded) {
            worldLoaded = true;

            worldSavedData = new LabsWorldFixData();

            // Any code from this point should only ever run at most once for a world
            try {
                File saveDir = event.getWorld().getSaveHandler().getWorldDirectory();
                if (!saveDir.exists()) return;
                File levelDat = new File(saveDir, "level.dat");
                if (!levelDat.exists()) {
                    LabsWorldFixData.save(event.getWorld(), worldSavedData);
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

                        if (compareVersions(versionInSave, LabsRemapHelper.NEWEST_PRE_LABS_VERSION) < 0)
                            initDataFixes(event.getWorld());

                        NomiLabs.LOGGER.debug("Mod version in save is {}, {} data fixes.", versionInSave, worldSavedData.dataFixes ? "enabling" : "disabling");
                        break;
                    }
                    // World loaded without coremod
                    NomiLabs.LOGGER.debug("Nomi Labs was previously not used in this save, {} data fixes.", worldSavedData.dataFixes ? "enabling" : "disabling");
                    initDataFixes(event.getWorld());

                    LabsWorldFixData.save(event.getWorld(), worldSavedData);

                } else {
                    NomiLabs.LOGGER.error("NBT doesnt have ModList key. Skipping...");
                }
            } catch (Exception e) {
                LabsRemapHelper.abort();
                NomiLabs.LOGGER.error("Error on World Loading. Closing Server...");
                NomiLabs.LOGGER.throwing(e);
            }
        }
    }

     */

    // TODO NEEDED?
    /*
    public static void playerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        // If data fixing is disabled for the current world, skip
        if (!worldSavedData.dataFixes) return;

        EntityPlayer player = event.player;

        DataFixerOld dataFixerOld = new DataFixerOld();

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
                    dataFixerOld.fixItemsInCompound(slotNBT);
                    player.inventory.setInventorySlotContents(i, new ItemStack(slotNBT));
                }
            }

            // Fix items in player's EnderChest
            for (int i = 0; i < player.getInventoryEnderChest().getSizeInventory(); i++) {
                NBTTagCompound slotNBT = player.getInventoryEnderChest().getStackInSlot(i).serializeNBT();
                if (worldSavedData.fixHostPlayerInventory) {
                    dataFixerOld.fixItemsInCompound(slotNBT);
                    player.getInventoryEnderChest().setInventorySlotContents(i, new ItemStack(slotNBT));
                }
            }

            worldSavedData.fixHostPlayerInventory = false;
            LabsWorldFixData.save(event.player.getEntityWorld(), worldSavedData);
        } else
            NomiLabs.LOGGER.debug("Server is dedicated, skipping PlayerLoggedIn fixes.");
    }

     */

    /*
    public static void warn() {
        StringBuilder warn = new StringBuilder();
        List<String> remove = new ArrayList<>();
        warn.append(TextFormatting.BOLD).append("Data Fixers must be Activated.\n").append(TextFormatting.RESET);
        if (LabsConfig.content.customContent.remap) {
            remove.add("Dark Red Coal");
            warn.append("- This properly transforms old Dark Red Coal.\n");
        }
        if (LabsConfig.modIntegration.enableExtraUtils2Integration) {
            remove.add("Red Coal");
            remove.add("Redstone Coils");
            warn.append("- This modifies Red Coal and Redstone Coil (XU2 Items),\n")
                    .append("to not have Frequency NBT, which is removed.\n");
        }
        if (LabsConfig.content.gtCustomContent.enableOldMultiblocks) {
            remove.add("Custom MultiBlock Tweaker Multiblock Items");
            warn.append("- This modifies the metadata of multiblocks, which have been changed.\n");
        }
        warn.append("\n").append(TextFormatting.YELLOW).append("Please Remove:\n- ")
                .append(String.join("\n- ", remove))
                .append("\n")
                .append("from all Ender Storage Ender Chests, in an older instance.\n")
                .append(TextFormatting.GREEN).append("(Nomi-CEu ").append(NEWEST_PRE_NOMI_VERSION)
                .append("-, Nomi Labs ").append(NEWEST_PRE_LABS_VERSION)
                .append("-)\n\n").append(TextFormatting.RESET);

        warn.append(TextFormatting.BOLD).append("A Backup will be made. Pressing 'No' Will Cancel World Loading.\n\n")
                .append(TextFormatting.RED)
                .append("Note that after the world is loaded with this, you CANNOT undo this!\nYou WILL have to load from a backup!\n\n")
                .append(TextFormatting.RESET);

        boolean confirmed = StartupQuery.confirm(warn.toString());
        if (!confirmed)
            StartupQuery.abort();
    }
    */

    /*
    public static void initDataFixes(World world) {
        LabsRemapHelper.createWorldBackup();

        worldSavedData.dataFixes = true;
        worldSavedData.fixHostPlayerInventory = true;

        LabsWorldFixData.save(world, worldSavedData);
    }
     */

    /**
     * Returns positive if v1 > v2, 0 if equal, negative if v2 > v1
     */
    /*
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

     */

    public static void close() {
        worldSavedData = null;
    }
}
