package com.nomiceu.nomilabs.mixin.vanilla;

import net.minecraft.command.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.EnumDifficulty;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.nomiceu.nomilabs.mixinhelper.DifficultySettableServer;
import com.nomiceu.nomilabs.util.LabsDifficultyHelper;

/**
 * Prevents Changing Difficulty when Labs Locked, and saves changed difficulties to server.properties on Dedicated
 * Servers.
 */
@Mixin(CommandDifficulty.class)
public abstract class CommandDifficultyMixin extends CommandBase {

    @Shadow
    protected abstract EnumDifficulty getDifficultyFromCommand(String difficultyString) throws CommandException;

    /**
     * Overrides entire function, fixes conflict with SpongeForge.
     */
    @Inject(method = "execute", at = @At("HEAD"), cancellable = true)
    public void executeIfNotLocked(MinecraftServer server, ICommandSender sender, String[] args,
                                   CallbackInfo ci) throws CommandException {
        var locked = LabsDifficultyHelper.getLockedDifficulty();

        if (locked != null) {
            notifyCommandListener(sender, this, "command.nomilabs.difficulty.labs_locked_1",
                    new TextComponentTranslation(locked.getTranslationKey())
                            .setStyle(new Style().setColor(TextFormatting.DARK_AQUA)));
            notifyCommandListener(sender, this, "command.nomilabs.difficulty.labs_locked_2",
                    new TextComponentString(LabsModeHelper.getFormattedMode())
                            .setStyle(new Style().setColor(TextFormatting.GOLD)));
            ci.cancel();
            return;
        }

        if (args.length == 0) {
            throw new WrongUsageException("commands.difficulty.usage");
        }

        EnumDifficulty newDifficulty = getDifficultyFromCommand(args[0]);
        if (server instanceof DifficultySettableServer diff)
            diff.setDifficultyForAllWorldsAndSave(newDifficulty);
        else
            server.setDifficultyForAllWorlds(newDifficulty);

        notifyCommandListener(sender, this, "commands.difficulty.success",
                new TextComponentTranslation(newDifficulty.getTranslationKey()));
        ci.cancel();
    }
}
