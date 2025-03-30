package com.nomiceu.nomilabs.mixin.storagedrawers;

import org.spongepowered.asm.mixin.Mixin;

import com.jaquadro.minecraft.storagedrawers.api.storage.IDrawerAttributes;
import com.nomiceu.nomilabs.integration.storagedrawers.RefreshableDrawer;

/**
 * Simple Mixin to Implement RefreshableDrawer Interface
 */
@Mixin(value = IDrawerAttributes.class, remap = false)
public interface IDrawerAttributesMixin extends RefreshableDrawer {}
