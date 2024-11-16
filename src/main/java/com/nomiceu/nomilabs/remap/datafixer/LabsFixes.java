package com.nomiceu.nomilabs.remap.datafixer;

import static com.nomiceu.nomilabs.LabsValues.*;
import static com.nomiceu.nomilabs.util.LabsNames.makeLabsName;

import java.util.*;
import java.util.stream.Collectors;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.IFixType;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.Loader;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.ImmutableMap;
import com.nomiceu.nomilabs.config.LabsConfig;
import com.nomiceu.nomilabs.item.ItemInfo;
import com.nomiceu.nomilabs.item.registry.LabsItems;
import com.nomiceu.nomilabs.remap.LabsRemapHelper;
import com.nomiceu.nomilabs.remap.LabsRemappers;
import com.nomiceu.nomilabs.remap.datafixer.storage.ItemStackLike;
import com.nomiceu.nomilabs.remap.datafixer.types.LabsFixTypes;

import gregtech.api.GregTechAPI;
import gregtech.api.unification.material.Material;
import gregtech.common.pipelike.cable.Insulation;
import gregtech.common.pipelike.fluidpipe.FluidPipeType;
import gregtech.common.pipelike.itempipe.ItemPipeType;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.shorts.Short2ShortLinkedOpenHashMap;

/**
 * Definitions for all values, and all data fixes.
 * <p>
 * When the modList matches, and the fix version the world was created with matches the fix, the fix is included.
 * This is because items and blocks may not be loaded in the first load.
 * However, only fixes where the previous version the world was loaded with matches will be included in Ender Storage
 * Remapping.
 */
public class LabsFixes {

    /**
     * The name used to store fix data. Do not change this, although changing it will FORCE all fixes to be applied.
     * Do this via another way.
     */
    public static final String DATA_NAME = LABS_MODID + ".fix_data";

    /**
     * In which nbt key the data is stored. Do not change this, although changing it will FORCE all fixes to be applied.
     * Do this via another way. (This is better than changing the data name)
     */
    public static final String DATA_KEY = "LabsFixer";

    /**
     * The Data Fixer Version that occurs when a world has not been loaded with Nomi Labs before, and Nomi-CEu Specific
     * Fixes are not enabled.
     * <p>
     * This is not for new worlds; new worlds must never have Data Fixes.
     * <p>
     * When comparing against this version, check for equality rather than less than!
     */
    public static final int NEW = Integer.MAX_VALUE;

    /**
     * The current data format version. Increment this when breaking changes are made and
     * data fixers should be applied. If this is not incremented, nothing will be applied to loaded worlds and
     * inventories.
     */
    public static final int CURRENT = 4;

    /**
     * Version before Remapping of AE2 Stuff Pattern Encoder.
     * <p>
     * Versions before this needs to have the AE2 Stuff Pattern Encoder mapped to AE2 Interfaces.
     * <p>
     * Note that this also may be needed on worlds that have not been loaded with NomiLabs before!
     */
    public static final int PRE_AE2_STUFF_REMAP = 3;

    /**
     * Version before Capacitor Remapping.
     * <p>
     * Versions before this need nbt of custom capacitors removed.
     */
    public static final int PRE_CAPACITOR_REMAPPING = 2;

    /**
     * Version before Material Registry Rework.
     * <p>
     * Versions before this need to have their material items be remapped. Meta Blocks are remapped based on missing
     * registry event, as they have 'baseID's
     * (thus, any new GT Addons must be checked to make sure they do not have IDs in GregTech's Registry above 32000)
     * <p>
     * Meta Items are mapped below.
     */
    public static final int PRE_MATERIAL_REWORK = 1;

    /**
     * Default version, used if save doesn't have a fix version and nomi-labs was loaded in the world before, or if
     * something goes wrong.
     */
    public static final int DEFAULT = 0;

    /**
     * Default Nomi-CEu version, used if save doesn't have a fix version, or if something goes wrong, and Nomi-CEu
     * Specific Data Fixes are enabled.
     */
    public static final int DEFAULT_NOMI_CEU = -1;

    public static List<DataFix.ItemFix> itemFixes;

    public static List<DataFix.BlockFix> blockFixes;

    public static List<DataFix.TileEntityFix> tileEntityFixes;

    public static Map<IFixType, List<? extends DataFix<?>>> fixes;

