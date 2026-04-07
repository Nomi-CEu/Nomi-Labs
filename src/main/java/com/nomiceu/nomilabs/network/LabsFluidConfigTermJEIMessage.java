package com.nomiceu.nomilabs.network;

import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import appeng.api.storage.data.IAEFluidStack;
import appeng.container.implementations.ContainerFluidInterfaceConfigurationTerminal;
import appeng.fluids.util.AEFluidStack;
import io.netty.buffer.ByteBuf;

public class LabsFluidConfigTermJEIMessage implements IMessage {

    private int slot;
    private long id;
    private String fluidName;

    public LabsFluidConfigTermJEIMessage() {
        slot = 0;
        id = 0L;
        fluidName = "";
    }

    public LabsFluidConfigTermJEIMessage(int slot, long id, FluidStack fluid) {
        this.slot = slot;
        this.id = id;
        this.fluidName = fluid.getFluid().getName();
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        slot = buf.readInt();
        id = buf.readLong();
        fluidName = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(slot);
        buf.writeLong(id);
        ByteBufUtils.writeUTF8String(buf, fluidName);
    }

    public static class MessageHandler implements IMessageHandler<LabsFluidConfigTermJEIMessage, IMessage> {

        @Override
        public IMessage onMessage(LabsFluidConfigTermJEIMessage message, MessageContext ctx) {
            if (!ctx.side.isServer()) return null;

            if (!(ctx
                    .getServerHandler().player.openContainer instanceof ContainerFluidInterfaceConfigurationTerminal term))
                return null;

            ContainerFluidInterfaceConfigurationTerminal.FluidConfigTracker inv = term.getSlotByID(message.id);
            IAEFluidStack aefs = AEFluidStack
                    .fromFluidStack(new FluidStack(FluidRegistry.getFluid(message.fluidName), 1000));

            if (inv != null) {
                inv.getServer().setFluidInSlot(message.slot, aefs);
            }

            return null;
        }
    }
}
