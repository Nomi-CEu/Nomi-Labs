package com.nomiceu.nomilabs.gregtech.mixinhelper;

import java.util.function.Consumer;

public interface AccessibleRecipeBuilder<R> {

    Consumer<R> labs$getOnBuildAction();

    void labs$invalidateOnBuildAction();
}
