package dev.shadowsoffire.placebo.registry;

import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.stats.Stat;
import net.minecraft.stats.StatType;
import net.minecraft.world.Container;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.material.Fluid;

import java.util.Locale;

public class RegistryHelper {

    protected final String modid;

    public RegistryHelper(String modid) {
        this.modid = modid;
    }

    public <T extends Block> T block(String path, T block) {
        return create(this.modid, path, BuiltInRegistries.BLOCK, block);
    }

    public <T extends Fluid> T fluid(String path, T fluid) {
        return create(this.modid, path, BuiltInRegistries.FLUID, fluid);
    }

    public <T extends Item> T item(String path, T item) {
        return create(this.modid, path, BuiltInRegistries.ITEM, item);
    }

    public <T extends CreativeModeTab> CreativeModeTab tab(ResourceKey<CreativeModeTab> tabKey, T tab) {
        return Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, tabKey, tab);
    }

    public <T extends MobEffect> T effect(String path, T effect) {
        return create(this.modid, path, BuiltInRegistries.MOB_EFFECT, effect);
    }

    public <T extends SoundEvent> T sound(String path, T sound) {
        return create(this.modid, path, BuiltInRegistries.SOUND_EVENT, sound);
    }

    public <T extends Potion> T potion(String path, T potion) {
        return create(this.modid, path, BuiltInRegistries.POTION, potion);
    }

    public <T extends Enchantment> T enchant(String path, T enchant) {
        return create(this.modid, path, BuiltInRegistries.ENCHANTMENT, enchant);
    }

    public <U extends Entity, T extends EntityType<U>> T entity(String path, T entityType) {
        return create(this.modid, path, BuiltInRegistries.ENTITY_TYPE, entityType);
    }

    public <U extends BlockEntity, T extends BlockEntityType<U>> T blockEntity(String path, T blockEntity) {
        return create(this.modid, path, BuiltInRegistries.BLOCK_ENTITY_TYPE, blockEntity);
    }

    public <U extends ParticleOptions, T extends ParticleType<U>> T particle(String path, T particle) {
        return create(this.modid, path, BuiltInRegistries.PARTICLE_TYPE, particle);
    }

    public <U extends AbstractContainerMenu, T extends MenuType<U>> T menu(String path, T menu) {
        return create(this.modid, path, BuiltInRegistries.MENU, menu);
    }

    public <T extends PaintingVariant> T painting(String path, T painting) {
        return create(this.modid, path, BuiltInRegistries.PAINTING_VARIANT, painting);
    }

    public <C extends Container, U extends Recipe<C>, T extends RecipeType<U>> T recipe(String path, T recipe) {
        return create(this.modid, path, BuiltInRegistries.RECIPE_TYPE, recipe);
    }

    public <C extends Container, U extends Recipe<C>, T extends RecipeSerializer<U>> T recipeSerializer(String path, T recipeSerializer) {
        return create(this.modid, path, BuiltInRegistries.RECIPE_SERIALIZER, recipeSerializer);
    }

    public <T extends Attribute> T attribute(String path, T attribute) {
        return create(this.modid, path, BuiltInRegistries.ATTRIBUTE, attribute);
    }

    public <S, U extends Stat<S>, T extends StatType<U>> T stat(String path, T stat) {
        return create(this.modid, path, BuiltInRegistries.STAT_TYPE, stat);
    }

    public <U extends FeatureConfiguration, T extends Feature<U>> T feature(String path, T feature) {
        return create(this.modid, path, BuiltInRegistries.FEATURE, feature);
    }

    public <T> T custom(String path, Registry<? super T> registry, T obj) {
        return create(this.modid, path, registry, obj);
    }

    public static <T> T create(String modid, String path, Registry<? super T> registry, T registeredObj) {
        return Registry.register(registry, new ResourceLocation(modid, path.toLowerCase(Locale.ROOT)), registeredObj);
    }

}
