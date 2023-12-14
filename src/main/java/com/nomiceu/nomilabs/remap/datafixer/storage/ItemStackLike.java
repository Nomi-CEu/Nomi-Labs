package com.nomiceu.nomilabs.remap.datafixer.storage;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;

/**
 * Essentially an ItemStack, but contains ResourceLocation instead of Item. For Unregistered Items.
 * <p>
 * Currently just contains rl, meta and tag.
 */
public class ItemStackLike {
    public ResourceLocation rl;
    public short meta;
    @Nullable
    public NBTTagCompound tag;

    public ItemStackLike(NBTTagCompound compound) {
        this.rl = new ResourceLocation(compound.getString("id"));
        this.meta = (short) Math.max(0, compound.getShort("Damage"));

        if (compound.hasKey("tag", Constants.NBT.TAG_COMPOUND)) {
            this.tag = compound.getCompoundTag("tag");
        } else
            this.tag = null;
    }

    public NBTTagCompound changeCompound(NBTTagCompound compound) {
        compound.setString("id", rl.toString());
        compound.setShort("Damage", meta);
        if (tag != null)
            compound.setTag("tag", tag);
        else if (compound.hasKey("tag", Constants.NBT.TAG_COMPOUND))
            compound.removeTag("tag");
        return compound;
    }

    public ItemStackLike setRl(ResourceLocation newRl) {
        rl = newRl;
        return this;
    }

    public ItemStackLike setMeta(short newMeta) {
        meta = newMeta;
        return this;
    }

    public ItemStackLike setTag(@Nullable NBTTagCompound newTag) {
        tag = newTag;
        return this;
    }
}
