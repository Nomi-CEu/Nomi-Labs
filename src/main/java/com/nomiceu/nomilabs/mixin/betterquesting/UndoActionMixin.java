package com.nomiceu.nomilabs.mixin.betterquesting;

import java.awt.event.InputEvent;

import org.apache.commons.lang3.SystemUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

/**
 * Makes undo shortcut respect OS (ctrl for windows/linux, cmd for mac)
 */
@Mixin(targets = "betterquesting.client.gui2.editors.TextEditorFrame$UndoHelper$UndoAction", remap = false)
public class UndoActionMixin {

    @ModifyConstant(method = "<init>", constant = @Constant(intValue = 128))
    private int returnCorrectModifier(int constant) {
        if (SystemUtils.IS_OS_MAC)
            return InputEvent.META_DOWN_MASK;

        return InputEvent.CTRL_DOWN_MASK;
    }
}
