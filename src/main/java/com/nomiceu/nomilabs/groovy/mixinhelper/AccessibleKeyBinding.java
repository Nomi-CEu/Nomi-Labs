package com.nomiceu.nomilabs.groovy.mixinhelper;

import net.minecraftforge.client.settings.KeyModifier;

public interface AccessibleKeyBinding {

    void setDefaultKeyModifierAndCode(KeyModifier modifier, int keyCode);
}
