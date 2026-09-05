package net.teekay.axess.network.packets.client;

import net.teekay.axess.Axess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.teekay.axess.access.AccessNetwork;
import net.teekay.axess.access.AccessNetworkDataClient;
import net.teekay.axess.access.AccessNetworkDataServer;
import net.teekay.axess.network.IAxessPacket;

import java.util.UUID;

public class StCNetworkDeletedPacket implements IAxessPacket {
    public static final CustomPacketPayload.Type<StCNetworkDeletedPacket> TYPE = new CustomPacketPayload.Type<>(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(Axess.MODID, "network_deleted"));
    public static final StreamCodec<RegistryFriendlyByteBuf, StCNetworkDeletedPacket> STREAM_CODEC = IAxessPacket.codec(StCNetworkDeletedPacket::new);

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() { return TYPE; }
    public UUID deletedNetwork;

    public StCNetworkDeletedPacket(UUID networkDeleted) {
        this.deletedNetwork = networkDeleted;
    }

    public StCNetworkDeletedPacket(FriendlyByteBuf buffer) {
        this.deletedNetwork = buffer.readUUID();
    }

    @Override
    public void encode(FriendlyByteBuf buffer) {
        buffer.writeUUID(deletedNetwork);
    }

    @Override
    public void handle(IPayloadContext context) {

        if (AccessNetworkDataClient.getNetwork(deletedNetwork) == null) {
            return;
        }

        AccessNetworkDataClient.removeNetwork(deletedNetwork);
    }
}
