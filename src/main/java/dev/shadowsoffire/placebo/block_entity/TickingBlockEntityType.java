package dev.shadowsoffire.placebo.block_entity;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

/**
 * A custom {@link BlockEntityType} that will automatically provide {@link BlockEntityTicker}s for {@linkplain TickingBlockEntity ticking block entities}.
 *
 * @param <T> The type of the ticking block entity.
 * @see TickingEntityBlock
 */
public class TickingBlockEntityType<T extends BlockEntity & TickingBlockEntity> extends BlockEntityType<T> {

    protected final boolean clientTick, serverTick;

    public TickingBlockEntityType(BlockEntitySupplier<? extends T> pFactory, Set<Block> pValidBlocks, boolean clientTick, boolean serverTick) {
        super(pFactory, pValidBlocks, null);
        this.clientTick = clientTick;
        this.serverTick = serverTick;
    }

    @Nullable
    public BlockEntityTicker<T> getTicker(boolean client) {
        if (client && this.clientTick) return (level, pos, state, entity) -> entity.clientTick(level, pos, state);
        else if (!client && this.serverTick) return (level, pos, state, entity) -> entity.serverTick(level, pos, state);
        return null;
    }

}
