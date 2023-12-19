package com.nomiceu.nomilabs.gregtech;

import com.nomiceu.nomilabs.gregtech.render.SpecificEmissiveOverlayRenderer;
import com.nomiceu.nomilabs.util.LabsNames;
import gregtech.api.gui.resources.TextureArea;
import gregtech.client.renderer.texture.cube.OrientedOverlayRenderer;
import gregtech.client.renderer.texture.cube.SimpleOverlayRenderer;
import mcjty.theoneprobe.rendering.OverlayRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LabsTextures {
    /* Overlays (Machine) */
    public static OrientedOverlayRenderer GREENHOUSE_OVERLAY;

    /* Overlays (Casings) */
    public static SimpleOverlayRenderer MICROVERSE_CASING;
    public static SimpleOverlayRenderer FUSION_MKIII_CASING;


    /* Recipe Map Textures */
    public static TextureArea PROGRESS_BAR_ROCKET;

    public static void preInit() {
        GREENHOUSE_OVERLAY = new OrientedOverlayRenderer("nomilabs:multiblock/greenhouse");
        MICROVERSE_CASING = new SimpleOverlayRenderer("nomilabs:microverse_casing");
        FUSION_MKIII_CASING = new SpecificEmissiveOverlayRenderer("casings/fusion/machine_casing_fusion_3", "casings/fusion/machine_casing_fusion_bloom");
        PROGRESS_BAR_ROCKET = labsFullImage("textures/gui/progress_bar/progress_bar_rocket.png");
    }

    /**
     * Like the one in TextureArea, but with Labs Registry.
     */
    public static TextureArea labsFullImage(String imageLocation) {
        return new TextureArea(LabsNames.makeLabsName(imageLocation), 0.0, 0.0, 1.0, 1.0);
    }
}
