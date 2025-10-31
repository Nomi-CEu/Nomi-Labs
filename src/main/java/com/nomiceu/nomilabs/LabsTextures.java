package com.nomiceu.nomilabs;

import com.nomiceu.nomilabs.util.LabsNames;

import gregtech.api.gui.resources.TextureArea;
import gregtech.client.renderer.texture.cube.OrientedOverlayRenderer;
import gregtech.client.renderer.texture.cube.SimpleOverlayRenderer;

public class LabsTextures {

    /* Constants */
    public static int P2P_SIZE = 32;

    public static String P2P_SORTING_LOC = "textures/gui/advanced_memory_card/sorting.png";
    public static int P2P_SORTING_AMT = 5;

    public static String P2P_CUSTOM_LOC = "textures/gui/advanced_memory_card/custom_modes.png";
    public static int P2P_CUSTOM_AMT = 2;

    public static int TOP_SIZE = 12;
    public static String TOP_LOC = "textures/gui/top/icons.png";
    public static int TOP_AMT = 2;

    /* Overlays (Machine) */
    public static OrientedOverlayRenderer GROWTH_CHAMBER_OVERLAY;
    public static SimpleOverlayRenderer LOCKED_STORAGE;

    /* Overlays (Casings) */
    public static SimpleOverlayRenderer MICROVERSE_CASING;

    /* Recipe Map Textures */
    public static TextureArea PROGRESS_BAR_ROCKET;

    /* Gui Textures */

    // Index 0-2: Modes, Index 3-4: Sorting Direction
    public static TextureArea[] P2P_SORTING_ICONS;
    public static TextureArea[] P2P_CUSTOM_MODES;
    public static TextureArea P2P_INPUT_ICON;
    public static TextureArea P2P_OUTPUT_ICON;

    public static TextureArea[] TOP_ICONS;

    public static void preInit() {
        GROWTH_CHAMBER_OVERLAY = new OrientedOverlayRenderer("nomilabs:multiblock/growth_chamber");
        LOCKED_STORAGE = new SimpleOverlayRenderer("nomilabs:overlay/overlay_storage_locked");
        MICROVERSE_CASING = new SimpleOverlayRenderer("nomilabs:microverse_casing");
        PROGRESS_BAR_ROCKET = labsFullImage("textures/gui/progress_bar/progress_bar_rocket.png");

        P2P_SORTING_ICONS = labsAreasImageHorizontal(P2P_SORTING_LOC, P2P_SIZE, P2P_SIZE, P2P_SORTING_AMT);
        P2P_CUSTOM_MODES = labsAreasImageHorizontal(P2P_CUSTOM_LOC, P2P_SIZE, P2P_SIZE, P2P_CUSTOM_AMT);

        P2P_INPUT_ICON = labsFullImage("textures/gui/advanced_memory_card/input.png");
        P2P_OUTPUT_ICON = labsFullImage("textures/gui/advanced_memory_card/output.png");

        TOP_ICONS = labsAreasImageHorizontal(TOP_LOC, TOP_SIZE, TOP_SIZE, TOP_AMT);
    }

    /**
     * Returns an array of all texture areas, that are stacked horizontally, in an image.
     */
    public static TextureArea[] labsAreasImageHorizontal(String imageLoc, int iconSizeX, int iconSizeY, int amt) {
        TextureArea[] areas = new TextureArea[amt];
        for (int i = 0; i < amt; i++) {
            areas[i] = labsAreaImage(imageLoc, iconSizeX * amt, iconSizeY,
                    i * iconSizeX, 0, iconSizeX, iconSizeY);
        }
        return areas;
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
