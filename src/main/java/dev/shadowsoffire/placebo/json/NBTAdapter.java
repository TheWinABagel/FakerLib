package dev.shadowsoffire.placebo.json;

import com.google.gson.*;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import dev.shadowsoffire.placebo.Placebo;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;

import java.lang.reflect.Type;
import java.util.function.Function;

public class NBTAdapter implements JsonDeserializer<CompoundTag>, JsonSerializer<CompoundTag> {

    @Deprecated
    public static final NBTAdapter INSTANCE = new NBTAdapter();

    public static final Codec<CompoundTag> EITHER_CODEC = Codec.either(Codec.STRING, CompoundTag.CODEC).xmap(either -> either.map(t -> {
        try {
            return TagParser.parseTag(t);
        }
        catch (CommandSyntaxException e) {
            throw new RuntimeException(e);
        }
    }, Function.identity()), Either::right);

    @Override
    public JsonElement serialize(CompoundTag src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.toString());
    }

    @Override
    public CompoundTag deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return EITHER_CODEC.parse(JsonOps.INSTANCE, json).getOrThrow(false, Placebo.LOGGER::error);
    }

}
