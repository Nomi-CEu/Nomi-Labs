package com.nomiceu.nomilabs.mixin.gregtech;

import java.util.List;

import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.nomiceu.nomilabs.gregtech.mixinhelper.AccessibleQuantumChest;
import com.nomiceu.nomilabs.util.LabsTranslate;

import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.client.renderer.texture.custom.QuantumStorageRenderer;
import gregtech.common.metatileentities.storage.MetaTileEntityQuantumChest;

/**
 * Makes quantum chest rendering take into account 'export slot' items.
 * Changes styling and fixes edge cases in tooltip.
 */
@Mixin(value = MetaTileEntityQuantumChest.class, remap = false)
public abstract class MetaTileEntityQuantumChestClientMixin extends MetaTileEntity {

    @Shadow
    protected long itemsStoredInside;

    @Shadow
    @Final
    private static String NBT_ITEMSTACK;

    @Shadow
    @Final
    private static String NBT_ITEMCOUNT;

    @Shadow
    @Final
    private static String NBT_PARTIALSTACK;

    @Shadow
    @Final
    private static String IS_VOIDING;

    /**
     * Mandatory Ignored Constructor
     */
    private MetaTileEntityQuantumChestClientMixin(ResourceLocation metaTileEntityId) {
        super(metaTileEntityId);
    }

    @Redirect(method = "addDisplayInformation",
              at = @At(value = "FIELD",
                       target = "Lgregtech/common/metatileentities/storage/MetaTileEntityQuantumChest;itemsStoredInside:J"),
              require = 1)
    private long accountExportItems(MetaTileEntityQuantumChest instance) {
        return itemsStoredInside + getExportItems().getStackInSlot(0).getCount();
    }

    @Inject(method = "renderMetaTileEntity(DDDF)V", at = @At("HEAD"), cancellable = true)
    private void accountExportItemsOrLocked(double x, double y, double z, float partialTicks, CallbackInfo ci) {
        ci.cancel();

        ItemStack stack = getExportItems().getStackInSlot(0);
        long amount = 0;
        if (stack.isEmpty()) {
            if (((AccessibleQuantumChest) this).labs$isLocked()) {
                // Use locked stack, set count to -1 to bypass initial checks; we will normalise it to zero later
                stack = ((AccessibleQuantumChest) this).labs$getLockedStack();
                amount = -1;
            }
        } else {
            amount = itemsStoredInside + stack.getCount();
        }

        QuantumStorageRenderer.renderChestStack(x, y, z, (MetaTileEntityQuantumChest) (Object) this, stack, amount,
                partialTicks);
    }

    @Inject(method = "addInformation",
            at = @At(value = "INVOKE",
                     target = "Lnet/minecraft/item/ItemStack;getTagCompound()Lnet/minecraft/nbt/NBTTagCompound;",
                     remap = true),
            require = 1,
            remap = false,
            cancellable = true)
    private void addProperInfo(ItemStack stack, @Nullable World player, List<String> tooltip, boolean advanced,
                               CallbackInfo ci) {
        ci.cancel();

        NBTTagCompound tag = stack.getTagCompound();
        if (tag == null) return;

        boolean locked = tag.hasKey("LockedStack", Constants.NBT.TAG_COMPOUND);

        ItemStack tempStack = ItemStack.EMPTY;
        long count = 0;
        if (tag.hasKey(NBT_PARTIALSTACK, Constants.NBT.TAG_COMPOUND)) {
            // Handle old format
            tempStack = new ItemStack(tag.getCompoundTag(NBT_PARTIALSTACK));
            count = tempStack.getCount();
        }

        if (tag.hasKey(NBT_ITEMSTACK, Constants.NBT.TAG_COMPOUND)) {
            tempStack = new ItemStack(tag.getCompoundTag(NBT_ITEMSTACK));
            count += tag.getLong(NBT_ITEMCOUNT);
        }

        // If empty, use stack from locked info
        if (tempStack.isEmpty() && locked) {
            tempStack = new ItemStack(tag.getCompoundTag("LockedStack"));
        }

        String name = tempStack.getDisplayName();
        tooltip.add(
                LabsTranslate.translate("tooltip.nomilabs.items_stored_simple", LabsTranslate.translate(name), count));

        if (locked) {
            tooltip.add(LabsTranslate.translate("nomilabs.top.properties.locked"));
        }

        if (tag.getBoolean(IS_VOIDING))
            tooltip.add(I18n.format("nomilabs.top.properties.voiding"));
    }
}
