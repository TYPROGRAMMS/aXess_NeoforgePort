package net.teekay.axess.block.readers;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.teekay.axess.registry.AxessBlockEntityRegistry;
import org.jetbrains.annotations.Nullable;

public class KeycardReaderBlock extends AbstractKeycardReaderBlock {


    public KeycardReaderBlock() {
        this(Properties.ofFullCopy(Blocks.IRON_BLOCK)
                        .noOcclusion().strength(4f, 6f)
                        .requiresCorrectToolForDrops());
    }

    public KeycardReaderBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }


    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return AxessBlockEntityRegistry.KEYCARD_READER.get().create(pPos, pState);
    }


    @Override
    protected com.mojang.serialization.MapCodec<KeycardReaderBlock> codec() {
        return BlockBehaviour.simpleCodec(properties -> new KeycardReaderBlock(properties));
    }

}
