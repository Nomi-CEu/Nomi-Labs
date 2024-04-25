package com.nomiceu.nomilabs.integration.effortlessbuilding;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import nl.requios.effortlessbuilding.EffortlessBuilding;
import nl.requios.effortlessbuilding.buildmode.BuildModes;
import nl.requios.effortlessbuilding.buildmode.ModeSettingsManager;
import nl.requios.effortlessbuilding.buildmodifier.ModifierSettingsManager;
import nl.requios.effortlessbuilding.network.RequestLookAtMessage;

@SuppressWarnings("unused")
public class EffortlessEventHandler {

    @SubscribeEvent
    public static void onPlayerInteract(PlayerInteractEvent.RightClickBlock event) {
        var player = event.getEntityPlayer();
        var stack = player.getHeldItem(event.getHand());
        if (!(stack.getItem() instanceof ItemBlock itemBlock)) return;

        BuildModes.BuildModeEnum buildMode = ModeSettingsManager.getModeSettings(player).getBuildMode();
        ModifierSettingsManager.ModifierSettings modifierSettings = ModifierSettingsManager.getModifierSettings(player);

        if (buildMode != BuildModes.BuildModeEnum.NORMAL) {
            event.setCanceled(true);
        } else if (modifierSettings.doQuickReplace()) {
            event.setCanceled(true);
            if (!event.getWorld().isRemote && player instanceof EntityPlayerMP playerMP) {
                EffortlessBuilding.packetHandler.sendTo(new RequestLookAtMessage(true), playerMP);
            }
        } else if (!event.getWorld().isRemote && player instanceof EntityPlayerMP playerMP) {
            EffortlessBuilding.packetHandler.sendTo(new RequestLookAtMessage(false), playerMP);
        }
    }
}
