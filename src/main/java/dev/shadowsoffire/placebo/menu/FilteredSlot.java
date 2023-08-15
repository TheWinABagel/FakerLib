package dev.shadowsoffire.placebo.menu;

import com.google.common.base.Predicates;
import dev.shadowsoffire.placebo.cap.InternalItemHandler;
import io.github.fabricators_of_create.porting_lib.transfer.item.SlotItemHandler;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

/**
 * Extension of {@link SlotItemHandler} which takes a filter on what may enter the slot.
 */
public class FilteredSlot extends SlotItemHandler {

    protected final Predicate<ItemStack> filter;
    protected final int index;

    public FilteredSlot(InternalItemHandler handler, int index, int x, int y, Predicate<ItemStack> filter) {
        super(handler, index, x, y);
        this.filter = filter;
        this.index = index;
    }

    public FilteredSlot(InternalItemHandler handler, int index, int x, int y) {
        this(handler, index, x, y, Predicates.alwaysTrue());
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return this.filter.test(stack);
    }
/*
    @Override
    public boolean mayPickup(Player playerIn) {
        return !((InternalItemHandler) this.getItemHandler()).extractItemInternal(this.index, 1, true).isEmpty();
    }

    @Override
    public ItemStack remove(int amount) {
        return ((InternalItemHandler) this.getItemHandler()).extractItemInternal(this.index, amount, false);
    } TODO Reimplement/figure out how to use transfer api properly for this
*/
}
