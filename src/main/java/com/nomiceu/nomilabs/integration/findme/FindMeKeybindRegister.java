package com.nomiceu.nomilabs.integration.findme;

import net.minecraftforge.fml.client.registry.ClientRegistry;

import com.hjae.findme.proxy.ClientProxy;

/**
 * Registers the Find Fluids Keybind.
 */
public class FindMeKeybindRegister {

    public static void register() {
        ClientRegistry.registerKeyBinding(ClientProxy.KEY_FLUIDS);
    }
}
