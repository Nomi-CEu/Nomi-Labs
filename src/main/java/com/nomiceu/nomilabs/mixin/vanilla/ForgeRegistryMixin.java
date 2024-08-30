package com.nomiceu.nomilabs.mixin.vanilla;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.IForgeRegistryInternal;
import net.minecraftforge.registries.IForgeRegistryModifiable;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import com.google.common.collect.Maps;
import com.llamalad7.mixinextras.sugar.Local;
import com.nomiceu.nomilabs.NomiLabs;
import com.nomiceu.nomilabs.mixinhelper.RemappableForgeRegistry;
import com.nomiceu.nomilabs.mixinhelper.RemappableSnapshot;

/**
 * This mixin saves a new map in Forge Registries: Remapped.<br>
 * This is a map of remapped block ids to their new resource location, as specified by a remapper.<br>
 * Note that this is only applied when the new resource location cannot be registered at the old id, as in that case, no
 * saving of ids is required, as id loading will already load the new resource location.<br>
 * <p>
 * This mixin also fixes blocked lists not syncing between Forge Registries.
 */
@Mixin(value = ForgeRegistry.class, remap = false)
public abstract class ForgeRegistryMixin<V extends IForgeRegistryEntry<V>>
                                        implements IForgeRegistryInternal<V>, IForgeRegistryModifiable<V>,
                                        RemappableForgeRegistry {

    @Shadow
    abstract void block(int id);

    @Shadow
    @Final
    private Set<Integer> blocked;

    @Unique
    private final Map<Integer, ResourceLocation> remapped = Maps.newHashMap();

    @Inject(method = "processMissingEvent",
            at = @At(value = "INVOKE",
                     target = "Lnet/minecraftforge/registries/ForgeRegistry;addAlias(Lnet/minecraft/util/ResourceLocation;Lnet/minecraft/util/ResourceLocation;)V"),
            require = 1,
            locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    public void handleRemaps(ResourceLocation name, ForgeRegistry<V> pool,
                             List<RegistryEvent.MissingMappings.Mapping<V>> mappings,
                             Map<ResourceLocation, Integer> missing, Map<ResourceLocation, Integer[]> remaps,
                             Collection<ResourceLocation> defaulted, Collection<ResourceLocation> failed,
                             boolean injectNetworkDummies, CallbackInfo ci,
                             @Local RegistryEvent.MissingMappings.Mapping<V> remap,
                             @Local(ordinal = 2) int realId) {
        if (remap.id != realId) {
            block(remap.id);
            remapped.put(remap.id, remap.getTarget().getRegistryName());
            NomiLabs.LOGGER.warn(
                    "[Forge Registry] Remap could not assign Id {} for Object {}! If this is of type BLOCK, without Data Fixers, after initial load, blocks will no longer be remapped!",
                    remap.id, remap.getTarget().getRegistryName());
        }
    }

    @Inject(method = "sync", at = @At("RETURN"))
    void syncBlockedRemapped(ResourceLocation name, ForgeRegistry<V> from, CallbackInfo ci) {
        blocked.clear();

        var remFrom = (RemappableForgeRegistry) from;

        remFrom.getBlocked().forEach(this::block);

        remapped.clear();
        remFrom.getRemapped().forEach(this::addRemapped);
    }

    @Inject(method = "makeSnapshot", at = @At("RETURN"))
    public void addRemappedToSnapshot(CallbackInfoReturnable<ForgeRegistry.Snapshot> cir) {
        ForgeRegistry.Snapshot ret = cir.getReturnValue();
        ((RemappableSnapshot) ret).addAllRemapped(remapped);
    }

    @Override
    @Unique
    public void addRemapped(int id, ResourceLocation key) {
        remapped.put(id, key);
    }

    @Override
    @Unique
    public Map<Integer, ResourceLocation> getRemapped() {
        return remapped;
    }

    @Override
    @Unique
    public Set<Integer> getBlocked() {
        return blocked;
    }
}
