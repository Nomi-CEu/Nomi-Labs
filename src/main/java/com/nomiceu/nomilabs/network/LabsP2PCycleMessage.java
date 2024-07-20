package com.nomiceu.nomilabs.network;

import java.util.Objects;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.nomiceu.nomilabs.LabsValues;
import com.nomiceu.nomilabs.NomiLabs;
import com.nomiceu.nomilabs.util.ItemMeta;

import appeng.api.AEApi;
import appeng.api.definitions.IItemDefinition;
import co.neeve.nae2.NAE2;
import io.netty.buffer.ByteBuf;

public class LabsP2PCycleMessage implements IMessage {

    private int hand;
    private byte offset;
    private int expectedSlot;

    @SuppressWarnings("DataFlowIssue")
    public LabsP2PCycleMessage() {
        hand = EnumHand.MAIN_HAND.ordinal();
        offset = 0;
        expectedSlot = 0;
    }

    public LabsP2PCycleMessage(EntityPlayer player, EnumHand hand, byte offset) {
        this.hand = hand.ordinal();
        this.offset = offset;
        this.expectedSlot = hand == EnumHand.MAIN_HAND ? player.inventory.currentItem : 0; // If offhand, doesn't matter
    }

    public int getOffset() {
        return offset;
    }

    public EnumHand getHand() {
        return EnumHand.values()[hand];
    }

    public boolean checkValid(EntityPlayer player) {
        return getHand() == EnumHand.OFF_HAND || player.inventory.currentItem == expectedSlot;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        hand = buf.readInt();
        offset = buf.readByte();
        expectedSlot = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(hand);
        buf.writeByte(offset);
        buf.writeInt(expectedSlot);
    }

    public static class MessageHandler implements IMessageHandler<LabsP2PCycleMessage, IMessage> {

        private static BiMap<ItemMeta, Integer> p2ps = null;

        public static BiMap<ItemMeta, Integer> getP2ps() {
            if (p2ps != null) return p2ps;

            p2ps = HashBiMap.create();

            // Add AE2 P2Ps
            var parts = AEApi.instance().definitions().parts();
            addP2pFromDef(parts.p2PTunnelME());
            addP2pFromDef(parts.p2PTunnelRedstone());
            addP2pFromDef(parts.p2PTunnelItems());
            addP2pFromDef(parts.p2PTunnelFluids());
            addP2pFromDef(parts.p2PTunnelLight());
            addP2pFromDef(parts.p2PTunnelFE());
            addP2pFromDef(parts.p2PTunnelEU());
            addP2pFromDef(parts.p2PTunnelGTEU());

            // Add NAE2 P2Ps
            if (Loader.isModLoaded(LabsValues.NAE2_MODID))
                addNae2P2ps();

            return p2ps;
        }

        @Optional.Method(modid = LabsValues.NAE2_MODID)
        private static void addNae2P2ps() {
            addP2pFromDef(NAE2.definitions().parts().p2pTunnelInterface());
        }

        private static void addP2pFromDef(IItemDefinition def) {
            def.maybeStack(1).ifPresent(itemStack -> p2ps.put(new ItemMeta(itemStack), p2ps.size()));
        }

        private static void tryCycle(EntityPlayer player, LabsP2PCycleMessage cycle) {
            if (cycle.checkValid(player)) {
                ItemStack heldStack = player.getHeldItem(cycle.getHand());
                ItemMeta held = new ItemMeta(heldStack);
                if (!getP2ps().containsKey(held)) {
                    var profile = player.getGameProfile();
                    String item = Objects.toString(held.getItem().getRegistryName(), "an unregistered item");
                    NomiLabs.LOGGER.warn("{} ({}) tried to cycle {}", profile.getName(), profile.getId(), item);
                    return;
                }
                ItemStack next = getP2ps().inverse().get((getP2ps().get(held) + 1) % p2ps.size()).toStack();
                next.setCount(heldStack.getCount());
                next.setTagCompound(heldStack.getTagCompound());
                player.setHeldItem(cycle.getHand(), next);
            }
        }

        @Override
        public IMessage onMessage(LabsP2PCycleMessage message, MessageContext ctx) {
            if (ctx.side == Side.CLIENT) return null;

            FMLCommonHandler.instance().getMinecraftServerInstance()
                    .addScheduledTask(() -> tryCycle(ctx.getServerHandler().player, message));
            return null;
        }
    }
}
