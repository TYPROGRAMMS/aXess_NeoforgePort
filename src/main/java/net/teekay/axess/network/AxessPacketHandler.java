package net.teekay.axess.network;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.teekay.axess.network.packets.client.*;
import net.teekay.axess.network.packets.server.*;

/** NeoForge 1.21.1 custom-payload registration and send helpers. */
public final class AxessPacketHandler {
    private AxessPacketHandler() {}

    public static void registerPayloads(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");
        registrar.playToServer(CtSDeleteNetworkPacket.TYPE, CtSDeleteNetworkPacket.STREAM_CODEC, CtSDeleteNetworkPacket::handle);
        registrar.playToServer(CtSModifyNetworkPacket.TYPE, CtSModifyNetworkPacket.STREAM_CODEC, CtSModifyNetworkPacket::handle);
        registrar.playToServer(CtSModifyKeycardPacket.TYPE, CtSModifyKeycardPacket.STREAM_CODEC, CtSModifyKeycardPacket::handle);
        registrar.playToServer(CtSModifyKeycardReaderPacket.TYPE, CtSModifyKeycardReaderPacket.STREAM_CODEC, CtSModifyKeycardReaderPacket::handle);
        registrar.playToClient(StCNetworkDeletedPacket.TYPE, StCNetworkDeletedPacket.STREAM_CODEC, StCNetworkDeletedPacket::handle);
        registrar.playToClient(StCNetworkModifiedPacket.TYPE, StCNetworkModifiedPacket.STREAM_CODEC, StCNetworkModifiedPacket::handle);
        registrar.playToClient(StCSyncAllNetworks.TYPE, StCSyncAllNetworks.STREAM_CODEC, StCSyncAllNetworks::handle);
        registrar.playToClient(StCPlayerCacheModifiedPacket.TYPE, StCPlayerCacheModifiedPacket.STREAM_CODEC, StCPlayerCacheModifiedPacket::handle);
        registrar.playToClient(StCSyncPlayerNameCache.TYPE, StCSyncPlayerNameCache.STREAM_CODEC, StCSyncPlayerNameCache::handle);
    }


    public static void sendToServer(CustomPacketPayload payload) { PacketDistributor.sendToServer(payload); }
    public static void sendToPlayer(CustomPacketPayload payload, ServerPlayer player) { PacketDistributor.sendToPlayer(player, payload); }
    public static void sendToAllClients(CustomPacketPayload payload) { PacketDistributor.sendToAllPlayers(payload); }
}
