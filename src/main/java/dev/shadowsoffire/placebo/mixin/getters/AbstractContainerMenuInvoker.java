package dev.shadowsoffire.placebo.mixin.getters;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(AbstractContainerMenu.class)
public interface AbstractContainerMenuInvoker {

    @Invoker("moveItemStackTo")
    public boolean _moveItemStackTo(ItemStack pStack, int pStartIndex, int pEndIndex, boolean pReverseDirection);

}
