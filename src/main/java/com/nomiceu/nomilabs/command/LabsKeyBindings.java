package com.nomiceu.nomilabs.command;

import com.cleanroommc.groovyscript.network.CReload;
import com.cleanroommc.groovyscript.network.NetworkHandler;
import com.nomiceu.nomilabs.util.LabsSide;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.resource.VanillaResourceType;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;

public class LabsKeyBindings {
    private static KeyBinding reloadWithResourcesKey;
    private static long timeSinceLastUse;

    public static void onConstruction() {
        reloadWithResourcesKey = new KeyBinding("key.nomilabs.reload_with_language", KeyConflictContext.IN_GAME,
                KeyModifier.ALT, Keyboard.KEY_R, "key.categories.nomilabs");
        ClientRegistry.registerKeyBinding(reloadWithResourcesKey);
    }

    public static void onInput() {
        long time = Minecraft.getSystemTime();
        if (LabsSide.isDedicatedServer())
            Minecraft.getMinecraft().player.sendMessage(new TextComponentString("Reloading in multiplayer is currently not allowed to avoid desync."));
        if (Minecraft.getMinecraft().isIntegratedServerRunning() && LabsSide.isClient()
                && reloadWithResourcesKey.isPressed() && time - timeSinceLastUse >= 1000
                && Minecraft.getMinecraft().player.getPermissionLevel() >= 4) {
            timeSinceLastUse = time;
            var beginTime = System.currentTimeMillis();
            FMLClientHandler.instance().refreshResources(VanillaResourceType.LANGUAGES);
            beginTime = System.currentTimeMillis() - beginTime;
            Minecraft.getMinecraft().player.sendMessage(
                    new TextComponentString("Reloading Language Files took " + beginTime + "ms"));
            NetworkHandler.sendToServer(new CReload());
        }
    }
}
