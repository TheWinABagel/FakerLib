package dev.shadowsoffire.placebo.events;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.Map;

/**
 * This event is fired whenever the enchantment level of a particular item is requested for gameplay purposes.<br>
 * It is called from {@link IForgeItemStack#getEnchantmentLevel(Enchantment)} and {@link IForgeItemStack#getAllEnchantments()}.
 * <p>
 * It is not fired for interactions with NBT, which means these changes will not reflect in the item tooltip.
 * <p>
 * This event is fired on the {@link MinecraftForge#EVENT_BUS}.<br>
 * This event is not cancellable.<br>
 * This event does not have a result.
 */
public class GetEnchantmentLevelEvent  {

    protected final ItemStack stack;
    protected final Map<Enchantment, Integer> enchantments;

    public GetEnchantmentLevelEvent(ItemStack stack, Map<Enchantment, Integer> enchantments) {
        this.stack = stack;
        this.enchantments = enchantments;
    }

    /**
     * Returns the item stack that is being queried.
     */
    public ItemStack getStack() {
        return this.stack;
    }

    /**
     * Returns the mutable enchantment->level map.
     */
    public Map<Enchantment, Integer> getEnchantments() {
        return this.enchantments;
    }

}
