package dev.shadowsoffire.placebo.events;

import com.google.common.base.Preconditions;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
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
 */
public class ItemUseEvent {

    public final InteractionHand hand;
    public final BlockPos pos;
    @Nullable
    public final Direction face;
    public InteractionResult cancellationResult = InteractionResult.PASS;
    public final UseOnContext ctx;
    public ItemStack stack;

    public ItemUseEvent(UseOnContext ctx, ItemStack stack) {
        this.hand = Preconditions.checkNotNull(ctx.getHand(), "Null hand in ItemUseEvent!");
        this.pos = Preconditions.checkNotNull(ctx.getClickedPos(), "Null position in ItemUseEvent!");
        this.face = ctx.getClickedFace();
        this.ctx = ctx;
        this.stack = stack;
    }

    /**
     * @return Convenience method to get the world of this interaction.
     */
    public Level getLevel() {
        return this.ctx.getLevel();
    }



    public interface ItemUse {
        public static final Event<OnItemUse> ITEM_USE_EVENT = EventFactory.createArrayBacked(OnItemUse.class, callbacks -> (event) -> {
            for (OnItemUse callback : callbacks) {
                if (callback.itemUse(event)) return true;
            }
            return false;
        });
    }

    @FunctionalInterface
    public interface OnItemUse {
        boolean itemUse(ItemUseEvent event);

    }


}
