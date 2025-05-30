package com.nomiceu.nomilabs.mixin.solarflux;

import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import org.apache.commons.lang3.tuple.Pair;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.google.common.math.LongMath;
import com.nomiceu.nomilabs.config.LabsConfig;
import com.nomiceu.nomilabs.integration.solarflux.AccessibleInventoryDummy;
import com.nomiceu.nomilabs.integration.solarflux.AccessibleTileBaseSolar;

import gregtech.api.GTValues;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import tk.zeitheron.solarflux.api.SolarInstance;
import tk.zeitheron.solarflux.api.attribute.SimpleAttributeProperty;
import tk.zeitheron.solarflux.block.tile.TileBaseSolar;
import tk.zeitheron.solarflux.gui.ContainerBaseSolar;
import tk.zeitheron.solarflux.items.ItemUpgrade;
import tk.zeitheron.solarflux.utils.BlockPosFace;
import tk.zeitheron.solarflux.utils.InventoryDummy;

@Mixin(value = TileBaseSolar.class, remap = false)
public abstract class TileBaseSolarMixin extends TileEntity implements AccessibleTileBaseSolar {

    @Shadow
    public long energy;

    @Shadow
    public abstract int getEnergyStored();

    @Shadow
    @Final
    public SimpleAttributeProperty transfer;

    @Shadow
    public SolarInstance instance;

    @Shadow
    @Final
    public List<BlockPosFace> traversal;

    @Shadow
    @Final
    public SimpleAttributeProperty capacity;

    @Shadow
    public List<EntityPlayer> crafters;

    @Shadow
    public long currentGeneration;

    @Shadow
    public abstract int getGeneration();

    @Shadow
    @Final
    public InventoryDummy chargeInventory;

    @Shadow
    @Final
    public SimpleAttributeProperty generation;

    @Shadow
    @Final
    public InventoryDummy upgradeInventory;

    @Unique
    private final Set<EnumFacing> labs$blacklisted = new ObjectOpenHashSet<>(4);

    @Unique
    private final Map<ResourceLocation, Pair<ItemStack, Integer>> labs$tickedUpgrades = new Object2ObjectOpenHashMap<>(
            5);

    @Unique
    private IEnergyStorage labs$chargerEnergy = null;

    // Distribute Auto Balancing Operations
    @Unique
    private final int labs$offset = GTValues.RNG.nextInt(20);

    @Unique
    private long labs$timer = 0;

    @Unique
    private TileEntity[] labs$teCache;

    @Unique
    private AccessibleTileBaseSolar[] labs$solarCache;

    @Unique
    private boolean labs$lastBalanceSuccess = true;

    @Unique
    private int labs$extraBalancing = 0;

    @Unique
    private boolean[] labs$excludePosUpdate = null;

    @Unique
    @Override
    public void labs$addBlacklistSide(EnumFacing side) {
        labs$blacklisted.add(side);
    }

    @Unique
    @Override
    public long labs$rawEnergy() {
        return energy;
    }

    @Unique
    @Override
    public void labs$setEnergy(long amt) {
        energy = amt;
    }

    @Inject(method = "update",
            at = @At(value = "INVOKE",
                     target = "Lnet/minecraft/world/World;getTotalWorldTime()J"),
            require = 1,
            remap = true,
            cancellable = true)
    private void replaceEnergyHandlingLogic(CallbackInfo ci) {
        ci.cancel();

        // Load Base Attribute Values from SolarInstance
        capacity.setBaseValue(instance.cap);
        transfer.setBaseValue(instance.transfer);

        // Handle Block Transfer (Upgrade) Update
        if (!traversal.isEmpty() && world.getWorldTime() % 20L == 0) {
            traversal.clear();
        }

        currentGeneration = getGeneration();

        labs$buildExclusionCache();
        labs$tickChargerAndUpgrades();

        energy = Math.min(energy + currentGeneration, capacity.getValueL());

        for (int i = 0; i < crafters.size(); ++i) {
            try {
                EntityPlayer player = crafters.get(i);
                if (player.openContainer instanceof ContainerBaseSolar) {
                    player.openContainer.detectAndSendChanges();
                } else {
                    crafters.remove(i);
                }
            } catch (Throwable ignored) {}
        }

        labs$buildTeCache();

        if (labs$timer == 0 || (labs$timer + labs$offset) % labs$getCfg().autoBalancingFrequency == 0) {
            labs$lastBalanceSuccess = labs$autoBalanceEnergy(true);
        }
        labs$autoPushEnergy();
        labs$timer++;
    }

