package dev.shadowsoffire.placebo.recipe;

import net.fabricmc.fabric.impl.recipe.ingredient.builtin.NbtIngredient;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

@Deprecated(forRemoval = true)
public class NBTIngredient extends NbtIngredient { //probably not neccessary?
    public NBTIngredient(Ingredient base, @Nullable CompoundTag nbt, boolean strict) {
        super(base, nbt, strict);
    }
/*
    public NBTIngredient(ItemStack stack) {
        super(Set.of(stack.getItem()), stack.getTag(), false);
    }*/

}
