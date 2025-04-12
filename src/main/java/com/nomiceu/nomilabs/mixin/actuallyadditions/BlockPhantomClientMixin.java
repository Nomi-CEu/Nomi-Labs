package com.nomiceu.nomilabs.mixin.actuallyadditions;

import static com.nomiceu.nomilabs.util.LabsTranslate.translate;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.nomiceu.nomilabs.integration.actuallyadditions.AccessibleTileEntityPhantomface;

import de.ellpeck.actuallyadditions.api.tile.IPhantomTile;
import de.ellpeck.actuallyadditions.mod.blocks.BlockPhantom;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.util.GTUtility;

/**
 * Improves Phantomfaces' hover info, fixes bad display of MTE names.
 */
@Mixin(value = BlockPhantom.class, remap = false)
public abstract class BlockPhantomClientMixin {

    @Inject(method = "displayHud", at = @At("HEAD"), cancellable = true)
    private void betterHudDisplaying(Minecraft mc, EntityPlayer player, ItemStack stack,
                                     RayTraceResult posHit, ScaledResolution resolution, CallbackInfo ci) {
        ci.cancel();
        TileEntity te = mc.world.getTileEntity(posHit.getBlockPos());

        if (!(te instanceof IPhantomTile phantom)) return;

        FontRenderer font = mc.fontRenderer;
        float x = resolution.getScaledWidth() / 2f + 5;
        float y = resolution.getScaledHeight() / 2f - 40;
        int increaseBy = mc.fontRenderer.FONT_HEIGHT + 2;
        int maxWidth = 200;

        // Basic Info
        y = labs$draw(font, increaseBy, x, y, "aa.gui.hover.phantom.range", phantom.getRange());

        // Connection Status
        if (!phantom.hasBoundPosition()) {
            y = labs$draw(font, increaseBy, x, y, "aa.gui.hover.phantom.not_connected");
            labs$drawSplit(font, maxWidth, x, y, "aa.gui.hover.phantom.not_connected.info");
            return;
        }

        if (((phantom instanceof AccessibleTileEntityPhantomface access) && access.labs$boundValid()) ||
                phantom.isBoundThingInRange()) {
            y = labs$draw(font, increaseBy, x, y, "aa.gui.hover.phantom.connected");
        } else {
            y = labs$draw(font, increaseBy, x, y, "aa.gui.hover.phantom.connected_failed");
            y = labs$drawSplit(font, maxWidth, x, y, "aa.gui.hover.phantom.connected_failed.info");

            // Add a buffer
            y += increaseBy;
        }

        // Connection Info
        ItemStack boundStack;
        MetaTileEntity mte = GTUtility.getMetaTileEntity(mc.world, phantom.getBoundPosition());

        // Special case for GT MTEs
        if (mte != null) {
            boundStack = mte.getStackForm();
        } else {
            IBlockState state = mc.world.getBlockState(phantom.getBoundPosition());
            Block block = state.getBlock();

            // Can't use block.getPickBlock because we need a hitResult for that
            boundStack = new ItemStack(state.getBlock(), 1, block.getMetaFromState(state));
        }

        String name = boundStack.getDisplayName().trim();
        if (name.isEmpty())
            name = translate("aa.gui.hover.phantom.unknown_block");

        y = labs$draw(font, increaseBy, x, y, "aa.gui.hover.phantom.bound_block", name);

        BlockPos pos = phantom.getBoundPosition();
        y = labs$draw(font, increaseBy, x, y, "aa.gui.hover.phantom.bound_pos", pos.getX(), pos.getY(), pos.getZ());

        double distance = (new Vec3d(posHit.getBlockPos())).distanceTo(new Vec3d(phantom.getBoundPosition()));
        labs$draw(font, increaseBy, x, y, "aa.gui.hover.phantom.bound_dist", distance);
    }

    @Unique
    private float labs$draw(FontRenderer font, int increaseBy, float x, float y, String key, Object... params) {
        font.drawStringWithShadow(translate(key, params), x, y, 0xffffff);
        return y + increaseBy;
    }

    @Unique
    private float labs$drawSplit(FontRenderer font, int maxWidth, float x, float y, String key) {
        List<String> list = font.listFormattedStringToWidth(translate(key), maxWidth);

        for (int i = 0; i < list.size(); ++i) {
            font.drawString(list.get(i), x, y + i * font.FONT_HEIGHT, 0xffffff, true);
        }

        return y + list.size() * font.FONT_HEIGHT;
    }
}
