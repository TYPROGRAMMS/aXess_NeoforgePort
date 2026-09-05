package net.teekay.axess.network.packets.client;
import net.minecraft.nbt.CompoundTag;

import net.teekay.axess.Axess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.teekay.axess.network.IAxessPacket;
import net.teekay.axess.utilities.name_cache.ClientPlayerNameCache;
import net.teekay.axess.utilities.name_cache.ServerPlayerNameCache;

import java.util.Objects;

public class StCSyncPlayerNameCache implements IAxessPacket {
    public static final CustomPacketPayload.Type<StCSyncPlayerNameCache> TYPE = new CustomPacketPayload.Type<>(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(Axess.MODID, "sync_player_name_cache"));
    public static final StreamCodec<RegistryFriendlyByteBuf, StCSyncPlayerNameCache> STREAM_CODEC = IAxessPacket.codec(StCSyncPlayerNameCache::new);

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() { return TYPE; }
    public ServerPlayerNameCache serverDataModel;

    public StCSyncPlayerNameCache(ServerPlayerNameCache serverDataModel) {
        this.serverDataModel = serverDataModel;
    }

    public StCSyncPlayerNameCache(FriendlyByteBuf buffer) {
        this.serverDataModel = ServerPlayerNameCache.load(Objects.requireNonNull(buffer.readNbt()));
    }

    @Override
    public void encode(FriendlyByteBuf buffer) {
        buffer.writeNbt(serverDataModel.save(new CompoundTag(), null));
    }

    @Override
    public void handle(IPayloadContext context) {

        ClientPlayerNameCache.loadAllFromServer(serverDataModel);
    }
}
