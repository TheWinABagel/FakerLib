package dev.shadowsoffire.placebo.codec;

import com.google.common.collect.BiMap;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.shadowsoffire.placebo.Placebo;
import dev.shadowsoffire.placebo.json.ItemAdapter;
import io.github.fabricators_of_create.porting_lib.util.CraftingHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;

/**
 * Util class for codecs.
 */
public class PlaceboCodecs {

    /**
     * Creates a map-backed codec with a default codec to use as a fallback.
     *
     * param <V>          The type being de/serialized.
     * @param name         The name of the type being de/serialized for error logging.
     * @param reg          The codec map.
     * @param defaultCodec The default codec to use if the deserialized object has no type field.
     * @return A codec backed by the provided map, that will fallback if necessary.
     */
    public static <T extends CodecProvider<T>> Codec<T> mapBackedDefaulted(String name, BiMap<ResourceLocation, Codec<? extends T>> reg, Codec<? extends T> defaultCodec) {
        return new MapBackedCodec<>(name, reg, () -> defaultCodec);
    }

    /**
     * Creates a map-backed codec. Deserialized objects must have a 'type' field declaring the target codec name.
     *
     * param <V>  The type being de/serialized.
     * @param name The name of the type being de/serialized for error logging.
     * @param reg  The codec map.
     * @return A codec backed by the provided map.
     */
    public static <T extends CodecProvider<? super T>> Codec<T> mapBacked(String name, BiMap<ResourceLocation, Codec<? extends T>> reg) {
        return new MapBackedCodec<>(name, reg);
    }

    /**
     * Converts a codec into a set codec.
     */
    public static <T> Codec<Set<T>> setOf(Codec<T> elementCodec) {
        return setFromList(elementCodec.listOf());
    }

    /**
     * Converts a list codec into a set codec.
     */
    public static <T> Codec<Set<T>> setFromList(Codec<List<T>> listCodec) {
        return listCodec.<Set<T>>xmap(HashSet::new, ArrayList::new);
    }

    /**
     * Creates an enum codec using the lowercase name of the enum values as the keys.
     */
    public static <E extends Enum<E>> Codec<E> enumCodec(Class<E> clazz) {
        return ExtraCodecs.stringResolverCodec(e -> e.name().toLowerCase(Locale.ROOT), name -> Enum.valueOf(clazz, name.toUpperCase(Locale.ROOT)));
    }

    /**
     * Creates a string resolver codec for a type implementing {@link StringRepresentable}.
     */
    public static <T extends StringRepresentable> Codec<T> stringResolver(Function<String, T> decoder) {
        return ExtraCodecs.stringResolverCodec(StringRepresentable::getSerializedName, decoder);
    }

    /**
     * Creates a nullable field codec for use in {@link RecordCodecBuilder}.
     * <p>
     * Used to avoid swallowing exceptions during parse errors.
     *
     * @see NullableFieldCodec
     */
    public static <A> MapCodec<Optional<A>> nullableField(Codec<A> elementCodec, String name) {
        return new NullableFieldCodec<>(name, elementCodec);
    }

    /**
     * Creates a nullable field codec with the given default value for use in {@link RecordCodecBuilder}.
     * <p>
     * Serialized objects are expected to declare their serializer in the top-level 'type' key.
     *
     * @see PlaceboCodecs#mapBacked(String, BiMap)
     * @see PlaceboCodecs#mapBackedDefaulted(String, BiMap, Codec)
     */
    public static <A> MapCodec<A> nullableField(Codec<A> elementCodec, String name, A defaultValue) {
        return nullableField(elementCodec, name).xmap(o -> o.orElse(defaultValue), Optional::ofNullable);
    }

}
