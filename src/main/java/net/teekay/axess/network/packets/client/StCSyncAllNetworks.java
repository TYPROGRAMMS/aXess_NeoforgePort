package net.teekay.axess.network.packets.client;
import net.minecraft.nbt.CompoundTag;

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
import org.checkerframework.checker.units.qual.C;

import java.util.Objects;

public class StCSyncAllNetworks implements IAxessPacket {
    public static final CustomPacketPayload.Type<StCSyncAllNetworks> TYPE = new CustomPacketPayload.Type<>(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(Axess.MODID, "sync_all_networks"));
    public static final StreamCodec<RegistryFriendlyByteBuf, StCSyncAllNetworks> STREAM_CODEC = IAxessPacket.codec(StCSyncAllNetworks::new);

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() { return TYPE; }
    public AccessNetworkDataServer serverDataModel;

    public StCSyncAllNetworks(AccessNetworkDataServer serverDataModel) {
        this.serverDataModel = serverDataModel;
    }

    public StCSyncAllNetworks(FriendlyByteBuf buffer) {
        this.serverDataModel = AccessNetworkDataServer.load(Objects.requireNonNull(buffer.readNbt()));
    }

    @Override
    public void encode(FriendlyByteBuf buffer) {
        buffer.writeNbt(serverDataModel.save(new CompoundTag(), null));
    }

    @Override
    public void handle(IPayloadContext context) {

        AccessNetworkDataClient.loadAllFromServer(serverDataModel);
    }
}
