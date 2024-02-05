package com.nomiceu.nomilabs.gregtech;

import gregtech.api.unification.stack.MaterialStack;

import java.util.List;

public interface AccessibleMaterial {
    void setComponents(List<MaterialStack> components);
}
