package com.nomiceu.nomilabs.mixin.architecturecraft;

import com.elytradev.architecture.common.shape.EnumShape;
import org.apache.commons.lang3.NotImplementedException;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

/**
 * Allows accessing the protected static ID Map, which is used by Architecture Craft.
 */
@Mixin(value = EnumShape.class, remap = false)
public interface ShapeAccessor {
    @Accessor(value = "idMap")
    static Map<Integer, EnumShape> getIDMap() {
        throw new NotImplementedException("ShapeAccessorMixin Failed to Apply!");
    }
}