    @Unique
    private void labs$buildExclusionCache() {
        if (labs$excludePosUpdate != null) return;

        labs$excludePosUpdate = new boolean[6];

        for (var side : EnumFacing.VALUES) {
            if (side == EnumFacing.UP) {
                labs$excludePosUpdate[side.ordinal()] = true;
                continue;
            }

            BlockPos offset = pos.offset(side);

            // Check for out of build area
            if ((side == EnumFacing.DOWN && offset.getY() < 0) || !world.getWorldBorder().contains(offset))
                labs$excludePosUpdate[side.ordinal()] = true;
        }
    }

    @Unique
    private void labs$buildTeCache() {
        labs$teCache = new TileEntity[6];
        labs$solarCache = new AccessibleTileBaseSolar[4];

        for (var side : EnumFacing.VALUES) {
            if (labs$excludePosUpdate[side.ordinal()] || labs$blacklisted.contains(side)) continue;

            BlockPos offset = pos.offset(side);

            // Don't exclude pos update, the air state can change
            // Fast check to prevent loading of chunks
            if (!world.isBlockLoaded(offset)) continue;

            // Don't exclude pos update, the air state can change
            // Fast check, using direct method from chunk and direct comparison
            // To save time in `Block#isAir` and `World.isOutsideBuildHeight`
            IBlockState state = world.getChunk(offset).getBlockState(offset);
            if (state.getBlock() == Blocks.AIR) continue;

            TileEntity te = world.getTileEntity(offset);

            if (te instanceof AccessibleTileBaseSolar solar) {
                // Don't exclude pos update, the air state can change
                // Skip TE's that are below this one, which are also solars
                if (side == EnumFacing.DOWN)
                    continue;

                labs$solarCache[side.getHorizontalIndex()] = solar;
            } else
                labs$teCache[side.ordinal()] = te;
        }
        labs$blacklisted.clear();
    }

    @Unique
    private void labs$tickChargerAndUpgrades() {
        if (((AccessibleInventoryDummy) upgradeInventory).labs$stateChanged() || labs$timer == 0)
            labs$updateUpgrades();

        labs$resetAttributes();
        labs$tickCharger();
    }

    @Unique
    private void labs$updateUpgrades() {
        labs$tickedUpgrades.clear();
        for (int i = 0; i < upgradeInventory.getSizeInventory(); i++) {
            ItemStack stack = upgradeInventory.getStackInSlot(i);
            if (stack.isEmpty()) continue;

            if (stack.getItem() instanceof ItemUpgrade upgrade &&
                    upgrade.canStayInPanel(labs$getThis(), stack, upgradeInventory)) {
                ResourceLocation id = stack.getItem().getRegistryName();

                if (!labs$tickedUpgrades.containsKey(id)) {
                    labs$tickedUpgrades.put(id, Pair.of(stack, stack.getCount()));
                    continue;
                }

                var pair = labs$tickedUpgrades.get(id);
                pair.setValue(pair.getValue() + stack.getCount());
            } else {
                upgradeInventory.setInventorySlotContents(i, ItemStack.EMPTY);
                Block.spawnAsEntity(world, pos, stack);
            }
        }
    }

    @Unique
    private void labs$resetAttributes() {
        generation.clearAttributes();
        transfer.clearAttributes();
        capacity.clearAttributes();

        for (var pair : labs$tickedUpgrades.values()) {
            var upgrade = (ItemUpgrade) pair.getKey().getItem();
            upgrade.update(labs$getThis(), pair.getKey(), pair.getValue());
        }
    }

    @Unique
    private void labs$tickCharger() {
        if (((AccessibleInventoryDummy) chargeInventory).labs$stateChanged() || labs$timer == 0) {
            labs$chargerEnergy = null;
            ItemStack stack = chargeInventory.getStackInSlot(0);

            if (stack.isEmpty()) return;

            IEnergyStorage storage = stack.getCapability(CapabilityEnergy.ENERGY, null);
            if (storage == null || !storage.canReceive() || storage.getEnergyStored() >= storage.getMaxEnergyStored())
                return;

            labs$chargerEnergy = storage;
        } else if (labs$chargerEnergy == null) return;

        if (!labs$chargerEnergy.canReceive() ||
                labs$chargerEnergy.getEnergyStored() >= labs$chargerEnergy.getMaxEnergyStored()) {
            labs$chargerEnergy = null;
            return;
        }

        energy -= labs$chargerEnergy.receiveEnergy(Math.min(getEnergyStored(), transfer.getValueI()), false);
    }

