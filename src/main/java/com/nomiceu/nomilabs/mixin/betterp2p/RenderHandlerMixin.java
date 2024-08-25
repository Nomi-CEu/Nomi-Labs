package com.nomiceu.nomilabs.mixin.betterp2p;

import java.util.Collection;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.nomiceu.nomilabs.integration.betterp2p.LabsClientCache;
import com.projecturanus.betterp2p.client.render.OutlineRenderer;
import com.projecturanus.betterp2p.client.render.RenderHandler;

import kotlin.Pair;

/**
 * Adds different overlay colors for Output/Input.
 */
@Mixin(value = RenderHandler.class, remap = false)
public class RenderHandlerMixin {

    /**
     * Replace original outline handler.
     */
    @Redirect(method = "renderOverlays",
              at = @At(value = "INVOKE",
                       target = "Lcom/projecturanus/betterp2p/client/render/OutlineRenderer;renderOutlinesWithFacing(Lnet/minecraftforge/client/event/RenderWorldLastEvent;Lnet/minecraft/entity/player/EntityPlayer;Ljava/util/Collection;III)V",
                       ordinal = 1),
              require = 1)
    private static void replaceOriginalOutlineHandler(RenderWorldLastEvent evt, EntityPlayer p,
                                                      Collection<Pair<BlockPos, EnumFacing>> coordinates, int r, int g,
                                                      int b) {
        OutlineRenderer.renderOutlinesWithFacing(evt, p, LabsClientCache.inputLoc, 0x6d, 0x9c, 0xf8);
        OutlineRenderer.renderOutlinesWithFacing(evt, p, LabsClientCache.outputLoc, 0xec, 0xb3, 0x6c);
    }
}
