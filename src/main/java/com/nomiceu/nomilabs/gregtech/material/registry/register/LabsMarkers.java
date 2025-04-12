package com.nomiceu.nomilabs.gregtech.material.registry.register;

import static com.nomiceu.nomilabs.gregtech.material.registry.LabsMaterials.*;

import gregtech.api.unification.material.MarkerMaterial;

public class LabsMarkers {

    public static void initMarkers() {
        DILITHIUM = MarkerMaterial.create("dilithium");
        DILITHIUM.setMaterialRGB(0xddcecb);
        BLACK_QUARTZ = MarkerMaterial.create("quartz_black");
        BLACK_QUARTZ.setMaterialRGB(0x000000);
    }
}
