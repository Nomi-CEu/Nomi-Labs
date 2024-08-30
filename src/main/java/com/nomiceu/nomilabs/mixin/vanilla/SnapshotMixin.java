package com.nomiceu.nomilabs.mixin.vanilla;

import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.registries.ForgeRegistry;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.google.common.collect.Maps;
import com.nomiceu.nomilabs.mixinhelper.RemappableForgeRegistry;
import com.nomiceu.nomilabs.mixinhelper.RemappableSnapshot;

/**
 * This mixin allows for saving of Forge Registry Remappings to snapshots.
 */
@Mixin(value = ForgeRegistry.Snapshot.class, remap = false)
public class SnapshotMixin implements RemappableSnapshot {

    @Unique
    public Map<Integer, ResourceLocation> remapped = Maps.newHashMap();

    @Unique
    private static final String REMAPPED_KEY = "remapped";

    @Inject(method = "write", at = @At("RETURN"))
    public void saveRemapped(CallbackInfoReturnable<NBTTagCompound> cir) {
        NBTTagCompound data = cir.getReturnValue();
        NBTTagList remapList = new NBTTagList();
        remapped.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEachOrdered(e -> {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setInteger("K", e.getKey());
            tag.setString("V", e.getValue().toString());
            remapList.appendTag(tag);
        });
        data.setTag(REMAPPED_KEY, remapList);
    }

    @Inject(method = "read", at = @At("RETURN"))
    private static void readRemapped(NBTTagCompound nbt, CallbackInfoReturnable<ForgeRegistry.Snapshot> cir) {
        ForgeRegistry.Snapshot ret = cir.getReturnValue();
        NBTTagList list = nbt.getTagList(REMAPPED_KEY, Constants.NBT.TAG_COMPOUND);
        list.forEach(e -> {
            NBTTagCompound comp = (NBTTagCompound) e;
            ((RemappableSnapshot) ret).addRemapped(comp.getInteger("K"), new ResourceLocation(comp.getString("V")));
        });
    }

    @Override
    @Unique
    public void addRemapped(int id, ResourceLocation key) {
        remapped.put(id, key);
    }

    @Override
    @Unique
    public void addAllRemapped(Map<Integer, ResourceLocation> map) {
        remapped.putAll(map);
    }

    @Override
    @Unique
    public void loadToRegistry(RemappableForgeRegistry reg) {
        remapped.forEach(reg::addRemapped);
    }
}