    @Unique
    private boolean labs$autoBalanceEnergy(boolean blacklist) {
        EnumFacing maxSide = null;
        long maxDelta = 0;
        long maxAbsDelta = 0;

        // Try all horizontal sides, find side that needs the most balancing
        for (var side : EnumFacing.HORIZONTALS) {
            if (labs$solarCache[side.getHorizontalIndex()] == null) continue;

            long rawEnergy = labs$solarCache[side.getHorizontalIndex()].labs$rawEnergy();
            if (energy == rawEnergy) continue;

            // noinspection UnstableApiUsage
            long delta = LongMath.saturatedSubtract(energy, rawEnergy);
            long absDelta = Math.abs(delta);
            if (absDelta <= maxAbsDelta) continue;

            maxSide = side;
            maxDelta = delta;
            maxAbsDelta = absDelta;
        }

        if (maxSide == null) return false;

        labs$autoBalanceSide(maxSide, maxDelta, maxAbsDelta, labs$solarCache[maxSide.getHorizontalIndex()], blacklist);
        return true;
    }

    @Unique
    private void labs$autoBalanceSide(EnumFacing side, long delta, long absDelta, AccessibleTileBaseSolar solar,
                                      boolean blacklist) {
        // Blacklist this TE from the other solar's handling
        if (blacklist)
            solar.labs$addBlacklistSide(side.getOpposite());

        // Quick Balance: Avg then Set
        long avg = LongMath.mean(energy, solar.labs$rawEnergy());

        // Fix energy being removed
        // If delta is odd, so difference is odd, therefore the sum would be odd
        // Delta & 1 will be 1 when odd, and 0 when even
        long energyHere = avg + (delta & 1);
        long energyThere = avg;

        // If the delta is big enough, provide extra energy
        if (currentGeneration > 0 && absDelta > currentGeneration * 0.75f) {
            long extraEnergy;

            if (absDelta > transfer.getValueI() * 0.75f)
                extraEnergy = transfer.getValueI();
            else
                extraEnergy = currentGeneration;

            extraEnergy = Math.min(extraEnergy, avg);

            if (delta > 0) {
                energyHere -= extraEnergy;
                energyThere += extraEnergy;
            } else {
                energyHere += extraEnergy;
                energyThere -= extraEnergy;
            }
        }

        // Check for capacity issues
        // This shouldn't create any capacity overflow because the solars started with energies below capacity
        if (energyHere > capacity.getValueI())
            energyThere += energyHere - capacity.getValueI();
        else if (energyThere > capacity.getValueI())
            energyHere += energyThere - capacity.getValueI();

        labs$setEnergy(energyHere);
        solar.labs$setEnergy(energyThere);
    }

    @Unique
    private void labs$autoPushEnergy() {
        labs$extraBalancing = 0;

        for (int i = 0; i < labs$teCache.length; i++) {
            labs$autoPushTo(labs$teCache[i], EnumFacing.byIndex(i).getOpposite());
        }

        if (!traversal.isEmpty()) {
            for (BlockPosFace traverse : traversal) {
                labs$autoPushTo(world.getTileEntity(traverse.pos), traverse.face);
            }
        }
    }

    @Unique
    private void labs$autoPushTo(TileEntity te, EnumFacing side) {
        if (te == null) return;

        if (!te.hasCapability(CapabilityEnergy.ENERGY, side)) return;

        IEnergyStorage storage = te.getCapability(CapabilityEnergy.ENERGY, side);
        if (storage == null || !storage.canReceive()) return;

        int stored = getEnergyStored();
        int max = transfer.getValueI();

        if (stored >= max || !labs$lastBalanceSuccess ||
                !labs$getCfg().extraBalancing || labs$extraBalancing >= labs$getCfg().extraBalancingAmount) {
            energy -= storage.receiveEnergy(Math.min(stored, max), false);
            return;
        }

        // Figure out how much the storage can receive
        max = Math.min(max, storage.receiveEnergy(max, true));

        if (stored >= max) {
            energy -= storage.receiveEnergy(max, false);
            return;
        }

        // Try to auto-balance
        stored = labs$autoPushBalance(max);
        energy -= storage.receiveEnergy(Math.min(stored, max), false);
    }

    @Unique
    private int labs$autoPushBalance(int target) {
        long currEnergy = energy;

        while (currEnergy < target && labs$extraBalancing < labs$getCfg().extraBalancingAmount) {
            // Reset energy for balancing purposes
            energy = 0;

            labs$lastBalanceSuccess = labs$autoBalanceEnergy(false);
            if (!labs$lastBalanceSuccess) break;

            labs$extraBalancing++;
            currEnergy += energy;
        }

        energy = currEnergy;
        return getEnergyStored();
    }

    @Unique
    private TileBaseSolar labs$getThis() {
        return (TileBaseSolar) (Object) this;
    }

    @Unique
    private static LabsConfig.ModIntegration.SolarFluxPerformanceOptions labs$getCfg() {
        return LabsConfig.modIntegration.solarFluxPerformanceOptions;
    }
}
