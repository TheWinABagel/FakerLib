package dev.shadowsoffire.placebo.cap;

import io.github.fabricators_of_create.porting_lib.transfer.item.ItemStackHandler;
import net.minecraft.world.item.ItemStack;

public class InternalItemHandler extends ItemStackHandler {

    public InternalItemHandler(int size) {
        super(size);
    }
/*
    public ItemStack extractItemInternal(int slot, int amount, boolean simulate) {
        return super.extractItem(slot, amount, simulate);
    }

    public ItemStack insertItemInternal(int slot, ItemStack stack, boolean simulate) {
        return super.insertItem(slot, stack, simulate);
    } TODO same deal as FilteredSlot
*/}
