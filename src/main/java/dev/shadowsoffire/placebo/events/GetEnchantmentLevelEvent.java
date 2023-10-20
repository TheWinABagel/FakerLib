package dev.shadowsoffire.placebo.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;

/**
 * This event is fired whenever the enchantment level of a particular item is requested for gameplay purposes.<br>
 * It is called from {@link net.minecraft.world.item.enchantment.EnchantmentHelper#getEnchantmentLevel(Enchantment, LivingEntity)} (Enchantment)} and {@link net.minecraft.world.item.enchantment.EnchantmentHelper#getEnchantments(ItemStack)}.
 * <p>
 * It is not fired for interactions with NBT, which means these changes will not reflect in the item tooltip.
 * <p>
 * This event is not cancellable.<br>
 * This event does not have a result.
 */
public interface GetEnchantmentLevelEvent  {


    Event<GetEnchantmentLevelEvent> GET_ENCHANTMENT_LEVEL = EventFactory.createArrayBacked(GetEnchantmentLevelEvent.class,
            (listeners) -> (enchantments, stack) -> {
                for (GetEnchantmentLevelEvent listener : listeners) {
                    Map<Enchantment, Integer> result = listener.onEnchantRequest(enchantments, stack);
                    return result;
                }

                return enchantments;
            });

    Map<Enchantment, Integer> onEnchantRequest(Map<Enchantment, Integer> enchantments, ItemStack stack);
}
