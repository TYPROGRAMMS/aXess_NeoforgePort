package net.teekay.axess.network.packets.server;

import net.teekay.axess.Axess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.minecraft.server.level.ServerPlayer;
import net.teekay.axess.access.AccessNetwork;
import net.teekay.axess.access.AccessNetworkDataServer;
import net.teekay.axess.network.IAxessPacket;

import java.util.Objects;

public class CtSModifyNetworkPacket implements IAxessPacket {
    public static final CustomPacketPayload.Type<CtSModifyNetworkPacket> TYPE = new CustomPacketPayload.Type<>(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(Axess.MODID, "modify_network"));
    public static final StreamCodec<RegistryFriendlyByteBuf, CtSModifyNetworkPacket> STREAM_CODEC = IAxessPacket.codec(CtSModifyNetworkPacket::new);

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() { return TYPE; }
    public AccessNetwork network;

    public CtSModifyNetworkPacket(AccessNetwork network) {
        this.network = network;
    }

    public CtSModifyNetworkPacket(FriendlyByteBuf buffer) {
        this.network = AccessNetwork.fromNBT(Objects.requireNonNull(buffer.readNbt()));
    }

    @Override
    public void encode(FriendlyByteBuf buffer) {
        buffer.writeNbt(network.toNBT());
    }

    @Override
    public void handle(IPayloadContext context) {

        try {
            ServerPlayer player = (net.minecraft.server.level.ServerPlayer) context.player();
            if (player == null) return;
            AccessNetworkDataServer serverNetworkData = AccessNetworkDataServer.get(player.server);
            serverNetworkData.playerModifyNetwork(player, network);
        } catch (Exception e) {
            
            return;
        }
    }
}
