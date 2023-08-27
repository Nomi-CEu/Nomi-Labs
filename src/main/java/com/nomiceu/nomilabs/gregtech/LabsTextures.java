package com.nomiceu.nomilabs.gregtech;

import gregtech.client.renderer.texture.cube.OrientedOverlayRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LabsTextures {
    public static OrientedOverlayRenderer GREENHOUSE_OVERLAY;

    public static void preInit() {
        GREENHOUSE_OVERLAY = new OrientedOverlayRenderer("nomilabs:multiblock/greenhouse");
    }
}