    public static Map<ResourceLocation, ResourceLocation> multiblockMetaIdRemap;
    public static Map<Short, Short> multiblockMetaRemap;
    public static Set<ResourceLocation> specialMetaItemsRemap;
    public static Set<String> materialNames;
    public static Map<String, OldCapacitorSpecification> capacitorSpecificationRemap;

    public static void init() {
        helpersInit();

        /*
         * Item Fixes.
         * Note that this is not applied on any new items.
         * This fix is applied:
         * - On Player Inventories
         * - On Entity Inventories
         * - On Tile Entity Inventories
         * - Inside Ender Storage Ender Chests
         *
         * Example of an input:
         * ItemStackLike:
         * rl: "minecraft:skull" // Type: Resource Location. Not Null.
         * meta: 3 // Type: Short
         * tag: {owner: "adsf", i: {k: 1, l: 4}, ...} // Type: NBT Tag Compound. Nullable.
         *
         * Example:
         * itemFixes.add(
         * new DataFix.ItemFix("Example", // Name
         *
         * "Example Item Fix. Turns Apples into Creeper Heads.", // Description
         *
         * false, // Whether the correct mode is required for the change to work right
         *
         * (version) -> version <= DEFAULT_VERSION, // Whether the previous version in the save means that this fix must
         * be applied.
         * // Note that this is not included in the fix list if the previous version is equal to the current overall fix
         * version.
         *
         * (stack) -> stack.rl.equals(new ResourceLocation("minecraft:apple")), // Input ItemStackLike, return a boolean
         * (true to fix, false to skip)
         *
         * (stack) -> stack.setRl(new ResourceLocation("minecraft:skull")).setMeta((short) 4))); // Change the given
         * ItemStackLike (Changes to Creeper Head in this case)
         */
        itemFixes = new ObjectArrayList<>();

        // Must be first Dark Red Coal Remap and Custom Capacitor NBT Removal, then Deprecated Item Remap, then CT
        // General Remap,
        // because only one fix is applied.

        itemFixes.add(
                new DataFix.ItemFix("Dark Red Coal Remap",
                        "Correctly remaps Content Tweaker Dark Red Coal to XU2 Red Coal.",
                        false,
                        (version) -> version <= DEFAULT,
                        (stack) -> stack.rl.equals(new ResourceLocation(CONTENTTWEAKER_MODID, "dark_red_coal")),
                        (stack) -> stack.setRl(new ResourceLocation(XU2_MODID, "ingredients"))
                                .setMeta((short) 4)) // Red Coal
        );

        if (Loader.isModLoaded(ENDER_IO_MODID))
            itemFixes.add(
                    new DataFix.ItemFix("Custom Capacitor NBT Removal",
                            "Removes NBT from Custom Capacitors.",
                            false,
                            (version) -> version <= PRE_CAPACITOR_REMAPPING,
                            (stack) -> (stack.rl.getNamespace().equals(LABS_MODID) ||
                                    stack.rl.getNamespace().equals(CONTENTTWEAKER_MODID)) &&
                                    capacitorSpecificationRemap.containsKey(stack.rl.getPath()) &&
                                    capacitorSpecificationRemap.get(stack.rl.getPath()).needChange(stack.tag),
                            (stack) -> stack
                                    .setTag(capacitorSpecificationRemap.get(stack.rl.getPath()).remove(stack.tag))));

        itemFixes.add(
                new DataFix.ItemFix("Deprecated Item Remap",
                        "Remaps Deprecated Items to their Modern Counterparts.",
                        false,
                        (version) -> version <= DEFAULT,
                        (stack) -> LabsRemappers.deprecatedRemapper.shouldRemap(stack.rl),
                        (stack) -> stack.setRl(LabsRemappers.deprecatedRemapper.remapRl(stack.rl))));

        itemFixes.add(
                new DataFix.ItemFix("Content Tweaker Item Remap",
                        "Remaps Content Tweaker Items to their counterparts.",
                        false,
                        (version) -> version <= DEFAULT,
                        (stack) -> LabsRemappers.ctRemapper.shouldRemap(stack.rl),
                        (stack) -> stack.setRl(LabsRemappers.ctRemapper.remapRl(stack.rl))));

        itemFixes.add(
                new DataFix.ItemFix("Perfect Gem Item Remap",
                        "Remaps Perfect Gems to their counterparts.",
                        false,
                        (version) -> version <= DEFAULT,
                        (stack) -> LabsRemappers.perfectGemRemapper.shouldRemap(stack.rl),
                        (stack) -> stack.setRl(LabsRemappers.perfectGemRemapper.remapRl(stack.rl))));

        if (LabsConfig.modIntegration.enableExtraUtils2Integration)
            itemFixes.add(
                    new DataFix.ItemFix("XU2 Frequency Removal",
                            "Removes Frequency from XU2 Ingredients.",
                            false,
                            (version) -> version <= DEFAULT || version == NEW,
                            (stack) -> stack.rl.equals(new ResourceLocation(XU2_MODID, "ingredients")) &&
                                    stack.tag != null && stack.tag.hasKey("Freq"),
                            (stack) -> {
                                var tag = Objects.requireNonNull(stack.tag);
                                tag.removeTag("Freq");
                                stack.setTag(tag);
                            }));

        itemFixes.add(
                new DataFix.ItemFix("Old Multiblock Metadata Remap",
                        "Remaps old Multiblock Metadata to the new format.",
                        true,
                        (version) -> version <= DEFAULT_NOMI_CEU,
                        (stack) -> stack.rl.equals(new ResourceLocation(GREGTECH_MODID, "machine")) &&
                                multiblockMetaRemap.containsKey(stack.meta),
                        (stack) -> stack.setMeta(multiblockMetaRemap.get(stack.meta))));

        itemFixes.add(
                new DataFix.ItemFix("Material Meta Item Remap",
                        "Remaps old Meta Items, from Custom Materials, to the new format and registry.",
                        false,
                        (version) -> version <= PRE_MATERIAL_REWORK,
                        (stack) -> stack.rl.getNamespace().equals(GREGTECH_MODID) &&
                                LabsRemapHelper.META_ITEM_MATCHER.matcher(stack.rl.getPath()).matches() &&
                                !LabsRemapHelper.META_BLOCK_MATCHER.matcher(stack.rl.getPath()).matches() &&
                                stack.meta >= LabsRemapHelper.MIN_META_ITEM_BASE_ID,
                        (stack) -> stack.setMeta((short) (stack.meta - LabsRemapHelper.MIN_META_ITEM_BASE_ID))
                                .setRl(makeLabsName(stack.rl.getPath()))));

        itemFixes.add(
                new DataFix.ItemFix("Material Meta Blocks' Item Forms Remap",
                        "Remaps old Meta Blocks' Item Forms, from Custom Materials, to the new format and registry.",
                        false,
                        (version) -> version <= PRE_MATERIAL_REWORK,
                        (stack) -> LabsRemappers.metaBlockRemapper.shouldRemap(stack.rl),
                        (stack) -> stack.setRl(LabsRemappers.metaBlockRemapper.remapRl(stack.rl))));

        itemFixes.add(
                new DataFix.ItemFix("Material Special Meta Item Remap",
                        "Remaps old special placeable Meta Items, from Custom Materials, to the new format and registry.",
                        false,
                        (version) -> version <= PRE_MATERIAL_REWORK,
                        (stack) -> specialMetaItemsRemap.contains(stack.rl) &&
                                stack.meta >= LabsRemapHelper.MIN_META_ITEM_BASE_ID,
                        (stack) -> stack.setMeta((short) (stack.meta - LabsRemapHelper.MIN_META_ITEM_BASE_ID))
                                .setRl(makeLabsName(stack.rl.getPath()))));

        if (Loader.isModLoaded(AE2_STUFF_MODID))
            itemFixes.add(
                    new DataFix.ItemFix("AE2 Stuff Pattern Encoder Remap",
                            "Remaps AE2 Stuff Pattern Encoders, which were removed in AE2 Stuff Unofficial, to AE2 Interfaces.",
                            false,
                            (version) -> version <= PRE_AE2_STUFF_REMAP || version == NEW,
                            (stack) -> stack.rl.getNamespace().equals(AE2_STUFF_MODID) &&
                                    stack.rl.getPath().equals("encoder"),
                            (stack) -> stack.setRl(new ResourceLocation(AE2_MODID, "interface"))));

        /*
         * Block Fixes.
         * Note that this is not applied on any new blocks, placed or generated.
         * This fix is applied:
         * - On Placed Blocks
         *
         * Example of an input:
         * BlockStateLike:
         * rl: "minecraft:concrete" // Type: Resource Location. Not Null. Note that Remappers are Applied before Fixes!
         * meta: 1 // Type: Short
         *
         * Example:
         * blockFixes.add(
         * new DataFix.BlockFix("Example", // Name
         *
         * "Example Block Fix. Turns Oak Logs into Brown Concrete.", // Description
         *
         * false, // Whether the correct mode is required for the change to work right
         *
         * (version) -> version <= DEFAULT_VERSION, // Whether the previous version in the save means that this fix must
         * // be applied.
         * // Note that this is not included in the fix list if the previous
         * // version is equal to the current overall fix version.
         *
         * false, // Whether the tile entity tag is needed. If yes, it is accessible via state.setTileEntityTag() and
         * // state.tileEntityTag. If this is false, that field will be null.
         *
         * (state) -> state.rl.equals(new ResourceLocation("minecraft:log")) && state.meta == 0,
         * // Input BlockStateLike, return a boolean (true to fix, false to skip). You CAN NOT check the tile entity
         * here!
         *
         * null, // Secondary check, Input BlockStateLike, return a boolean (true to fix, false to skip). You CAN check
         * // the tile entity here! Input Null if checking tile entity is not needed.
         *
         * (state) -> state.setRl(new ResourceLocation("minecraft:concrete")).setMeta((short) 12),
         * // Change the given BlockStateLike (Changes to Brown Concrete in this case).
         * // You can also change the tile entity here if needed.
         * ));
         */
        blockFixes = new ObjectArrayList<>();

        blockFixes.add(
                new DataFix.BlockFix("Material Special Meta Block Remap",
                        "Remaps old special placeable Meta Blocks, from Custom Materials, to the new format and registry.",
                        false,
                        (version) -> version <= PRE_MATERIAL_REWORK,
                        true,
                        (state) -> specialMetaItemsRemap.contains(state.rl),
                        (state) -> state.tileEntityTag != null &&
                                specialMetaItemsRemap
                                        .contains(new ResourceLocation(state.tileEntityTag.getString("PipeBlock"))) &&
                                materialNames.contains(state.tileEntityTag.getString("PipeMaterial")),
                        (state) -> {
                            state.setRl(makeLabsName(state.rl.getPath()));

                            // noinspection DataFlowIssue
                            state.tileEntityTag.setString("PipeBlock",
                                    makeLabsName(
                                            new ResourceLocation(state.tileEntityTag.getString("PipeBlock")).getPath())
                                                    .toString());
                        }));

        if (Loader.isModLoaded(AE2_STUFF_MODID))
            blockFixes.add(
                    new DataFix.BlockFix("AE2 Stuff Pattern Encoder Remap",
                            "Remaps AE2 Stuff Pattern Encoders, which were removed in AE2 Stuff Unofficial, to AE2 Interfaces.",
                            false,
                            (version) -> version <= PRE_AE2_STUFF_REMAP || version == NEW,
                            true,
                            (state) -> state.rl.getNamespace().equals(AE2_MODID) &&
                                    state.rl.getPath().equals("interface") && state.meta != 0, // Apply if meta is not
                                                                                               // 0, interface can only
                                                                                               // be meta of 0
                            // Always apply regardless of TE Tag
                            null,
                            (state) -> {
                                state.setMeta((short) 0);

                                if (state.tileEntityTag == null ||
                                        !state.tileEntityTag.getString("id")
                                                .equals(new ResourceLocation(AE2_STUFF_MODID, "encoder").toString()))
                                    return;

                                state.tileEntityTag.setString("id",
                                        new ResourceLocation(AE2_MODID, "interface").toString());
                                state.tileEntityTag.setBoolean("omniDirectional", true); // Default setting, required

                                // Set a custom name
                                state.tileEntityTag.setString("customName", "Replaced Pattern Encoder");

                                NBTTagCompound patternTag = null;

                                // Check if we have to port patterns
                                if (!state.tileEntityTag.getTagList("Items", Constants.NBT.TAG_COMPOUND).isEmpty()) {
                                    NBTTagList tagList = state.tileEntityTag.getTagList("Items",
                                            Constants.NBT.TAG_COMPOUND);
                                    state.tileEntityTag.removeTag("Items");

                                    for (var slotBase : tagList) {
                                        // Edge Case 1
                                        if (slotBase.isEmpty() || !(slotBase instanceof NBTTagCompound slotTag))
                                            continue;

                                        // Edge Case 2
                                        if (!slotTag.hasKey("Slot", Constants.NBT.TAG_ANY_NUMERIC) ||
                                                !LabsRemapHelper.tagHasItemInfo(slotTag))
                                            continue;

                                        // Slot & Item Check
                                        byte slot = slotTag.getByte("Slot");
                                        ItemStackLike item = new ItemStackLike(slotTag);

                                        // Check if correct slot, and if correct item, and if count is higher than 0
                                        if (slot != 10 || !item.rl.getNamespace().equals(AE2_MODID) ||
                                                !item.rl.getPath().equals("material") ||
                                                item.meta != 52 || item.count <= 0)
                                            continue;

                                        patternTag = slotTag;
                                        break;
                                    }
                                }

                                var patterns = new NBTTagCompound();
                                var patternItems = new NBTTagList();

                                if (patternTag != null) {
                                    // Set new slot index
                                    patternTag.setInteger("Slot", 0);

                                    patternItems.appendTag(patternTag);
                                }

                                var infoTag = new ItemStack(LabsItems.INFO_ITEM, 1, ItemInfo.AE2_STUFF_REMAP_INFO)
                                        .writeToNBT(new NBTTagCompound());
                                infoTag.setInteger("Slot", patternItems.tagList.size()); // Should be 0 if no pattern, 1
                                                                                         // if has pattern
                                patternItems.appendTag(infoTag);

                                patterns.setTag("Items", patternItems);
                                state.tileEntityTag.setTag("patterns", patterns);
                            }));

        /*
         * Tile Entity Fixes.
         * Note that this is not applied on any new tile entities.
         * This fix is applied:
         * - On Existing Tile Entities' Tag Compounds.
         *
         * Example of an input:
         * {x:279,y:73,z:199,Items:[{Slot:0b,id:"minecraft:apple",Count:1,Damage:0s}],id:"minecraft:chest",Lock:"", ...}
         * // Type: NBT Tag Compound. Not Null.
         *
         * Notes:
         * - Item Fixes are called each item in this tile before this fix is applied.
         * - Do `/blockdata ~ ~-1 ~ {}` to get the Tile Entity Compound of the block you are standing on (in game)
         *
         * Example:
         * tileEntityFixes.add(
         * new DataFix.TileEntityFix("Example", // Name
         *
         * "Example Tile Entity Fix. Changes the amount of the all items in chests to be 64.", // Description
         *
         * false, // Whether the correct mode is required for the change to work right
         *
         * (version) -> version <= DEFAULT_VERSION,
         * // Whether the previous version in the save means that this fix must be applied.
         * // Note that this is not included in the fix list if the previous version is equal to the
         * // current overall fix version.
         *
         * // Input NBT Tag Compound, return a boolean (true to fix, false to skip)
         * (compound) -> compound.hasKey("id", Constants.NBT.TAG_STRING) &&
         * compound.getString("id").equals(new ResourceLocation("minecraft:chest").toString()) &&
         * compound.hasKey("Items", Constants.NBT.TAG_LIST) && !compound.getTagList("Items",
         * Constants.NBT.TAG_COMPOUND).isEmpty(),
         *
         * // Input NBT Tag Compound, transform it
         * (compound -> {
         * // Get Item List
         * NBTTagList itemList = compound.getTagList("Items", Constants.NBT.TAG_COMPOUND);
         * // Iterate through, changing count to 64
         * for (int i = 0; i < itemList.tagList.size(); i++) {
         * if (!(itemList.get(i) instanceof NBTTagCompound item)) continue;
         * item.setInteger("Count", 64);
         * itemList.set(i, item);
         * }
         * // Item List is modified directly, no need to set it back
         * })));
         */
        tileEntityFixes = new ObjectArrayList<>();

        tileEntityFixes.add(
                new DataFix.TileEntityFix("Old Multiblock Tile Entity Meta ID Remap",
                        "Remaps old Multiblock Tile Entity Names to the new format.",
                        false,
                        (version) -> version <= DEFAULT_NOMI_CEU,
                        (compound) -> compound.hasKey("MetaId", Constants.NBT.TAG_STRING) &&
                                compound.hasKey("id", Constants.NBT.TAG_STRING) &&
                                compound.getString("id").equals(
                                        new ResourceLocation(GREGTECH_MODID, "machine").toString()) &&
                                multiblockMetaIdRemap.containsKey(new ResourceLocation(compound.getString("MetaId"))),
                        (compound -> compound.setString("MetaId", multiblockMetaIdRemap
                                .get(new ResourceLocation(compound.getString("MetaId"))).toString()))));

        fixes = new Object2ObjectOpenHashMap<>();
        fixes.put(LabsFixTypes.FixerTypes.ITEM, itemFixes);
        fixes.put(LabsFixTypes.FixerTypes.CHUNK, blockFixes);
        fixes.put(LabsFixTypes.FixerTypes.TILE_ENTITY, tileEntityFixes);
    }

