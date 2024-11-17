package com.nomiceu.nomilabs.mixin.vanilla;

import java.io.File;
import java.net.Proxy;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.dedicated.PropertyManager;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.EnumDifficulty;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.nomiceu.nomilabs.NomiLabs;
import com.nomiceu.nomilabs.config.LabsConfig;
import com.nomiceu.nomilabs.config.LabsVersionConfig;
import com.nomiceu.nomilabs.mixinhelper.DifficultySettableServer;
import com.nomiceu.nomilabs.util.LabsTranslate;

/**
 * Gets the Locked Difficulty on Servers, Locking the Difficulty there as well.
 * <p>
 * This is because servers select difficulty from the 'server.properties' file.
 * <p>
 * Also overrides the difficulty in server.properties for consistency.
 * <p>
 * Allows for support of calling `setDifficultyForAllWorldsAndSave`, changing difficulty and saving to
 * `server.properties` on the fly.
 * <p>
 * Also replaces {mode} and {server} with Labs Values if config says so.
 */
@Mixin(DedicatedServer.class)
public abstract class DedicatedServerMixin extends MinecraftServer implements DifficultySettableServer {

    /**
     * Mandatory Ignored Constructor
     */
    public DedicatedServerMixin(File anvilFileIn, Proxy proxyIn, DataFixer dataFixerIn,
                                YggdrasilAuthenticationService authServiceIn, MinecraftSessionService sessionServiceIn,
                                GameProfileRepository profileRepoIn, PlayerProfileCache profileCacheIn) {
        super(anvilFileIn, proxyIn, dataFixerIn, authServiceIn, sessionServiceIn, profileRepoIn, profileCacheIn);
    }

    @Shadow
    private PropertyManager settings;

    @Inject(method = "getDifficulty", at = @At("HEAD"), cancellable = true)
    public void getLockedDifficulty(CallbackInfoReturnable<EnumDifficulty> cir) {
        var locked = EnumDifficulty.byId(2);
    }

    @Redirect(method = "init",
              at = @At(value = "INVOKE",
                       target = "Lnet/minecraft/server/dedicated/DedicatedServer;setMOTD(Ljava/lang/String;)V"))
    public void motdSubstitutions(DedicatedServer instance, String s) {
        if (!LabsConfig.advanced.serverMotdSubstitutions) return;

        NomiLabs.LOGGER.info("Enabling Labs MOTD Substitutions...");
        instance.setMOTD(s.replace("{version}", LabsVersionConfig.formattedVersion));
    }

    @SuppressWarnings("LoggingSimilarMessage")
    @Inject(method = "init", at = @At("RETURN"))
    public void changeInitDifficulty(CallbackInfoReturnable<EnumDifficulty> cir) {
        var locked = EnumDifficulty.byId(2);
        var savedDifficulty = this.settings.getIntProperty("difficulty", 1);
        if (savedDifficulty != locked.getId()) {
            NomiLabs.LOGGER.warn("===============================================");
            NomiLabs.LOGGER.warn("============ LABS DIFFICULTY LOCK: ============");
            NomiLabs.LOGGER.warn("-----------------------------------------------");

            NomiLabs.LOGGER.warn("Server Difficulty was set to {}, it has now been overrided to {}!",
                    LabsTranslate.translate(EnumDifficulty.byId(savedDifficulty).getTranslationKey()),
                    LabsTranslate.translate(locked.getTranslationKey()));

            NomiLabs.LOGGER.warn("This is because HOGifactory forces peaceful mode");

            NomiLabs.LOGGER.warn("-----------------------------------------------");
            NomiLabs.LOGGER.warn("===============================================");
        }
    }

    @Override
    public void setDifficultyForAllWorldsAndSave(EnumDifficulty difficulty) {
        setDifficultyForAllWorlds(difficulty);
        settings.setProperty("difficulty", difficulty.getId());
        settings.saveProperties();
    }
}
