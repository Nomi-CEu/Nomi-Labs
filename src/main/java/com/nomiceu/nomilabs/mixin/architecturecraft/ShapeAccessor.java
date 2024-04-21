package com.nomiceu.nomilabs.mixin.architecturecraft;

import java.util.Map;

import org.apache.commons.lang3.NotImplementedException;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import com.elytradev.architecture.common.shape.Shape;

/**
 * Allows accessing the protected static ID Map, which is used by Architecture Craft.
 */
@Mixin(value = Shape.class, remap = false)
public interface ShapeAccessor {

    @Accessor(value = "idMap")
    static Map<Integer, Shape> getIDMap() {
        throw new NotImplementedException("ShapeAccessorMixin Failed to Apply!");
    }
}
