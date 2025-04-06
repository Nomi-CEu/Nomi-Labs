package com.nomiceu.nomilabs.integration.ftbutilities.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.TextComponentTranslation;

import com.nomiceu.nomilabs.mixinhelper.AccessibleEntityPlayerSP;

public class DisplayGameOverlayMessageHelper {

    public static void sendMessageOrDisplay(EntityPlayer player, String key) {
        if (player instanceof EntityPlayerMP) {
            player.sendStatusMessage(new TextComponentTranslation(key), true);
        } else if (player instanceof AccessibleEntityPlayerSP) {
            ((AccessibleEntityPlayerSP) player).labs$getGuiIngame().addChatMessage(ChatType.GAME_INFO,
                    new TextComponentTranslation(key));
        }
    }
}
