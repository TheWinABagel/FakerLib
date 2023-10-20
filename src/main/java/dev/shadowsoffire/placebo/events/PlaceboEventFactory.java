package dev.shadowsoffire.placebo.events;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.HashMap;
import java.util.Map;

public class PlaceboEventFactory {

    public static InteractionResult onItemUse(ItemStack stack, UseOnContext ctx) {
        var event = new ItemUseEvent(ctx, stack);
        boolean isCancelled = ItemUseEvent.ItemUse.ITEM_USE_EVENT.invoker().itemUse(event);
        if (isCancelled) return event.cancellationResult;
        return null;
    }

    /**
     * Called from {link IForgeItemStack#getEnchantmentLevel(Enchantment)}
     * Injected via coremods/get_ench_level_event_specific.js
     */
    public static int getEnchantmentLevelSpecific(int level, ItemStack stack, Enchantment ench) {
        var enchMap = new HashMap<Enchantment, Integer>();
        enchMap.put(ench, level);
        var eventResult = GetEnchantmentLevelEvent.GET_ENCHANTMENT_LEVEL.invoker().onEnchantRequest(enchMap, stack);
        return eventResult.get(ench);
    }

    /**
     * Called from {link IForgeItemStack#getAllEnchantments()}
     * Injected via coremods/get_ench_level_event.js
     */
    public static Map<Enchantment, Integer> getEnchantmentLevel(Map<Enchantment, Integer> enchantments, ItemStack stack) {
        enchantments = new HashMap<>(enchantments);
        GetEnchantmentLevelEvent.GET_ENCHANTMENT_LEVEL.invoker().onEnchantRequest(enchantments, stack);
        return enchantments;
    }
}
