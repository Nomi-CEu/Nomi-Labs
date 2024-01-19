package com.nomiceu.nomilabs.gregtech;

import com.nomiceu.nomilabs.util.LabsNames;
import gregtech.api.gui.resources.TextureArea;
import gregtech.client.renderer.texture.cube.OrientedOverlayRenderer;
import gregtech.client.renderer.texture.cube.SimpleOverlayRenderer;

public class LabsTextures {
    /* Overlays (Machine) */
    public static OrientedOverlayRenderer GREENHOUSE_OVERLAY;

    /* Overlays (Casings) */
    public static SimpleOverlayRenderer MICROVERSE_CASING;


    /* Recipe Map Textures */
    public static TextureArea PROGRESS_BAR_ROCKET;

    public static void preInit() {
        GREENHOUSE_OVERLAY = new OrientedOverlayRenderer("nomilabs:multiblock/greenhouse");
        MICROVERSE_CASING = new SimpleOverlayRenderer("nomilabs:microverse_casing");
        PROGRESS_BAR_ROCKET = labsFullImage("textures/gui/progress_bar/progress_bar_rocket.png");
    }

    /**
     * Like the one in TextureArea, but with Labs Registry.
     */
    public static TextureArea labsFullImage(String imageLocation) {
        return new TextureArea(LabsNames.makeLabsName(imageLocation), 0.0, 0.0, 1.0, 1.0);
    }
}
