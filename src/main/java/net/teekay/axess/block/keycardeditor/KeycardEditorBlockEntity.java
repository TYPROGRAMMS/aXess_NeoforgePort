package net.teekay.axess.block.keycardeditor;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import net.minecraft.core.HolderLookup;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.teekay.axess.Axess;
import net.teekay.axess.item.keycard.AbstractKeycardItem;
import net.teekay.axess.registry.AxessBlockEntityRegistry;
import net.teekay.axess.screen.KeycardEditorMenu;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class KeycardEditorBlockEntity extends BlockEntity implements MenuProvider {
    public class KeycardItemStackHandler extends ItemStackHandler {
        private final Runnable onChangedCallback;

        public KeycardItemStackHandler(int i, Runnable onChanged) {
            super(i);
            onChangedCallback = onChanged;
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return super.isItemValid(slot, stack) && (stack.getItem() instanceof AbstractKeycardItem);
        }

        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            if (onChangedCallback != null) onChangedCallback.run();

            if (level != null && !level.isClientSide()) {
                level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
            }
        }
    }


    private static final Component TITLE = Component.translatable("gui." + Axess.MODID + ".keycard_editor");

    private final ItemStackHandler itemStackHandler = new KeycardItemStackHandler(1, this::setChanged);
    public static final int KEYCARD_SLOT = 0;

    


    public KeycardEditorBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(AxessBlockEntityRegistry.KEYCARD_EDITOR.get(), pPos, pBlockState);
    }

    public IItemHandler getItemHandler() { return itemStackHandler; }

    public void drops() {
        SimpleContainer inv = new SimpleContainer(itemStackHandler.getSlots());

        for (int i = 0; i < itemStackHandler.getSlots(); i++) {
            inv.setItem(i, itemStackHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inv);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new KeycardEditorMenu(pContainerId, pPlayerInventory, this);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        
    }

    @Override
    public void loadAdditional(CompoundTag pTag, HolderLookup.Provider registries) {

        itemStackHandler.deserializeNBT(registries, pTag.getCompound("inventory"));

        super.loadAdditional(pTag, registries);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider registries) {
        pTag.put("inventory", itemStackHandler.serializeNBT(registries));
        super.saveAdditional(pTag, registries);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = super.getUpdateTag(registries);
        saveAdditional(tag, registries);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider registries) {
        loadAdditional(tag, registries);
    }


    @Override
    public Component getDisplayName() {
        return TITLE;
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider registries) {
        loadAdditional(pkt.getTag(), registries);
    }


}
