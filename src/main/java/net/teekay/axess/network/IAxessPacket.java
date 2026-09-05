package net.teekay.axess.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.network.codec.StreamCodec;

/** Common marker for aXess NeoForge custom payloads. */
public interface IAxessPacket extends CustomPacketPayload {
    void encode(FriendlyByteBuf buffer);
    void handle(IPayloadContext context);

    static <T extends IAxessPacket> StreamCodec<RegistryFriendlyByteBuf, T> codec(java.util.function.Function<FriendlyByteBuf, T> decoder) {
        return StreamCodec.of((buf, msg) -> msg.encode(buf), decoder::apply);
    }
}
