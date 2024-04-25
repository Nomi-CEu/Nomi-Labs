package com.nomiceu.nomilabs.integration.ftbutilities.event;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import com.feed_the_beast.ftbutilities.events.chunks.ChunkModifiedEvent;
import com.nomiceu.nomilabs.integration.ftbutilities.CanEditChunkDataMessageHelper;

@SuppressWarnings("unused")
public class FTBUtilsEventHandler {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onPlayerJoined(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.player.world.isRemote || !(event.player instanceof EntityPlayerMP mp)) return;
        CanEditChunkDataMessageHelper.sendMessage(mp);
    }

    @SubscribeEvent
    public static void onPlayerClone(net.minecraftforge.event.entity.player.PlayerEvent.Clone event) {
        if (event.getEntityPlayer().world.isRemote || !(event.getEntityPlayer() instanceof EntityPlayerMP mp)) return;
        CanEditChunkDataMessageHelper.sendMessage(mp);
    }

    @SubscribeEvent
    public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (event.player.world.isRemote || !(event.player instanceof EntityPlayerMP mp)) return;
        CanEditChunkDataMessageHelper.sendMessage(mp);
    }

    @SubscribeEvent
    public static void onChunkChanged(EntityEvent.EnteringChunk event) {
        if (event.getEntity().world.isRemote || !(event.getEntity() instanceof EntityPlayerMP mp)) return;
        CanEditChunkDataMessageHelper.sendMessage(mp);
    }

    @SubscribeEvent
    public static void onChunkTeamClaimed(ChunkModifiedEvent.Claimed event) {
        CanEditChunkDataMessageHelper.sendMessageToAll();
    }

    @SubscribeEvent
    public static void onChunkTeamUnclaimed(ChunkModifiedEvent.Unclaimed event) {
        CanEditChunkDataMessageHelper.sendMessageToAll();
    }
}
