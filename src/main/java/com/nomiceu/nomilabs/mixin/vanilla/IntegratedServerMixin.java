package com.nomiceu.nomilabs.mixin.vanilla;

import java.io.File;
import java.net.Proxy;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.EnumDifficulty;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.nomiceu.nomilabs.mixinhelper.DifficultySettableServer;

/**
 * Allows for support of calling `setDifficultyForAllWorldsAndSave`.
 */
@Mixin(IntegratedServer.class)
public abstract class IntegratedServerMixin extends MinecraftServer implements DifficultySettableServer {

    /**
     * Default Ignored Constructor
     */
    public IntegratedServerMixin(File anvilFileIn, Proxy proxyIn, DataFixer dataFixerIn,
                                 YggdrasilAuthenticationService authServiceIn, MinecraftSessionService sessionServiceIn,
                                 GameProfileRepository profileRepoIn, PlayerProfileCache profileCacheIn) {
        super(anvilFileIn, proxyIn, dataFixerIn, authServiceIn, sessionServiceIn, profileRepoIn, profileCacheIn);
    }

    @Unique
    @Override
    public void labs$setDifficultyForAllWorldsAndSave(EnumDifficulty difficulty) {
        setDifficultyForAllWorlds(difficulty);
    }
}
