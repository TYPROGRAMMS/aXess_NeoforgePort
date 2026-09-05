package net.teekay.axess.network.packets.server;

import net.teekay.axess.Axess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.teekay.axess.access.AccessNetworkDataServer;
import net.teekay.axess.network.IAxessPacket;

import java.util.Objects;
import java.util.UUID;

public class CtSDeleteNetworkPacket implements IAxessPacket {
    public static final CustomPacketPayload.Type<CtSDeleteNetworkPacket> TYPE = new CustomPacketPayload.Type<>(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(Axess.MODID, "delete_network"));
    public static final StreamCodec<RegistryFriendlyByteBuf, CtSDeleteNetworkPacket> STREAM_CODEC = IAxessPacket.codec(CtSDeleteNetworkPacket::new);

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() { return TYPE; }
    public UUID deletedNetwork;

    public CtSDeleteNetworkPacket(UUID networkDeleted) {
        this.deletedNetwork = networkDeleted;
    }

    public CtSDeleteNetworkPacket(FriendlyByteBuf buffer) {
        this.deletedNetwork = buffer.readUUID();
    }

    @Override
    public void encode(FriendlyByteBuf buffer) {
        buffer.writeUUID(deletedNetwork);
    }

    @Override
    public void handle(IPayloadContext context) {

        try {
            var player = (net.minecraft.server.level.ServerPlayer) context.player();
            if (player == null) return;
            AccessNetworkDataServer serverData = AccessNetworkDataServer.get(player.server);
            serverData.playerDeleteNetwork(player, deletedNetwork);
        } catch (Exception e) {
            
            return;
        }

        
    }
}
