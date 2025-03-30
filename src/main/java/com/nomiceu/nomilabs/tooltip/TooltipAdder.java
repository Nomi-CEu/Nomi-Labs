package com.nomiceu.nomilabs.tooltip;

import static com.nomiceu.nomilabs.util.LabsTranslate.*;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.jetbrains.annotations.Nullable;

import com.cleanroommc.groovyscript.helper.ingredient.IngredientHelper;
import com.enderio.core.client.handlers.SpecialTooltipHandler;
import com.jaquadro.minecraft.storagedrawers.item.ItemCompDrawers;
import com.jaquadro.minecraft.storagedrawers.item.ItemDrawers;
import com.nomiceu.nomilabs.LabsValues;
import com.nomiceu.nomilabs.config.LabsConfig;
import com.nomiceu.nomilabs.groovy.NBTClearingRecipe;
import com.nomiceu.nomilabs.integration.storagedrawers.CustomUpgradeHandler;
import com.nomiceu.nomilabs.util.ItemMeta;

import crazypants.enderio.api.capacitor.CapabilityCapacitorData;
import crazypants.enderio.base.capacitor.CapacitorKey;

@SideOnly(Side.CLIENT)
public class TooltipAdder {

    public static void addTooltipNormal(List<String> tooltip, ItemStack stack) {
        // Drawer Upgrade Notice
        if (stack.getTagCompound() != null && stack.getTagCompound().hasKey(CustomUpgradeHandler.CUSTOM_UPGRADES)) {
            if (stack.getItem() instanceof ItemDrawers || stack.getItem() instanceof ItemCompDrawers)
                tooltip.add(LabsTooltipHelper.DRAWER_UPDGRADE.translate());
        }

        // Custom Tooltips
        var groovyTooltips = LabsTooltipHelper.getTranslatableFromStack(stack);
        if (groovyTooltips != null)
            tooltip.addAll(groovyTooltips);

        // Add Information of EIO Capacitors' Levels
        if (Loader.isModLoaded(LabsValues.ENDER_IO_MODID) && LabsConfig.modIntegration.enableEnderIOIntegration)
            addTooltipEIO(tooltip, stack);
    }

    public static void addTooltipClearing(List<String> tooltip, ItemStack stack, EntityPlayer player) {
        if (player == null) return;

        InventoryCrafting inv = null;
        InventoryCraftResult result = null;

        if (player.openContainer instanceof ContainerWorkbench) {
            inv = ((ContainerWorkbench) player.openContainer).craftMatrix;
            result = ((ContainerWorkbench) player.openContainer).craftResult;
        } else if (player.openContainer instanceof ContainerPlayer) {
            inv = ((ContainerPlayer) player.openContainer).craftMatrix;
            result = ((ContainerPlayer) player.openContainer).craftResult;
        }

        if (inv == null || result == null) return;

        var resultStack = result.getStackInSlot(0);
        if (IngredientHelper.isEmpty(resultStack) || resultStack != stack) return;

        // Can't use GetRecipeUsed, because on client. Check matrix
        var resultItemMeta = new ItemMeta(resultStack);
        if (!NBTClearingRecipe.NBT_CLEARERS.containsKey(resultItemMeta)) return;

        var inputTooltips = NBTClearingRecipe.NBT_CLEARERS.get(resultItemMeta);
        var input = isNBTClearing(inv, inputTooltips);
        if (input != null)
            tooltip.add(inputTooltips.get(input).translate());
    }

    @Nullable
    private static ItemMeta isNBTClearing(InventoryCrafting inv, Map<ItemMeta, Translatable> pairs) {
        ItemMeta found = null;
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            var stack = inv.getStackInSlot(i);
            if (stack.isEmpty()) continue;

            if (found != null) return null;

            var itemMeta = new ItemMeta(stack);
            if (pairs.containsKey(itemMeta)) found = itemMeta;
            else return null;
        }
        return found;
    }

    @Optional.Method(modid = LabsValues.ENDER_IO_MODID)
    private static void addTooltipEIO(List<String> tooltip, ItemStack stack) {
        if (stack.hasCapability(CapabilityCapacitorData.getCapNN(), null)) {
            if (!SpecialTooltipHandler.showAdvancedTooltips()) return;

            var cap = Objects.requireNonNull(stack.getCapability(CapabilityCapacitorData.getCapNN(), null)); // Null
                                                                                                             // shouldn't
                                                                                                             // happen,
                                                                                                             // as
                                                                                                             // hasCapability
                                                                                                             // returned
                                                                                                             // true
            var level = cap.getUnscaledValue(CapacitorKey.NO_POWER);

            var formatter = new DecimalFormat("0.##"); // Format Levels to two decimal places (or less, this also
                                                       // removes trailing zeros)
            formatter.setRoundingMode(RoundingMode.HALF_UP); // Rounds up if in the middle (.5), else rounds to nearest

            // No clue what to use as the capacitor key, using NO_POWER, common declarations, in EnderIO and Nomi-Labs,
            // don't use that parameter anyway
            tooltip.add(translate("tooltip.nomilabs.capacitors.level", formatter.format(level)));
        }
    }
}
