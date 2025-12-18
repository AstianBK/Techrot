package com.the_blood_knight.techrot.messager;

import com.the_blood_knight.techrot.Techrot;
import com.the_blood_knight.techrot.common.api.ITechRotPlayer;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class SyncDataPacket implements IMessage {

    public NBTTagCompound data;

    public SyncDataPacket() {}

    public SyncDataPacket(NBTTagCompound tag) {
        this.data = tag;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.data = ByteBufUtils.readTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeTag(buf, data);
    }

    public static class Handler implements IMessageHandler<SyncDataPacket, IMessage> {
        @Override
        public IMessage onMessage(SyncDataPacket message, MessageContext ctx) {
            if(ctx.side == Side.CLIENT){
                Minecraft.getMinecraft().addScheduledTask(() -> {
                    EntityPlayerSP player = Minecraft.getMinecraft().player;

                    ITechRotPlayer cap = player.getCapability(Techrot.CapabilityRegistry.PLAYER_UPGRADES, null);

                    cap.getInventory().deserializeNBT(message.data.getCompoundTag("Inv"));
                    cap.setHeartRot(message.data.getInteger("rotHealth"));
                    cap.setFly(message.data.getBoolean("fly"));
                    cap.setCombustible(message.data.getInteger("combustible"));
                });
            }else {
                EntityPlayerMP playerMP = ctx.getServerHandler().player;
                ITechRotPlayer cap = playerMP.getCapability(Techrot.CapabilityRegistry.PLAYER_UPGRADES, null);

                cap.getInventory().deserializeNBT(message.data.getCompoundTag("Inv"));
                cap.setHeartRot(message.data.getInteger("rotHealth"));
                cap.setFly(message.data.getBoolean("fly"));
                cap.setCombustible(message.data.getInteger("combustible"));

            }
            return null;
        }
    }
}