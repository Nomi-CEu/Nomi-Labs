package com.nomiceu.nomilabs.mixin.simplyjetpacks;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tonius.simplyjetpacks.item.ItemMeta;
import tonius.simplyjetpacks.item.ItemMetaMods;
import tonius.simplyjetpacks.item.MetaItemsMods;
import tonius.simplyjetpacks.setup.ModItems;

import java.util.EnumSet;
import java.util.stream.Collectors;

/**
 * For Some Reason, Simply Jetpacks doesn't register some items if redstone arsenal integration is enabled.
 * <br>
 * This includes all TE Thrusters except for Fluxed, and Armor Platings. Not sure why.
 * <br>
 * This mixin fixes that.
 */
@SuppressWarnings("SimplifyStreamApiCallChains") // Not available till java 16, IntelliJ is dumb sometimes
@Mixin(value = ItemMetaMods.class, remap = false)
public class ItemMetaModsMixin extends ItemMeta {

    /**
     * Check that these EnumSet.range() calls still includes all needed items on mod update!
     * <br>
     * These are used so we can avoid FLUX_PLATE and FLUX_ARMOR_PLATING
     */
    @Unique
    private static final EnumSet<MetaItemsMods> PLATINGS_THRUSTERS = EnumSet.range(MetaItemsMods.ARMOR_PLATING_TE_1, MetaItemsMods.THRUSTER_TE_5); // Includes all Normal TE Armour Platings and Thrusters
    @Unique
    private static final EnumSet<MetaItemsMods> UNITS = EnumSet.range(MetaItemsMods.UNIT_GLOWSTONE_EMPTY, MetaItemsMods.UNIT_CRYOTHEUM); // Includes Glowstone and Cryotheum Units

    /**
     * Mandatory Ignored Constructor
     */
    public ItemMetaModsMixin(String registryName) {
        super(registryName);
    }

    @Inject(method = "getSubItems", at = @At("HEAD"), cancellable = true)
    public void getSubItems(CreativeTabs creativeTabs, NonNullList<ItemStack> list, CallbackInfo ci) {
        if (!isInCreativeTab(creativeTabs) || !ModItems.integrateTE || !ModItems.integrateRA) return; // This fix only applies if TE and RA Integration are Enabled
        if (ModItems.integrateVanilla) {
            list.addAll(MetaItemsMods.ITEMS_VANILLA.stream().map(
                    (item) -> new ItemStack(this, 1, item.ordinal())
            ).collect(Collectors.toList()));
        }
        if (ModItems.integrateEIO) {
            list.addAll(MetaItemsMods.ITEMS_EIO.stream().map(
                    (item) -> new ItemStack(this, 1, item.ordinal())
            ).collect(Collectors.toList()));
        }
        if (ModItems.integrateMek) {
            list.addAll(MetaItemsMods.ITEMS_MEK.stream().map(
                    (item) -> new ItemStack(this, 1, item.ordinal())
            ).collect(Collectors.toList()));
        }
        if (ModItems.integrateIE) {
            list.addAll(MetaItemsMods.ITEMS_IE.stream().map(
                    (item) -> new ItemStack(this, 1, item.ordinal())
            ).collect(Collectors.toList()));
        }
        // Since we know TE and RA Integration is on, no need to check, and no need to register Flux Plate or Flux Armor Plating
        // Use our custom lists
        list.addAll(PLATINGS_THRUSTERS.stream().map(
                (item) -> new ItemStack(this, 1, item.ordinal())
        ).collect(Collectors.toList()));
        list.addAll(UNITS.stream().map(
                (item) -> new ItemStack(this, 1, item.ordinal())
        ).collect(Collectors.toList()));

        // Don't let the normal func be called
        ci.cancel();
    }
}
