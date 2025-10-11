package com.nomiceu.nomilabs.tooltip;

import java.util.*;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.Loader;

import com.jaquadro.minecraft.storagedrawers.item.ItemCompDrawers;
import com.jaquadro.minecraft.storagedrawers.item.ItemDrawers;
import com.nomiceu.nomilabs.LabsValues;
import com.nomiceu.nomilabs.NomiLabs;
import com.nomiceu.nomilabs.integration.storagedrawers.CustomUpgradeHandler;
import com.nomiceu.nomilabs.util.ItemMeta;
import com.nomiceu.nomilabs.util.LabsTranslate;

import eutros.framedcompactdrawers.item.ItemControllerCustom;
import eutros.framedcompactdrawers.item.ItemSlaveCustom;

public class DrawerTooltipAdder {

    public static void addDrawerInfo(List<String> tooltip, ItemStack stack) {
        Item item = stack.getItem();

        // Add tooltip if drawers, compacting drawers
        if (!(item instanceof ItemDrawers || item instanceof ItemCompDrawers))
            return;

        if (Loader.isModLoaded(LabsValues.FRAMED_COMPACT_MODID)) {
            if (disallowDrawer(item)) return;
        }

        NBTTagCompound nbt = stack.getTagCompound();
        if (nbt == null) return;

        boolean shouldPrintShiftMsg = false;

        // Main Tooltip Handling
        if (nbt.hasKey("tile")) {
            shouldPrintShiftMsg = addDrawerTileInfo(tooltip, nbt);
        } else if (nbt.hasKey(CustomUpgradeHandler.CUSTOM_UPGRADES)) {
            shouldPrintShiftMsg = addDrawerUpgradeInfo(tooltip, nbt);
        }

        // Shift Info Handling
        if (shouldPrintShiftMsg) {
            tooltip.add(LabsTranslate.translate("tooltip.nomilabs.drawers.shift"));
        }
    }

    private static boolean disallowDrawer(Item item) {
        // Disallow framed controllers/slaves in tooltip (doesn't inherit from ItemDrawers)
        return item instanceof ItemControllerCustom || item instanceof ItemSlaveCustom;
    }

    private static boolean addDrawerTileInfo(List<String> tooltip, NBTTagCompound compound) {
        if (!tooltip.isEmpty()) {
            // Clear old stored info tooltip
            String target = TextFormatting.YELLOW + LabsTranslate.translate("storagedrawers.drawers.sealed");
            List<String> removed = new ArrayList<>();
            for (int i = tooltip.size() - 1; i >= 0; i--) {
                String line = tooltip.remove(i);
                if (line.equals(target)) {
                    break;
                }

                removed.add(line);
            }

            if (!removed.isEmpty()) {
                for (int i = removed.size() - 1; i >= 0; i--) {
                    tooltip.add(removed.get(i));
                }
            }
        }

        boolean result;
        result = addDrawerTileUpgradeInfo(tooltip, compound);
        result = addDrawerStoredInfo(tooltip, compound) || result;
        return result;
    }

    private static boolean addDrawerTileUpgradeInfo(List<String> tooltip, NBTTagCompound compound) {
        NBTTagList upgrades = compound.getCompoundTag("tile").getTagList("Upgrades", Constants.NBT.TAG_COMPOUND);

        if (upgrades.isEmpty()) return false;

        if (!LabsTooltipHelper.isShiftDown()) {
            tooltip.add(LabsTranslate.translate("tooltip.nomilabs.drawers.upgrades"));
            return true;
        } else
            tooltip.add(LabsTranslate.translate("tooltip.nomilabs.drawers.upgrades_active"));

        addDrawerUpgrades(tooltip, upgrades);
        return false;
    }

    private static boolean addDrawerStoredInfo(List<String> tooltip, NBTTagCompound compound) {
        NBTBase drawers = compound.getCompoundTag("tile").getTag("Drawers");
        // noinspection ConstantValue Drawers might be null if drawers key is missing, according to my understanding
        if (drawers == null || drawers.isEmpty()) return false;

        // Special case for drawer lists: it can contain (empty) NBT Tag Compound
        if (drawers instanceof NBTTagList list && list.get(0).isEmpty()) return false;
        // Special case for drawer compounds: count can be 0
        else if (drawers instanceof NBTTagCompound comp && comp.getInteger("Count") <= 0) return false;

        if (!LabsTooltipHelper.isShiftDown()) {
            tooltip.add(LabsTranslate.translate("tooltip.nomilabs.drawers.stored"));
            return true;
        } else
            tooltip.add(LabsTranslate.translate("tooltip.nomilabs.drawers.stored_active"));

        if (drawers instanceof NBTTagCompound comp)
            addCompactingDrawerStored(tooltip, comp);
        else if (drawers instanceof NBTTagList list)
            addNormalDrawerStored(tooltip, list);
        else
            // What?
            NomiLabs.LOGGER.fatal("Unknown stored drawer info of type: {} ({})", drawers.getId(),
                    drawers.getClass().getName());

        return false;
    }

    private static void addNormalDrawerStored(List<String> tooltip, NBTTagList drawers) {
        for (NBTBase each : drawers) {
            var compound = (NBTTagCompound) each;
            int count = compound.getInteger("Count");

            var stack = new ItemStack(compound.getCompoundTag("Item"));
            if (stack.isEmpty()) continue;

            tooltip.add(LabsTranslate.translate("tooltip.nomilabs.drawers.item", stack.getDisplayName(), count));
        }
    }

    private static void addCompactingDrawerStored(List<String> tooltip, NBTTagCompound drawers) {
        int count = drawers.getInteger("Count");

        NBTTagList items = drawers.getTagList("Items", Constants.NBT.TAG_COMPOUND);
        for (NBTBase each : items) {
            var compound = (NBTTagCompound) each;

            int conv = compound.getInteger("Conv");
            if (conv <= 0) continue;

            int amount = count / conv;
            if (amount <= 0) continue;

            var stack = new ItemStack(compound.getCompoundTag("Item"));
            if (stack.isEmpty()) continue;

            tooltip.add(LabsTranslate.translate("tooltip.nomilabs.drawers.item", stack.getDisplayName(), amount));
        }
    }

    private static boolean addDrawerUpgradeInfo(List<String> tooltip, NBTTagCompound compound) {
        NBTTagList list = compound.getCompoundTag(CustomUpgradeHandler.CUSTOM_UPGRADES).getTagList("Upgrades",
                Constants.NBT.TAG_COMPOUND);
        if (list.isEmpty()) return false;

        if (!LabsTooltipHelper.isShiftDown()) {
            tooltip.add(LabsTranslate.translate("tooltip.nomilabs.drawers.upgrades"));
            return true;
        } else {
            tooltip.add(LabsTranslate.translate("tooltip.nomilabs.drawers.upgrades_active"));
        }

        addDrawerUpgrades(tooltip, list);
        return false;
    }

    private static void addDrawerUpgrades(List<String> tooltip, NBTTagList list) {
        Map<ItemMeta, Integer> upgrades = new HashMap<>(7);

        for (NBTBase each : list) {
            var stack = new ItemStack((NBTTagCompound) each);
            if (stack.isEmpty()) continue;

            var meta = new ItemMeta(stack);

            int currValue = upgrades.getOrDefault(meta, 0);
            upgrades.put(meta, currValue + 1);
        }

        for (var upgrade : upgrades.entrySet()) {
            tooltip.add(LabsTranslate.translate("tooltip.nomilabs.drawers.item",
                    upgrade.getKey().toStack().getDisplayName(), upgrade.getValue()));
        }
    }
}