    private static void helpersInit() {
        // Special Meta Items Remap is only needed for some meta items.
        // They are all placeable, and do not have the special syntax that includes a base id.
        // Therefore, they must be remapped.
        // Placeable forms are... stored in Tile Entity. Handled by TileEntityMaterialPipeBaseMixin, and Tile Entity
        // Fix.
        specialMetaItemsRemap = new HashSet<>();
        // Wires and Cables
        // Fine Wire is not placeable, and thus follows normal meta item rules.
        for (var cableType : Insulation.VALUES) { // All Cable Types
            specialMetaItemsRemap.add(new ResourceLocation(GREGTECH_MODID, cableType.getName()));
        }
        // Fluid Pipes
        for (var fluidPipeType : FluidPipeType.VALUES) {
            specialMetaItemsRemap
                    .add(new ResourceLocation(GREGTECH_MODID, String.format("fluid_pipe_%s", fluidPipeType.name)));
        }
        // Item Pipes
        for (var itemPipeType : ItemPipeType.VALUES) {
            specialMetaItemsRemap
                    .add(new ResourceLocation(GREGTECH_MODID, String.format("item_pipe_%s", itemPipeType.name)));
        }
        // Compressed Blocks and Frames follow the special syntax.

        // Get all material names to know which tile entities to fix.
        materialNames = GregTechAPI.materialManager.getRegistry(LABS_MODID).getAllMaterials().stream()
                .map(Material::getName).collect(Collectors.toSet());

        multiblockMetaRemap = new Short2ShortLinkedOpenHashMap();
        multiblockMetaRemap.put((short) 32000, (short) 32100); // Microverse 1
        multiblockMetaRemap.put((short) 32001, (short) 32101); // Microverse 2
        multiblockMetaRemap.put((short) 32002, (short) 32102); // Microverse 3
        multiblockMetaRemap.put((short) 32003, (short) 32104); // Naq Reactor 1
        multiblockMetaRemap.put((short) 32004, (short) 32105); // Naq Reactor 2
        multiblockMetaRemap.put((short) 32005, (short) 32106); // Actualization Chamber
        multiblockMetaRemap.put((short) 32006, (short) 32107); // Universal Crystallizer

        multiblockMetaIdRemap = new Object2ObjectLinkedOpenHashMap<>();
        multiblockMetaIdRemap.put(
                new ResourceLocation(MBT_MODID, "microverse_projector_basic"), makeLabsName("microverse_projector_1"));
        multiblockMetaIdRemap.put(
                new ResourceLocation(MBT_MODID, "microverse_projector_advanced"),
                makeLabsName("microverse_projector_2"));
        multiblockMetaIdRemap.put(
                new ResourceLocation(MBT_MODID, "microverse_projector_advanced_ii"),
                makeLabsName("microverse_projector_3"));
        multiblockMetaIdRemap.put(
                new ResourceLocation(MBT_MODID, "creative_tank_provider"), makeLabsName("creative_tank_provider"));
        multiblockMetaIdRemap.put(
                new ResourceLocation(MULTIBLOCK_TWEAKER_MODID, "naquadah_reactor_1"),
                makeLabsName("naquadah_reactor_1"));
        multiblockMetaIdRemap.put(
                new ResourceLocation(MULTIBLOCK_TWEAKER_MODID, "naquadah_reactor_2"),
                makeLabsName("naquadah_reactor_2"));
        multiblockMetaIdRemap.put(
                new ResourceLocation(MULTIBLOCK_TWEAKER_MODID, "actualization_chamber"),
                makeLabsName("actualization_chamber"));
        multiblockMetaIdRemap.put(
                new ResourceLocation(MULTIBLOCK_TWEAKER_MODID, "universal_crystallizer"),
                makeLabsName("universal_crystallizer"));
        multiblockMetaIdRemap.put(
                new ResourceLocation(MULTIBLOCK_TWEAKER_MODID, "dml_sim_chamber"), makeLabsName("dme_sim_chamber"));

        capacitorSpecificationRemap = ImmutableMap.of(
                "compressedoctadiccapacitor",
                new OldCapacitorSpecification(4f, "Compressed Octadic RF Capacitor",
                        "This is what is known as a Compressed Octadic Capacitor.",
                        "Or, you could just call this an Octadic Capacitor Two.",
                        "Can be inserted into EnderIO machines.",
                        "Level: 4"),

                "doublecompressedoctadiccapacitor",
                new OldCapacitorSpecification(5f, "Double Compressed Octadic RF Capacitor",
                        "AND THIS IS TO GO EVEN FURTHER BEYOND!",
                        "Can be inserted into EnderIO machines.",
                        "Level: 9.001",
                        "Just kidding, it's only 5."));
    }

