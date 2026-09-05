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

import java.util.Objects;

public class StCNetworkModifiedPacket implements IAxessPacket {
    public static final CustomPacketPayload.Type<StCNetworkModifiedPacket> TYPE = new CustomPacketPayload.Type<>(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(Axess.MODID, "network_modified"));
    public static final StreamCodec<RegistryFriendlyByteBuf, StCNetworkModifiedPacket> STREAM_CODEC = IAxessPacket.codec(StCNetworkModifiedPacket::new);

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() { return TYPE; }
    public AccessNetwork network;

    public StCNetworkModifiedPacket(AccessNetwork network) {
        this.network = network;
    }

    public StCNetworkModifiedPacket(FriendlyByteBuf buffer) {
        this.network = AccessNetwork.fromNBT(Objects.requireNonNull(buffer.readNbt()));
    }

    @Override
    public void encode(FriendlyByteBuf buffer) {
        buffer.writeNbt(network.toNBT());
    }

    @Override
    public void handle(IPayloadContext context) {


        AccessNetworkDataClient.setNetwork(network);
    }
}
