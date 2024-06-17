package com.nomiceu.nomilabs.gregtech;

import com.nomiceu.nomilabs.gregtech.metatileentity.multiblock.ProperlySidedOverlayRenderer;
import com.nomiceu.nomilabs.util.LabsNames;

import gregtech.api.gui.resources.TextureArea;
import gregtech.client.renderer.texture.cube.OrientedOverlayRenderer;
import gregtech.client.renderer.texture.cube.SimpleOverlayRenderer;

public class LabsTextures {

    /* Overlays (Machine) */
    public static OrientedOverlayRenderer GREENHOUSE_OVERLAY;
    public static OrientedOverlayRenderer OVERLAY_LAMP_1;
    public static OrientedOverlayRenderer OVERLAY_VENT_1;
    public static SimpleOverlayRenderer OVERLAY_BASE_1;

    /* Overlays (Casings) */
    public static SimpleOverlayRenderer MICROVERSE_CASING;

    /* Recipe Map Textures */
    public static TextureArea PROGRESS_BAR_ROCKET;

    public static void preInit() {
        GREENHOUSE_OVERLAY = new OrientedOverlayRenderer("nomilabs:multiblock/greenhouse");
        OVERLAY_LAMP_1 = new ProperlySidedOverlayRenderer("nomilabs:part/growth_modifier/overlay_lamp_1");
        OVERLAY_VENT_1 = new ProperlySidedOverlayRenderer("nomilabs:part/growth_modifier/overlay_vent_1");
        OVERLAY_BASE_1 = new SimpleOverlayRenderer("nomilabs:part/growth_base/overlay_base_1/overlay");
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
