package com.nomiceu.nomilabs;

import static gregtech.api.GregTechAPI.soundManager;

import net.minecraft.util.SoundEvent;

public class LabsSounds {

    public static SoundEvent MICROVERSE;

    public static void register() {
        MICROVERSE = soundManager.registerSound(LabsValues.LABS_MODID, "tick.microverse");
    }
}