    public static class OldCapacitorSpecification {

        private static final String EIO_KEY = "eiocap";
        private static final String EIO_LEVEL_KEY = "level";
        private static final String DISPLAY_KEY = "display";
        private static final String NAME_KEY = "Name";
        private static final String LORE_KEY = "Lore";
        private final float level;
        private final String name;
        private final String[] lore;

        private OldCapacitorSpecification(float level, String name, String... lore) {
            this.level = level;
            this.name = name;
            this.lore = lore;
        }

        public boolean needChange(@Nullable NBTTagCompound compound) {
            if (compound == null || compound.isEmpty()) return false;
            return testEIO(compound) || testName(compound) || testLore(compound);
        }

        @Nullable
        public NBTTagCompound remove(@Nullable NBTTagCompound compound) {
            if (compound == null || compound.isEmpty()) return null;
            removeEIO(compound);
            removeDisplay(compound);
            return compound;
        }

        private void removeEIO(NBTTagCompound compound) {
            if (!testEIO(compound)) return;
            var eio = compound.getCompoundTag(EIO_KEY);
            eio.removeTag(EIO_LEVEL_KEY);
            if (eio.isEmpty()) compound.removeTag(EIO_KEY);
            else compound.setTag(EIO_KEY, eio);
        }

        private boolean testEIO(NBTTagCompound compound) {
            if (!compound.hasKey(EIO_KEY, Constants.NBT.TAG_COMPOUND)) return false;
            var eio = compound.getCompoundTag(EIO_KEY);
            if (!eio.hasKey(EIO_LEVEL_KEY, Constants.NBT.TAG_FLOAT)) return false;
            return eio.getFloat(EIO_LEVEL_KEY) == level;
        }

