package dev.shadowsoffire.placebo.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import dev.shadowsoffire.placebo.Placebo;
import dev.shadowsoffire.placebo.events.PlaceboEventFactory;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {

    @Inject(method = "getItemEnchantmentLevel", at = @At("RETURN"), cancellable = true)
        private static void initializeEnchLevelEvent(Enchantment enchantment, ItemStack stack, CallbackInfoReturnable<Integer> cir, @Local CompoundTag compoundTag){
        if (stack.isEmpty()) return;
        int result = PlaceboEventFactory.getEnchantmentLevelSpecific(EnchantmentHelper.getEnchantmentLevel(compoundTag), stack, enchantment);
        //Placebo.LOGGER.info("Enchantment level: {}, old: {}", result, EnchantmentHelper.getEnchantmentLevel(compoundTag));
        cir.setReturnValue(result);
    }



}
