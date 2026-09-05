package net.teekay.axess.network.packets.server;

import net.teekay.axess.Axess;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.teekay.axess.access.AccessLevel;
import net.teekay.axess.access.AccessNetwork;
import net.teekay.axess.access.AccessNetworkDataServer;
import net.teekay.axess.access.AccessPermission;
import net.teekay.axess.block.keycardeditor.KeycardEditorBlockEntity;
import net.teekay.axess.item.keycard.AbstractKeycardItem;
import net.teekay.axess.network.IAxessPacket;
import net.teekay.axess.screen.KeycardEditorMenu;

import java.util.Optional;
import java.util.UUID;

public class CtSModifyKeycardPacket implements IAxessPacket {
    public static final CustomPacketPayload.Type<CtSModifyKeycardPacket> TYPE = new CustomPacketPayload.Type<>(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(Axess.MODID, "modify_keycard"));
    public static final StreamCodec<RegistryFriendlyByteBuf, CtSModifyKeycardPacket> STREAM_CODEC = IAxessPacket.codec(CtSModifyKeycardPacket::new);

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() { return TYPE; }
    public BlockPos blockEntityPos;
    public UUID networkUUID;
    public UUID accessLevelUUID;

    public CtSModifyKeycardPacket(BlockPos blockEntityPos, AccessNetwork network, AccessLevel level) {
        this.blockEntityPos = blockEntityPos;
        this.networkUUID = network.getUUID();
        this.accessLevelUUID = level.getUUID();
    }

    public CtSModifyKeycardPacket(FriendlyByteBuf buffer) {
        this.blockEntityPos = buffer.readBlockPos();
        this.networkUUID = buffer.readUUID();
        this.accessLevelUUID = buffer.readUUID();
    }

    @Override
    public void encode(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(blockEntityPos);
        buffer.writeUUID(networkUUID);
        buffer.writeUUID(accessLevelUUID);
    }

    @Override
    public void handle(IPayloadContext context) {

        context.enqueueWork(() -> {
            try {
                ServerPlayer player = (net.minecraft.server.level.ServerPlayer) context.player();
                if (player == null) return;

                if (!(player.containerMenu instanceof KeycardEditorMenu menu)) return;
                KeycardEditorBlockEntity keycardEditor = menu.blockEntity;

                IItemHandler itemHandler = keycardEditor.getItemHandler();
                ItemStack stack = itemHandler.getStackInSlot(KeycardEditorBlockEntity.KEYCARD_SLOT);
                if (!(stack.getItem() instanceof AbstractKeycardItem keycardItem)) return;

                AccessNetwork prevNetwork = keycardItem.getAccessNetwork(stack, player.level());
                if (prevNetwork != null) if (!prevNetwork.hasPermission(player, AccessPermission.KEYCARD_ASSIGN)) return;

                AccessNetworkDataServer serverNetworkData = AccessNetworkDataServer.get(player.getServer());
                AccessNetwork network = serverNetworkData.getNetwork(networkUUID);
                if (network == null) return;

                if (!network.hasPermission(player, AccessPermission.KEYCARD_ASSIGN)) return;

                AccessLevel accessLevel = network.getAccessLevel(accessLevelUUID);
                if (accessLevel == null) return;

                keycardItem.setAccessNetwork(stack, network);
                keycardItem.setAccessLevel(stack, accessLevel);

                //stack.setHoverName(Component.literal(accessLevel.getName()).append(" ").append(Component.translatable("item." + Axess.MODID + ".keycard")));

                keycardEditor.setChanged();
            } catch (Exception e) {}
        });
    }
}
