package com.nomiceu.nomilabs.mixin.betterp2p;

import java.awt.*;
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

    @Unique
    private static final Color labs$selected = new Color(0x45DA75);

    @Unique
    private static final Color labs$input = new Color(0x6D9CF8);

    @Unique
    private static final Color labs$output = new Color(0xECB36C);

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
                labs$renderOutline(evt, player, selectedList, labs$selected);
            else {
                // Flip should render if needed
                long time = System.currentTimeMillis();
                if (time - LabsClientCache.lastSelectedRenderChange > blinkSpeed) {
                    LabsClientCache.lastSelectedRenderChange = time;
                    LabsClientCache.renderingSelected = !LabsClientCache.renderingSelected;
                }

                if (LabsClientCache.renderingSelected) {
                    if (LabsClientCache.selectedIsOutput)
                        labs$renderOutline(evt, player, selectedList, labs$output);
                    else
                        labs$renderOutline(evt, player, selectedList, labs$input);
                }
            }
        }

        labs$renderOutline(evt, player, LabsClientCache.inputLoc, labs$input);
        labs$renderOutline(evt, player, LabsClientCache.outputLoc, labs$output);
    }

    @Unique
    private static void labs$renderOutline(RenderWorldLastEvent evt, EntityPlayer player,
                                           List<Pair<BlockPos, EnumFacing>> info, Color color) {
        OutlineRenderer.renderOutlinesWithFacing(evt, player, info, color.getRed(), color.getGreen(), color.getBlue());
    }

    @Unique
    private static boolean labs$heldIsMem(EntityPlayer player, EnumHand hand) {
        return player.getHeldItem(hand).getItem() instanceof ItemAdvancedMemoryCard;
    }
}
