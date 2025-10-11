package com.nomiceu.nomilabs.mixin.topaddons.architecturecraft;

import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import com.elytradev.architecture.common.shape.EnumShape;
import com.nomiceu.nomilabs.util.LabsTranslate;

import io.github.drmanganese.topaddons.addons.AddonArchitectureCraft;

@Mixin(value = AddonArchitectureCraft.class, remap = false)
public class AddonArchitectureCraftMixin {

    /**
     * Overwrites the {@link AddonArchitectureCraft#getShapeName(ItemStack)}.
     * 
     * @author IntegerLimit_
     * @reason It uses the old Shape Class. Overwriting instead of Injecting will hopefully make sure the class does NOT
     *         get loaded, causing a ClassNotFoundException.
     */
    @Overwrite
    private static String getShapeName(ItemStack stack) {
        var compound = stack.getTagCompound();
        return LabsTranslate
                .translate(EnumShape.forId(compound == null ? 0 : compound.getInteger("Shape")).translationKey);
    }
}
