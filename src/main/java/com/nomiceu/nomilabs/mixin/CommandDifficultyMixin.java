package com.nomiceu.nomilabs.mixin;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandDifficulty;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.EnumDifficulty;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.nomiceu.nomilabs.mixinhelper.DifficultySettableServer;
import com.nomiceu.nomilabs.util.LabsDifficultyHelper;
import com.nomiceu.nomilabs.util.LabsModeHelper;

/**
 * Prevents Changing Difficulty when Labs Locked, and saves changed difficulties to server.properties on Dedicated
 * Servers.
 */
@Mixin(CommandDifficulty.class)
public abstract class CommandDifficultyMixin extends CommandBase {

    @Inject(method = "execute", at = @At("HEAD"), cancellable = true)
    public void executeIfNotLocked(MinecraftServer server, ICommandSender sender, String[] args, CallbackInfo ci) {
        var locked = LabsDifficultyHelper.getLockedDifficulty();
        if (locked == null) return;

        notifyCommandListener(sender, this, "command.nomilabs.difficulty.labs_locked_1",
                new TextComponentTranslation(locked.getTranslationKey())
                        .setStyle(new Style().setColor(TextFormatting.DARK_AQUA)));
        notifyCommandListener(sender, this, "command.nomilabs.difficulty.labs_locked_2",
                new TextComponentString(LabsModeHelper.getFormattedMode())
                        .setStyle(new Style().setColor(TextFormatting.GOLD)));
        ci.cancel();
    }

    @Redirect(method = "execute",
              at = @At(value = "INVOKE",
                       target = "Lnet/minecraft/server/MinecraftServer;setDifficultyForAllWorlds(Lnet/minecraft/world/EnumDifficulty;)V"))
    public void setDifficultyAndSave(MinecraftServer instance, EnumDifficulty newDifficulty) {
        if (instance instanceof DifficultySettableServer diff)
            diff.setDifficultyForAllWorldsAndSave(newDifficulty);
        else
            instance.setDifficultyForAllWorlds(newDifficulty);
    }
}
