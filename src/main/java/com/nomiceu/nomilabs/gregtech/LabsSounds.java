package com.nomiceu.nomilabs.gregtech;

import com.nomiceu.nomilabs.LabsValues;
import net.minecraft.util.SoundEvent;

import static gregtech.api.GregTechAPI.soundManager;

public class LabsSounds {
    public static SoundEvent MICROVERSE;

    public static void register() {
        MICROVERSE = soundManager.registerSound(LabsValues.LABS_MODID, "tick.microverse");
    }
}
