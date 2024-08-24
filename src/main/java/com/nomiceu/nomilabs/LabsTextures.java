package com.nomiceu.nomilabs;

import com.nomiceu.nomilabs.util.LabsNames;

import gregtech.api.gui.resources.TextureArea;
import gregtech.client.renderer.texture.cube.OrientedOverlayRenderer;
import gregtech.client.renderer.texture.cube.SimpleOverlayRenderer;

public class LabsTextures {

    /* Overlays (Machine) */
    public static OrientedOverlayRenderer GROWTH_CHAMBER_OVERLAY;

    /* Overlays (Casings) */
    public static SimpleOverlayRenderer MICROVERSE_CASING;

    /* Recipe Map Textures */
    public static TextureArea PROGRESS_BAR_ROCKET;

    /* Gui Textures */
    public static TextureArea[] P2P_CUSTOM_MODES;
    public static TextureArea P2P_INPUT_ICON;
    public static TextureArea P2P_OUTPUT_ICON;

    public static void preInit() {
        GROWTH_CHAMBER_OVERLAY = new OrientedOverlayRenderer("nomilabs:multiblock/growth_chamber");
        MICROVERSE_CASING = new SimpleOverlayRenderer("nomilabs:microverse_casing");
        PROGRESS_BAR_ROCKET = labsFullImage("textures/gui/progress_bar/progress_bar_rocket.png");

        var imageSizeX = 64;
        var imageSizeY = 32;
        var imageLoc = "textures/gui/advanced_memory_card/custom_modes.png";
        var spriteSize = 32;
        P2P_CUSTOM_MODES = new TextureArea[] {
                labsAreaImage(imageLoc, imageSizeX, imageSizeY, 0, 0, spriteSize, spriteSize),
                labsAreaImage(imageLoc, imageSizeX, imageSizeY, spriteSize, 0, spriteSize, spriteSize),
        };

        P2P_INPUT_ICON = labsFullImage("textures/gui/advanced_memory_card/input.png");
        P2P_OUTPUT_ICON = labsFullImage("textures/gui/advanced_memory_card/output.png");
    }

    /**
     * Like the one in TextureArea, but with Labs Registry.
     */
    public static TextureArea labsFullImage(String imageLocation) {
        return new TextureArea(LabsNames.makeLabsName(imageLocation), 0.0, 0.0, 1.0, 1.0);
    }

    /**
     * Almost like the one in TextureArea, but with Labs Registry.
     */
    public static TextureArea labsAreaImage(String imageLocation, int imageSizeX, int imageSizeY, int u, int v,
                                            int width, int height) {
        return new TextureArea(LabsNames.makeLabsName(imageLocation),
                u / (imageSizeX * 1.0),
                v / (imageSizeY * 1.0),
                width / (imageSizeX * 1.0),
                height / (imageSizeY * 1.0));
    }
}
