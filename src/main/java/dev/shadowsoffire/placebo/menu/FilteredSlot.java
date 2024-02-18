package dev.shadowsoffire.placebo.menu;

import com.google.common.base.Predicates;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

/**
 * Extension of {@link Slot} which takes a filter on what may enter the slot.
 */
public class FilteredSlot extends Slot {

    protected final Predicate<ItemStack> filter;
    protected final int index;

    public FilteredSlot(Container container, int index, int x, int y, Predicate<ItemStack> filter) {
        super(container, index, x, y);
        this.filter = filter;
        this.index = index;
    }

    public FilteredSlot(Container container, int index, int x, int y) {
        this(container, index, x, y, Predicates.alwaysTrue());
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return this.filter.test(stack);
    }

    @Override
    public boolean mayPickup(Player player) {
        return super.mayPickup(player);
    }
}
