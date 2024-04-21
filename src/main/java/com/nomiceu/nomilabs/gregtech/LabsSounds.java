package com.nomiceu.nomilabs.gregtech;

import static gregtech.api.GregTechAPI.soundManager;

import net.minecraft.util.SoundEvent;

import com.nomiceu.nomilabs.LabsValues;

public class LabsSounds {

    public static SoundEvent MICROVERSE;

    public static void register() {
        MICROVERSE = soundManager.registerSound(LabsValues.LABS_MODID, "tick.microverse");
    }
}
