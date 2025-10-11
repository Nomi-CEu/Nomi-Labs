package com.nomiceu.nomilabs.mixin.gregtech;

import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.IItemHandlerModifiable;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReceiver;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.nomiceu.nomilabs.NomiLabs;
import com.nomiceu.nomilabs.gregtech.mixinhelper.AccessibleQuantumChest;

import gregtech.api.capability.GregtechDataCodes;
import gregtech.api.gui.GuiTextures;
import gregtech.api.gui.IUIHolder;
import gregtech.api.gui.ModularUI;
import gregtech.api.gui.widgets.ToggleButtonWidget;
import gregtech.api.items.itemhandlers.GTItemStackHandler;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.util.GTUtility;
import gregtech.common.metatileentities.storage.MetaTileEntityQuantumChest;

/**
 * Part of the implementation to allow quantum chests to be lockable.
 * <p>
 * Handles gui changes and syncing, and defines logic used by item handler checks in other mixins.
 * Also, checks locked stack when inserting directly into export items.
 */
@Mixin(value = MetaTileEntityQuantumChest.class, remap = false)
public abstract class MetaTileEntityQuantumChestMixin extends MetaTileEntity implements AccessibleQuantumChest {

    @Unique
    private static final String LABS$LOCKED_KEY = "IsLocked";

    @Unique
    private static final String LABS$LOCKED_STACK_KEY = "LockedStack";

    @Shadow
    protected static boolean areItemStackIdentical(ItemStack first, ItemStack second) {
        return false;
    }

    @Shadow
    @Final
    private static String NBT_ITEMSTACK;

    @Shadow
    protected boolean voiding;
    @Shadow
    @Final
    protected long maxStoredItems;
    @Unique
    private boolean labs$locked = false;

    @Unique
    private ItemStack labs$lockedStack = ItemStack.EMPTY;

    /**
     * Mandatory Ignored Constructor
     */
    private MetaTileEntityQuantumChestMixin(ResourceLocation metaTileEntityId) {
        super(metaTileEntityId);
    }

    /* Internal Changes */
    @WrapMethod(method = "createExportItemHandler")
    private IItemHandlerModifiable createNewExportItemHandler(Operation<IItemHandlerModifiable> original) {
        // Mostly copied directly from insert item impl
        return new GTItemStackHandler(this, 1) {

            @NotNull
            @Override
            public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
                if (!isItemValid(slot, stack)) return stack;
                return super.insertItem(slot, stack, simulate);
            }

            @Override
            public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                NBTTagCompound compound = stack.getTagCompound();
                ItemStack outStack = getStackInSlot(0);

                boolean stackAllowed;
                if (!outStack.isEmpty()) {
                    stackAllowed = areItemStackIdentical(stack, outStack);
                } else {
                    stackAllowed = !labs$lockedBlocksStack(stack);
                }
                if (compound == null) return stackAllowed;

                return stackAllowed && !(compound.hasKey(NBT_ITEMSTACK, Constants.NBT.TAG_COMPOUND) ||
                        compound.hasKey("Fluid", Constants.NBT.TAG_COMPOUND)); // prevents inserting items with NBT to
                                                                               // the Quantum Chest
            }

