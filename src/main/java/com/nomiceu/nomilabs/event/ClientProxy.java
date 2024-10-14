package com.nomiceu.nomilabs.event;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.cleanroommc.groovyscript.event.ScriptRunEvent;
import com.nomiceu.nomilabs.LabsTextures;
import com.nomiceu.nomilabs.LabsValues;
import com.nomiceu.nomilabs.NomiLabs;
import com.nomiceu.nomilabs.fluid.registry.LabsFluids;
import com.nomiceu.nomilabs.gregtech.block.registry.LabsMetaBlocks;
import com.nomiceu.nomilabs.integration.betterp2p.ModeDescriptionsHandler;
import com.nomiceu.nomilabs.integration.betterquesting.LabsTierHelper;
import com.nomiceu.nomilabs.integration.findme.FindMeKeybindRegister;
import com.nomiceu.nomilabs.item.registry.LabsItems;
import com.nomiceu.nomilabs.network.LabsNetworkHandler;
import com.nomiceu.nomilabs.network.LabsP2PCycleMessage;
import com.nomiceu.nomilabs.tooltip.LabsTooltipHelper;
import com.nomiceu.nomilabs.tooltip.TooltipAdder;
import com.nomiceu.nomilabs.util.ItemMeta;

/*
 * Every texture is registered, in case something in that registry, not in that config, is enabled.
 * Nothing happens if each classes registries are empty.
 */
@Mod.EventBusSubscriber(value = Side.CLIENT, modid = LabsValues.LABS_MODID)
@SideOnly(Side.CLIENT)
@SuppressWarnings("unused")
public class ClientProxy {

    public static void earlyPreInit() {
        LabsTextures.preInit();
    }

    public static void latePreInit() {
        if (Loader.isModLoaded(LabsValues.BQU_MODID))
            LabsTierHelper.preInit();
    }

    public static void postInit() {
        // Load EnderIO Keybinds, Make Sure Loaded Before Groovy Keybind Overrides
        if (Loader.isModLoaded(LabsValues.ENDER_IO_MODID)) {
            try {
                Class.forName("crazypants.enderio.base.handler.KeyTracker");
            } catch (ClassNotFoundException e) {
                NomiLabs.LOGGER.error(
                        "Failed to load EnderIO's KeyTracker Class! Overrides for Ender IO Keybindings may not be available!");
            }
        }

        // Register Find Me's Fluid Keyubind
        if (Loader.isModLoaded(LabsValues.FIND_ME_MODID))
            FindMeKeybindRegister.register();
    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        LabsItems.registerModels();
        LabsFluids.registerFluidBlockModels();
        LabsMetaBlocks.registerModels();
    }

    @SubscribeEvent
    public static void registerFluidModels(TextureStitchEvent.Pre event) {
        LabsFluids.registerFluidModels(event);
    }

    @SubscribeEvent
    public static void addTooltipNormal(ItemTooltipEvent event) {
        TooltipAdder.addTooltipNormal(event.getToolTip(), event.getItemStack());
        TooltipAdder.addTooltipClearing(event.getToolTip(), event.getItemStack(), event.getEntityPlayer());
    }

    @SubscribeEvent
    public static void languageChanged(LabsResourcesRefreshedEvent event) {
        if (Loader.isModLoaded(LabsValues.BETTER_P2P_MODID))
            ModeDescriptionsHandler.refreshDescriptions();
    }

    @SubscribeEvent
    public static void afterScriptLoad(ScriptRunEvent.Post event) {
        NomiLabs.LOGGER.info("Reloading Options File.");
        FMLClientHandler.instance().getClient().gameSettings.loadOptions();
    }

    @SubscribeEvent
    public static void onMouseEvent(MouseEvent event) {
        if (!Loader.isModLoaded(LabsValues.AE2_MODID)) return;

        int scroll = event.getDwheel();
        if (scroll == 0 || !LabsTooltipHelper.isShiftDown()) return;
        byte offset = (byte) (scroll < 0 ? 1 : -1);

        // Handle P2P Scroll
        Minecraft minecraft = Minecraft.getMinecraft();
        EntityPlayer player = minecraft.player;
        if (player == null || minecraft.currentScreen != null) return;

        if (LabsP2PCycleMessage.MessageHandler.getP2ps()
                .containsKey(new ItemMeta(player.getHeldItem(EnumHand.MAIN_HAND)))) {
            LabsNetworkHandler.NETWORK_HANDLER
                    .sendToServer(new LabsP2PCycleMessage(player, EnumHand.MAIN_HAND, offset));
            event.setCanceled(true);
        } else if (LabsP2PCycleMessage.MessageHandler.getP2ps()
                .containsKey(new ItemMeta(player.getHeldItem(EnumHand.OFF_HAND)))) {
                    LabsNetworkHandler.NETWORK_HANDLER
                            .sendToServer(new LabsP2PCycleMessage(player, EnumHand.OFF_HAND, offset));
                    event.setCanceled(true);
                }
    }
}
