package dev.shadowsoffire.placebo.events;

import com.google.common.base.Preconditions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This event is fired when an item would be used via {@link Item#useOn}<br>
 * It allows for the usage to be changed or cancelled.<br>
 * This event allows finer-tuned control over the actual item usage that cannot be achieved by using {@link RightClickBlock}
 */
public class ItemUseEvent { //Could probably be replaced with AttackEntityCallback

    private final InteractionHand hand;
    private final BlockPos pos;
    @Nullable
    private final Direction face;
    private InteractionResult cancellationResult = InteractionResult.PASS;
    private final UseOnContext ctx;

    public ItemUseEvent(UseOnContext ctx) {
        this.hand = Preconditions.checkNotNull(ctx.getHand(), "Null hand in ItemUseEvent!");
        this.pos = Preconditions.checkNotNull(ctx.getClickedPos(), "Null position in ItemUseEvent!");
        this.face = ctx.getClickedFace();
        this.ctx = ctx;
    }

    public UseOnContext getContext() {
        return this.ctx;
    }



    /**
     * @return The hand involved in this interaction. Will never be null.
     */
    @NotNull
    public InteractionHand getHand() {
        return this.hand;
    }

    /**
     * @return The itemstack involved in this interaction, {@code ItemStack.EMPTY} if the hand was empty.
     */
    @NotNull
    public ItemStack getItemStack() {
        return this.ctx.getItemInHand();
    }

    /**
     * If the interaction was on an entity, will be a BlockPos centered on the entity.
     * If the interaction was on a block, will be the position of that block.
     * Otherwise, will be a BlockPos centered on the player.
     * Will never be null.
     *
     * @return The position involved in this interaction.
     */
    @NotNull
    public BlockPos getPos() {
        return this.pos;
    }

    /**
     * @return The face involved in this interaction. For all non-block interactions, this will return null.
     */
    @Nullable
    public Direction getFace() {
        return this.face;
    }

    /**
     * @return Convenience method to get the world of this interaction.
     */
    public Level getLevel() {
        return this.ctx.getLevel();
    }

    /**
     * @return The InteractionResult that will be returned to vanilla if the event is cancelled, instead of calling the relevant
     *         method of the event. By default, this is {@link InteractionResult#PASS}, meaning cancelled events will cause
     *         the client to keep trying more interactions until something works.
     */
    public InteractionResult getCancellationResult() {
        return this.cancellationResult;
    }

    /**
     * Set the InteractionResult that will be returned to vanilla if the event is cancelled, instead of calling the relevant
     * method of the event.
     * Note that this only has an effect on {@link RightClickBlock}, {@link RightClickItem}, {@link EntityInteract}, and {@link EntityInteractSpecific}.
     */
    public void setCancellationResult(InteractionResult result) {
        this.cancellationResult = result;
    }
}
