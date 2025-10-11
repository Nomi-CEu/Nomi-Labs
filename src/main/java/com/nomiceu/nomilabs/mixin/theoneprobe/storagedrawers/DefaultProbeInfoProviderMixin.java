package com.nomiceu.nomilabs.mixin.theoneprobe.storagedrawers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.jaquadro.minecraft.storagedrawers.block.tile.TileEntityDrawers;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.nomiceu.nomilabs.integration.top.LabsCustomCountItemStackElement;
import com.nomiceu.nomilabs.util.ItemTagMeta;

import it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenHashMap;
import mcjty.theoneprobe.Tools;
import mcjty.theoneprobe.api.*;
import mcjty.theoneprobe.apiimpl.providers.DefaultProbeInfoProvider;
import mcjty.theoneprobe.apiimpl.styles.LayoutStyle;
import mcjty.theoneprobe.config.Config;

/**
 * Replaces the display of the extended chest mode for storage drawers.
 * Heavily inspired by {@link io.github.drmanganese.topaddons.addons.AddonStorageDrawers}.
 */
@Mixin(value = DefaultProbeInfoProvider.class, remap = false)
public class DefaultProbeInfoProviderMixin {

    @WrapOperation(method = "addProbeInfo",
                   at = @At(value = "INVOKE",
                            target = "Lmcjty/theoneprobe/apiimpl/providers/ChestInfoTools;showChestInfo(Lmcjty/theoneprobe/api/ProbeMode;Lmcjty/theoneprobe/api/IProbeInfo;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lmcjty/theoneprobe/api/IProbeConfig;)V"),
                   require = 1)
    private static void displayStorageDrawers(ProbeMode mode, IProbeInfo info, World world, BlockPos pos,
                                              IProbeConfig config, Operation<Void> original,
                                              @Local(argsOnly = true, ordinal = 0) EntityPlayer player) {
        TileEntity te = world.getTileEntity(pos);
        if (!(te instanceof TileEntityDrawers drawer)) {
            original.call(mode, info, world, pos, config);
            return;
        }

        if (drawer.isSealed()) return;
        if (drawer.getOwner() != null && !drawer.getOwner().equals(player.getUniqueID())) return;

        var group = drawer.getGroup();

        List<ItemStack> stacks = new ArrayList<>();
        for (int i = 0; i < group.getDrawerCount(); i++) {
            ItemStack stack = group.getDrawer(i).getStoredItemPrototype();
            if (!stack.isEmpty()) {
                stack = stack.copy();
                stack.setCount(group.getDrawer(i).getStoredItemCount());
                stacks.add(stack);
            }
        }

        if (stacks.isEmpty()) return;

        IProbeInfo vertical = info.vertical(
                new LayoutStyle().borderColor(Config.chestContentsBorderColor).spacing(0));

        if (Tools.show(mode, Config.getRealConfig().getShowChestContentsDetailed()) &&
                stacks.size() <= Config.showItemDetailThresshold) {
            var horizontalStyle = new LayoutStyle().spacing(8).alignment(ElementAlignment.ALIGN_CENTER);
            var verticalStyle = new LayoutStyle().spacing(0);

            // Expanded view, no need to condense
            for (ItemStack stack : stacks) {
                if (drawer.getDrawerAttributes().isUnlimitedVending()) {
                    stack.setCount(1);
                    vertical.horizontal(horizontalStyle)
                            .element(new LabsCustomCountItemStackElement(stack, "∞"))
                            .vertical(verticalStyle)
                            .itemLabel(stack)
                            .text(TextStyleClass.INFOIMP + "[∞]");
                    continue;
                }
                if (stack.isEmpty()) {
                    // Locked but empty
                    stack.setCount(1);
                    vertical.horizontal(horizontalStyle)
                            .element(new LabsCustomCountItemStackElement(stack, "0"))
                            .vertical(verticalStyle)
                            .itemLabel(stack)
                            .text(TextStyleClass.LABEL + "[0]");
                    continue;
                }

                int mss = stack.getMaxStackSize();
                int r = stack.getCount() % mss;
                int q = (stack.getCount() - r) / mss;
                vertical.horizontal(horizontalStyle)
                        .item(stack)
                        .vertical(verticalStyle)
                        .itemLabel(stack)
                        .text(TextStyleClass.LABEL + "[" + (stack.getCount() >= mss ? q + "x" + mss + " + " : "") + r +
                                "]");
            }

            return;
        }

        // Condensed view, try to condense (use a linked map to preserve order)
        Map<ItemTagMeta, Integer> condensed = new Object2IntLinkedOpenHashMap<>();
        for (ItemStack stack : stacks) {
            int count = stack.getCount();

            if (stack.isEmpty()) // Allow stack data to be fetched
                stack.setCount(1);

            condensed.merge(new ItemTagMeta(stack), count, Integer::sum);
        }

        IProbeInfo horizontal = null;
        int rows = 0;
        int idx = 0;
        for (var entry : condensed.entrySet()) {
            if (idx % 10 == 0) {
                horizontal = vertical.horizontal(new LayoutStyle().spacing(0));
                rows++;
                if (rows > 4) break;
            }

            // Locked (but empty) item
            if (entry.getValue() == 0) {
                horizontal.element(new LabsCustomCountItemStackElement(entry.getKey().toStack(), "0"));
            } else if (drawer.getDrawerAttributes().isUnlimitedVending()) {
                horizontal.element(new LabsCustomCountItemStackElement(entry.getKey().toStack(), "∞"));
            } else {
                horizontal.item(entry.getKey().toStack(entry.getValue()));
            }
            idx++;
        }
    }
}
