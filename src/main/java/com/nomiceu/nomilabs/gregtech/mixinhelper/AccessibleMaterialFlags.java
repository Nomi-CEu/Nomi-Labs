package com.nomiceu.nomilabs.gregtech.mixinhelper;

import gregtech.api.unification.material.info.MaterialFlag;

public interface AccessibleMaterialFlags {

    void labs$removeFlags(MaterialFlag... toRemove);
}