            @Override
            protected void onContentsChanged(int slot) {
                super.onContentsChanged(slot);
                labs$stackInserted(getStackInSlot(0));
            }
        };
    }

    /* Gui Changes */
    @ModifyReceiver(method = "createUI",
                    at = @At(value = "INVOKE",
                             target = "Lgregtech/api/gui/widgets/ToggleButtonWidget;setTooltipText(Ljava/lang/String;[Ljava/lang/Object;)Lgregtech/api/gui/widgets/ToggleButtonWidget;",
                             ordinal = 1))
    private ToggleButtonWidget moveVoidingButton(ToggleButtonWidget instance, String tooltipText, Object[] args) {
        instance.addSelfPosition(18, 0);
        return instance;
    }

    @ModifyReceiver(method = "createUI",
                    at = @At(value = "INVOKE",
                             target = "Lgregtech/api/gui/ModularUI$Builder;build(Lgregtech/api/gui/IUIHolder;Lnet/minecraft/entity/player/EntityPlayer;)Lgregtech/api/gui/ModularUI;"),
                    require = 1)
    private ModularUI.Builder addLockingButton(ModularUI.Builder instance, IUIHolder holder, EntityPlayer player) {
        return instance.widget(
                new ToggleButtonWidget(25, 64, 18, 18,
                        GuiTextures.BUTTON_LOCK, this::labs$isLockedInternal, this::labs$setLocked)
                                .setTooltipText("nomilabs.gui.item_lock.tooltip")
                                .shouldUseBaseBackground());
    }

    @ModifyExpressionValue(method = "addDisplayInformation",
                           at = @At(value = "INVOKE",
                                    target = "Lnet/minecraftforge/items/IItemHandlerModifiable;getStackInSlot(I)Lnet/minecraft/item/ItemStack;"))
    private ItemStack useLocked(ItemStack original) {
        if (original.isEmpty() && labs$locked)
            return labs$lockedStack;
        return original;
    }

    /* Sync Changes */
    @Inject(method = "readFromNBT", at = @At("RETURN"))
    private void readLockedNBT(NBTTagCompound data, CallbackInfo ci) {
        labs$locked = data.getBoolean(LABS$LOCKED_KEY); // Defaults to false

        if (labs$locked) {
            labs$lockedStack = new ItemStack(data.getCompoundTag(LABS$LOCKED_STACK_KEY));
        }
    }

    @Inject(method = "writeToNBT", at = @At("RETURN"))
    private void writeLockedNBT(NBTTagCompound data, CallbackInfoReturnable<NBTTagCompound> cir) {
        data.setBoolean(LABS$LOCKED_KEY, labs$locked);

        if (labs$locked) {
            data.setTag(LABS$LOCKED_STACK_KEY, labs$lockedStack.serializeNBT());
        }
    }

    @Inject(method = "receiveInitialSyncData", at = @At("RETURN"))
    private void readLockedInitial(PacketBuffer buf, CallbackInfo ci) {
        try {
            NBTTagCompound export = buf.readCompoundTag();
            if (export != null)
                GTUtility.readItems(exportItems, "ExportInventory", export);

            labs$locked = buf.readBoolean();
            if (labs$locked) {
                labs$lockedStack = buf.readItemStack();
            }
        } catch (IOException e) {
            NomiLabs.LOGGER.error(
                    "[QuantumChestMixin] Failed to load info from tile at {} from buffer in initial sync!", getPos());
        }
    }

    @Inject(method = "writeInitialSyncData", at = @At("RETURN"))
    private void writeLockedInitial(PacketBuffer buf, CallbackInfo ci) {
        var exportCompound = new NBTTagCompound();
        GTUtility.writeItems(exportItems, "ExportInventory", exportCompound);
        buf.writeCompoundTag(exportCompound);

        buf.writeBoolean(labs$locked);

        if (labs$locked) {
            buf.writeItemStack(labs$lockedStack);
        }
    }

    @Inject(method = "initFromItemStackData", at = @At("RETURN"))
    private void readLockedStack(NBTTagCompound data, CallbackInfo ci) {
        labs$locked = data.getBoolean(LABS$LOCKED_KEY); // Defaults to false

        if (labs$locked) {
            labs$lockedStack = new ItemStack(data.getCompoundTag(LABS$LOCKED_STACK_KEY));
        }
    }

    @Inject(method = "writeItemStackData", at = @At("RETURN"))
    private void writeLockedStack(NBTTagCompound data, CallbackInfo ci) {
        data.setBoolean(LABS$LOCKED_KEY, labs$locked);

        if (labs$locked) {
            data.setTag(LABS$LOCKED_STACK_KEY, labs$lockedStack.serializeNBT());
        }

        labs$locked = false;
        labs$lockedStack = ItemStack.EMPTY;
    }

    @Inject(method = "receiveCustomData", at = @At(value = "TAIL"))
    private void receiveLockedUpdate(int dataId, PacketBuffer buf, CallbackInfo ci) {
        if (dataId == GregtechDataCodes.UPDATE_LOCKED_STATE) {
            labs$setLocked(buf.readBoolean());
            scheduleRenderUpdate();
        }
    }

    /* Helper */
    @Unique
    private boolean labs$isLockedInternal() {
        return labs$locked;
    }

    @Unique
    private void labs$setLocked(boolean locked) {
        if (labs$locked == locked) return;

        labs$locked = locked;
        if (!getWorld().isRemote) {
            writeCustomData(GregtechDataCodes.UPDATE_LOCKED_STATE, buf -> buf.writeBoolean(locked));
            markDirty();
        }

        // Update locked stack
        if (locked) {
            ItemStack exportItems = getExportItems().getStackInSlot(0);
            if (!exportItems.isEmpty()) {
                labs$lockedStack = exportItems.copy();
                labs$lockedStack.setCount(1);
                return;
            }
        }

        labs$lockedStack = ItemStack.EMPTY;
    }

    @Unique
    @Override
    public boolean labs$isLocked() {
        return labs$locked && !labs$lockedStack.isEmpty();
    }

    @Unique
    @Override
    public boolean labs$isVoiding() {
        return voiding;
    }

    @Unique
    @Override
    public boolean labs$lockedBlocksStack(ItemStack stack) {
        if (!labs$locked || labs$lockedStack.isEmpty()) return false;

        return !areItemStackIdentical(stack, labs$lockedStack);
    }

    @Unique
    @Override
    public void labs$stackInserted(ItemStack stack) {
        if (!labs$locked || !labs$lockedStack.isEmpty()) return;

        labs$lockedStack = stack.copy();
        labs$lockedStack.setCount(1);
    }

    @Unique
    @Override
    public ItemStack labs$getLockedStack() {
        return labs$lockedStack;
    }

    @Unique
    @Override
    public long labs$getMaxStored() {
        return maxStoredItems;
    }
}
