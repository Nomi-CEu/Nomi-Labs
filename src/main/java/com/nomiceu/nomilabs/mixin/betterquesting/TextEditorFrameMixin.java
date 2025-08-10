package com.nomiceu.nomilabs.mixin.betterquesting;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.nomiceu.nomilabs.config.LabsConfig;

import betterquesting.client.gui2.editors.TextEditorFrame;
import io.netty.util.collection.IntObjectMap;

/**
 * Allows disabling of the separate window description editor, fixing a crash with macs and cleanroom loader.
 */
@Mixin(value = TextEditorFrame.class, remap = false)
public class TextEditorFrameMixin {

    @Shadow
    @Final
    private static IntObjectMap<TextEditorFrame> open;

    @WrapMethod(method = "getOrCreate")
    private static TextEditorFrame cancelIfCfg(int questID, String title, String name, String description,
                                               Operation<TextEditorFrame> original) {
        if (!LabsConfig.advanced.disableBQuWindow) return original.call(questID, title, name, description);

        if (open.containsKey(questID))
            return open.get(questID);

        // Handled gracefully in *most* situations; causes a crash when show window is clicked;
        // but that can be ignored... this config will only be used *situationally* by devs at most
        return null;
    }
}
