package net.teekay.axess.network.packets.client;

import net.teekay.axess.Axess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.teekay.axess.network.IAxessPacket;
import net.teekay.axess.utilities.name_cache.ClientPlayerNameCache;

import java.util.UUID;

public class StCPlayerCacheModifiedPacket implements IAxessPacket {
    public static final CustomPacketPayload.Type<StCPlayerCacheModifiedPacket> TYPE = new CustomPacketPayload.Type<>(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(Axess.MODID, "player_cache_modified"));
    public static final StreamCodec<RegistryFriendlyByteBuf, StCPlayerCacheModifiedPacket> STREAM_CODEC = IAxessPacket.codec(StCPlayerCacheModifiedPacket::new);

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() { return TYPE; }
    public UUID uuid;
    public String name;

    public StCPlayerCacheModifiedPacket(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }

    public StCPlayerCacheModifiedPacket(FriendlyByteBuf buffer) {
        this.uuid = buffer.readUUID();
        this.name = buffer.readUtf();
    }

    @Override
    public void encode(FriendlyByteBuf buffer) {
        buffer.writeUUID(uuid);
        buffer.writeUtf(name, 128);
    }

    @Override
    public void handle(IPayloadContext context) {


        ClientPlayerNameCache.setName(uuid, name);
    }
}
