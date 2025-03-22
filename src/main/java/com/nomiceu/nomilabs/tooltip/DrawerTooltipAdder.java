package com.nomiceu.nomilabs.tooltip;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;

import com.jaquadro.minecraft.storagedrawers.item.ItemCompDrawers;
import com.jaquadro.minecraft.storagedrawers.item.ItemDrawers;
import com.nomiceu.nomilabs.NomiLabs;
import com.nomiceu.nomilabs.integration.storagedrawers.CustomUpgradeHandler;
import com.nomiceu.nomilabs.util.ItemMeta;
import com.nomiceu.nomilabs.util.LabsTranslate;

public class DrawerTooltipAdder {

    public static void addDrawerInfo(List<String> tooltip, ItemStack stack) {
        if (!(stack.getItem() instanceof ItemDrawers || stack.getItem() instanceof ItemCompDrawers))
            return;

        NBTTagCompound nbt = stack.getTagCompound();
        if (nbt == null) return;

        // Drawer Stored Notice
        if (nbt.hasKey("tile")) {
            addDrawerStoredInfo(tooltip, nbt);
            return;
        }

        // Drawer Upgrade Notice
        if (nbt.hasKey(CustomUpgradeHandler.CUSTOM_UPGRADES)) {
            addDrawerUpgradeInfo(tooltip, nbt);
        }
    }

    private static void addDrawerStoredInfo(List<String> tooltip, NBTTagCompound compound) {
        NBTBase drawers = compound.getCompoundTag("tile").getTag("Drawers");
        // noinspection ConstantValue Drawers might be null if drawers key is missing, according to my understanding
        if (drawers == null || drawers.isEmpty()) return;

        if (!LabsTooltipHelper.isShiftDown()) {
            tooltip.add(LabsTranslate.translate("tooltip.nomilabs.drawers.stored_info"));
            return;
        }

        addDrawerUpgrades(tooltip, compound.getCompoundTag("tile").getTagList("Upgrades", Constants.NBT.TAG_COMPOUND));
        if (drawers instanceof NBTTagCompound comp)
            addCompactingDrawerStored(tooltip, comp);
        else if (drawers instanceof NBTTagList list)
            addNormalDrawerStored(tooltip, list);
        // What?
        else
            NomiLabs.LOGGER.fatal("Unknown stored drawer info of type: {} ({})", drawers.getId(),
                    drawers.getClass().getName());
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

    private static void addDrawerUpgradeInfo(List<String> tooltip, NBTTagCompound compound) {
        tooltip.add(LabsTranslate.translate("tooltip.nomilabs.drawers.upgrades"));
        if (!LabsTooltipHelper.isShiftDown()) {
            tooltip.add(LabsTranslate.translate("tooltip.nomilabs.drawers.upgrades_info"));
            return;
        }

        NBTTagList list = compound.getCompoundTag(CustomUpgradeHandler.CUSTOM_UPGRADES).getTagList("Upgrades",
                Constants.NBT.TAG_COMPOUND);
        addDrawerUpgrades(tooltip, list);
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
            tooltip.add(LabsTranslate.translate("tooltip.nomilabs.drawers.item_upgrade",
                    upgrade.getKey().toStack().getDisplayName(), upgrade.getValue()));
        }
    }
}
