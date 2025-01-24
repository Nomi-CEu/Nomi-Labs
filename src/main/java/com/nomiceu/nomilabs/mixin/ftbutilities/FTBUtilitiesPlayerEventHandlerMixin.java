package com.nomiceu.nomilabs.mixin.ftbutilities;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.feed_the_beast.ftbutilities.handlers.FTBUtilitiesPlayerEventHandler;
import com.nomiceu.nomilabs.integration.ftbutilities.CanEditChunkHelper;
import com.nomiceu.nomilabs.integration.ftbutilities.network.DisplayGameOverlayMessageHelper;

@Mixin(value = FTBUtilitiesPlayerEventHandler.class, remap = false)
public class FTBUtilitiesPlayerEventHandlerMixin {

    @Unique
    private static boolean labs$cancelledMainHand = false;

    /* New Logic */

    @Inject(method = "onRightClickBlock", at = @At("HEAD"), cancellable = true)
    @SuppressWarnings("deprecation")
    private static void handleAndCancelPlace(PlayerInteractEvent.RightClickBlock event, CallbackInfo ci) {
        var stack = event.getEntityPlayer().getHeldItem(event.getHand());
        var item = stack.getItem();
        var world = event.getWorld();
        var pos = event.getPos();

        if (event.getHand() == EnumHand.MAIN_HAND)
            labs$cancelledMainHand = false;
        // Off Hand is always called after main hand
        else if (event.getHand() == EnumHand.OFF_HAND && labs$cancelledMainHand) {
            labs$cancelledMainHand = false;
            event.setCanceled(true);
            ci.cancel();
            return;
        }

        if (!(item instanceof ItemBlock itemBlock))
            return;

        // Offset Pos if Needed
        if (event.getFace() != null && !world.getBlockState(pos).getBlock().isReplaceable(world, pos))
            pos = pos.offset(event.getFace());

        if (!CanEditChunkHelper.cannotEditChunk(event.getEntityPlayer(),
                pos, itemBlock.getBlock().getStateFromMeta(stack.getMetadata())))
            return;

        event.setCanceled(true);
        labs$cancelledMainHand = true;

        if (!event.getWorld().isRemote)
            DisplayGameOverlayMessageHelper.sendMessageOrDisplay(event.getEntityPlayer(),
                    "ftbutilities.status.chunk.no_place_block");
        ci.cancel();
    }

    @Redirect(method = "onBlockLeftClick",
              at = @At(value = "INVOKE",
                       target = "Lcom/feed_the_beast/ftbutilities/data/ClaimedChunks;blockBlockEditing(Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/state/IBlockState;)Z"),
              require = 1)
    private static boolean blockBlockEditingRedirect(EntityPlayer player, BlockPos pos, IBlockState state) {
        return CanEditChunkHelper.cannotEditChunk(player, pos, state);
    }

    /* Status Message Adders */

    @Inject(method = "onEntityAttacked", at = @At("TAIL"))
    private static void sendStatusEntity(AttackEntityEvent event, CallbackInfo ci) {
        if (!event.isCanceled() || event.getEntityPlayer().world == null)
            return;

        if (!event.getEntityPlayer().world.isRemote) {
            if (event.getTarget() instanceof EntityPlayer)
                DisplayGameOverlayMessageHelper.sendMessageOrDisplay(event.getEntityPlayer(),
                        "ftbutilities.status.server.pvp");
            else
                DisplayGameOverlayMessageHelper.sendMessageOrDisplay(event.getEntityPlayer(),
                        "ftbutilities.status.chunk.no_attack");
        }
    }

    @Inject(method = "onBlockLeftClick", at = @At("TAIL"))
    private static void sendStatusBlockBreak(PlayerInteractEvent.LeftClickBlock event, CallbackInfo ci) {
        if (!event.isCanceled() || event.getEntityPlayer().world == null) return;

        if (!event.getEntityPlayer().world.isRemote)
            DisplayGameOverlayMessageHelper.sendMessageOrDisplay(event.getEntityPlayer(),
                    "ftbutilities.status.chunk.no_break_block");
    }

    @Inject(method = "onRightClickItem",
            at = @At(value = "INVOKE",
                     target = "Lcom/feed_the_beast/ftblib/lib/util/InvUtils;forceUpdate(Lnet/minecraft/entity/player/EntityPlayer;)V",
                     shift = At.Shift.AFTER),
            require = 1)
    private static void sendStatusItemUse(PlayerInteractEvent.RightClickItem event, CallbackInfo ci) {
        if (!event.getEntityPlayer().world.isRemote)
            DisplayGameOverlayMessageHelper.sendMessageOrDisplay(event.getEntityPlayer(),
                    "ftbutilities.status.chunk.no_interact_item");
    }

    @Inject(method = "onRightClickBlock",
            at = @At(value = "INVOKE",
                     target = "Lcom/feed_the_beast/ftblib/lib/util/InvUtils;forceUpdate(Lnet/minecraft/entity/player/EntityPlayer;)V",
                     shift = At.Shift.AFTER),
            require = 1)
    private static void sendStatusBlockInteraction(PlayerInteractEvent.RightClickBlock event, CallbackInfo ci) {
        if (!event.getEntityPlayer().world.isRemote)
            DisplayGameOverlayMessageHelper.sendMessageOrDisplay(event.getEntityPlayer(),
                    "ftbutilities.status.chunk.no_interact_block");
    }

    /* Status Message Changers */

    @Redirect(method = "onRightClickItem",
              at = @At(value = "INVOKE",
                       target = "Lnet/minecraft/entity/player/EntityPlayer;sendStatusMessage(Lnet/minecraft/util/text/ITextComponent;Z)V",
                       remap = true),
              require = 1)
    private static void sendTranslatableStatusMessageItem(EntityPlayer instance, ITextComponent chatComponent,
                                                          boolean actionBar) {
        // World is Already Not Remote Here
        DisplayGameOverlayMessageHelper.sendMessageOrDisplay(instance, "ftbutilities.status.server.item_disabled");
    }

    @Redirect(method = "onRightClickBlock",
              at = @At(value = "INVOKE",
                       target = "Lnet/minecraft/entity/player/EntityPlayer;sendStatusMessage(Lnet/minecraft/util/text/ITextComponent;Z)V",
                       remap = true),
              require = 1)
    private static void sendTranslatableStatusMessageBlock(EntityPlayer instance, ITextComponent chatComponent,
                                                           boolean actionBar) {
        // World is Already Not Remote Here
        DisplayGameOverlayMessageHelper.sendMessageOrDisplay(instance, "ftbutilities.status.server.item_disabled");
    }

    /* Removals */

    @Inject(method = "onBlockBreak", at = @At("HEAD"), cancellable = true)
    private static void cancelOriginalBreakHandler(BlockEvent.BreakEvent event, CallbackInfo ci) {
        ci.cancel();
    }

    @Inject(method = "onBlockPlace", at = @At("HEAD"), cancellable = true)
    @SuppressWarnings("deprecation")
    private static void cancelOriginalPlaceHandler(BlockEvent.PlaceEvent event, CallbackInfo ci) {
        ci.cancel();
    }
}
