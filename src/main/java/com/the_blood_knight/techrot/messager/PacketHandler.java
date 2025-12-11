package com.the_blood_knight.techrot.messager;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler {
    private static int packetId = 0;

    public static SimpleNetworkWrapper INSTANCE;

    public static void registerNetwork() {
        INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel("techrot");

        registerMessage(SyncDataPacket.Handler.class, SyncDataPacket.class, Side.CLIENT);
        registerMessage(SyncDataPacket.Handler.class, SyncDataPacket.class, Side.SERVER);
    }

    private static <REQ extends IMessage, REPLY extends IMessage> void registerMessage(
            Class<? extends IMessageHandler<REQ, REPLY>> handler,
            Class<REQ> packet,
            Side side) {
        INSTANCE.registerMessage(handler, packet, packetId++, side);
    }

    public static void sendTo(IMessage msg, EntityPlayerMP player) {
        INSTANCE.sendTo(msg, player);
    }

    public static void sendToAll(IMessage msg) {
        INSTANCE.sendToAll(msg);
    }

    public static void sendToServer(IMessage msg) {
        INSTANCE.sendToServer(msg);
    }

    public static void sendAround(IMessage msg, NetworkRegistry.TargetPoint point) {
        INSTANCE.sendToAllAround(msg, point);
    }
}