        private void removeDisplay(NBTTagCompound compound) {
            if (!compound.hasKey(DISPLAY_KEY, Constants.NBT.TAG_COMPOUND)) return;
            var display = compound.getCompoundTag(DISPLAY_KEY);
            if (testName(compound)) display.removeTag(NAME_KEY);
            if (testLore(compound)) display.removeTag(LORE_KEY);

            if (display.isEmpty()) compound.removeTag(DISPLAY_KEY);
            else compound.setTag(DISPLAY_KEY, display);
        }

        private boolean testName(NBTTagCompound compound) {
            if (!compound.hasKey(DISPLAY_KEY, Constants.NBT.TAG_COMPOUND)) return false;
            var display = compound.getCompoundTag(DISPLAY_KEY);
            if (!display.hasKey(NAME_KEY, Constants.NBT.TAG_STRING)) return false;
            return display.getString(NAME_KEY).equals(name);
        }

        private boolean testLore(NBTTagCompound compound) {
            if (!compound.hasKey(DISPLAY_KEY, Constants.NBT.TAG_COMPOUND)) return false;
            var display = compound.getCompoundTag(DISPLAY_KEY);
            if (!display.hasKey(LORE_KEY, Constants.NBT.TAG_LIST)) return false;
            var lore = display.getTagList(LORE_KEY, Constants.NBT.TAG_STRING);
            for (int i = 0; i < lore.tagList.size(); i++) {
                var tagStr = (NBTTagString) lore.tagList.get(i);
                var str = tagStr.getString();
                if (!str.equals(this.lore[i])) return false;
            }
            return true;
        }
    }
}
