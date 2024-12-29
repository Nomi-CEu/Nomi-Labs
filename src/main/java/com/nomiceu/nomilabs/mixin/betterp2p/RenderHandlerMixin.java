package com.nomiceu.nomilabs.mixin.betterp2p;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.google.common.collect.ImmutableList;
import com.nomiceu.nomilabs.config.LabsConfig;
import com.nomiceu.nomilabs.integration.betterp2p.LabsClientCache;
import com.projecturanus.betterp2p.client.ClientCache;
import com.projecturanus.betterp2p.client.render.OutlineRenderer;
import com.projecturanus.betterp2p.client.render.RenderHandler;
import com.projecturanus.betterp2p.item.ItemAdvancedMemoryCard;

import kotlin.Pair;

/**
 * Adds different overlay colors for output/input, allows selected p2p to blink instead of be green.
 */
@Mixin(value = RenderHandler.class, remap = false)
public class RenderHandlerMixin {

    /**
     * Replace original outline handler.
     */
    @Inject(method = "renderOverlays", at = @At(value = "HEAD"), cancellable = true)
    private static void replaceOriginalOutlineHandler(RenderWorldLastEvent evt, CallbackInfo ci) {
        ci.cancel();
        EntityPlayer player = Minecraft.getMinecraft().player;
        ClientCache cache = ClientCache.INSTANCE;

        if (!(labs$heldIsMem(player, EnumHand.MAIN_HAND) || labs$heldIsMem(player, EnumHand.OFF_HAND))) return;

        if (cache.getPositions().isEmpty() && cache.getSelectedPosition() == null) return;

        boolean blinkSelected = LabsConfig.modIntegration.betterP2POptions.blinkP2P;
        int blinkSpeed = LabsConfig.modIntegration.betterP2POptions.blinkSpeed;

        if (cache.getSelectedPosition() != null) {
            List<Pair<BlockPos, EnumFacing>> selectedList = ImmutableList
                    .of(new Pair<>(cache.getSelectedPosition(), cache.getSelectedFacing()));

            if (!blinkSelected)
                OutlineRenderer.renderOutlinesWithFacing(evt, player, selectedList, 0x45, 0xDA, 0x75);
            else {
                // Flip should render if needed
                long time = System.currentTimeMillis();
                if (time - LabsClientCache.lastSelectedRenderChange > blinkSpeed) {
                    LabsClientCache.lastSelectedRenderChange = time;
                    LabsClientCache.renderingSelected = !LabsClientCache.renderingSelected;
                }

                if (LabsClientCache.renderingSelected) {
                    if (LabsClientCache.selectedIsOutput)
                        OutlineRenderer.renderOutlinesWithFacing(evt, player, selectedList, 0xec, 0xb3, 0x6c);
                    else
                        OutlineRenderer.renderOutlinesWithFacing(evt, player, selectedList, 0x6d, 0x9c, 0xf8);
                }
            }
        }

        OutlineRenderer.renderOutlinesWithFacing(evt, player, LabsClientCache.inputLoc, 0x6d, 0x9c, 0xf8);
        OutlineRenderer.renderOutlinesWithFacing(evt, player, LabsClientCache.outputLoc, 0xec, 0xb3, 0x6c);
    }

    @Unique
    private static boolean labs$heldIsMem(EntityPlayer player, EnumHand hand) {
        return player.getHeldItem(hand).getItem() instanceof ItemAdvancedMemoryCard;
    }
}
