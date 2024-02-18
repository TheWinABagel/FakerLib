package dev.shadowsoffire.placebo.recipe;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import dev.shadowsoffire.placebo.Placebo;
import dev.shadowsoffire.placebo.util.RunnableReloader;
import net.fabricmc.fabric.api.recipe.v1.ingredient.DefaultCustomIngredients;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.ApiStatus;

import java.util.*;
import java.util.function.Consumer;

@Deprecated // Go use a json
public final class RecipeHelper {

    /**
     * Creates an ItemStack out of an appropriate stack-like object.<br>
     * An {@link ItemLike} returns a new itemstack with a size of 1.<br>
     * An {@link ItemStack} will a itself.
     *
     * @param thing An {@link ItemLike} or {@link ItemStack}
     * @return An ItemStack representing <code>thing</code>.
     * @throws IllegalArgumentException if <code>thing</code> is not a valid type.
     */
    public static ItemStack makeStack(Object thing) {
        if (thing instanceof ItemStack stack) return stack;
        if (thing instanceof ItemLike il) return new ItemStack(il);
        throw new IllegalArgumentException("Attempted to create an ItemStack from something that cannot be converted: " + thing);
    }

    /**
     * Creates an ingredient list from an array of ingredient-like objects.<br>
     * A {@link String} will become a tag ingredient.<br>
     * An {@link ItemStack} will become a stack ingredient.<br>
     * An {@link ItemLike} will become an itemstack and become a stack ingredient.<br>
     * An {@link Ingredient} will be accepted directly without transformation.<br>
     * All other objects will be treated as {@link Ingredient#EMPTY} if <code>allowEmpty</code> is true.
     *
     * @param modid      The creator's modid for error logging.
     * @param allowEmpty If empty inputs are permitted in the output list.
     * @param inputArr   An array of potential input objects that are in-order.
     * @return A list of ingredients for a recipe.
     */
  @SuppressWarnings({ "rawtypes", "unchecked" })
    public static NonNullList<Ingredient> createInput(String modid, boolean allowEmpty, Object... inputArr) {
        NonNullList<Ingredient> inputL = NonNullList.create();
        for (int i = 0; i < inputArr.length; i++) {
            Object input = inputArr[i];
            if (input instanceof TagKey tag) inputL.add(i, Ingredient.of(tag));
            else if (input instanceof String str) inputL.add(i, Ingredient.of(TagKey.create(Registries.ITEM, new ResourceLocation(str))));
            else if (input instanceof ItemStack stack && !stack.isEmpty()) inputL.add(i, Ingredient.of(stack));
            else if (input instanceof Ingredient ing) inputL.add(i, ing);
            else if (allowEmpty) inputL.add(i, Ingredient.EMPTY);
            else throw new UnsupportedOperationException("Attempted to add invalid recipe.  Complain to the author of " + modid + ". (Input " + input + " not allowed.)");
        }
        return inputL;
    }

    public static Ingredient getIngredient(JsonElement json) {
        if (json == null || json.isJsonNull())
            throw new JsonSyntaxException("Json cannot be null");

        if (json.isJsonArray()) {
            List<Ingredient> ingredients = Lists.newArrayList();
            List<Ingredient> vanilla = Lists.newArrayList();
            json.getAsJsonArray().forEach((ele) -> {
                Ingredient ing = getIngredient(ele);

                if (ing.getClass() == Ingredient.class) //Vanilla, Due to how we read it splits each itemstack, so we pull out to re-merge later
                    vanilla.add(ing);
                else
                    ingredients.add(ing);
            });

            if (!vanilla.isEmpty())
                ingredients.add(Ingredient.fromValues(vanilla.stream().flatMap((i) -> Arrays.stream(i.values))));
            if (ingredients.size() == 0)
                throw new JsonSyntaxException("Item array cannot be empty, at least one item must be defined");

            if (ingredients.size() == 1)
                return ingredients.get(0);

            return DefaultCustomIngredients.any(ingredients.toArray(Ingredient[]::new));
        }

        if (!json.isJsonObject())
            throw new JsonSyntaxException("Expected ingredient to be a object or array of objects");

        return Ingredient.fromJson(json);
    }

}
